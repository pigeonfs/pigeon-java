package io.github.pigeonfs;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.pigeonfs.emails.CreateEmailOptions;
import io.github.pigeonfs.emails.CreateEmailResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

/**
 * Official Java client for the Pigeon email API.
 * Shape follows resend-java: {@code new Pigeon(apiKey).emails().send(params)}.
 */
public class Pigeon {
    private static final Gson GSON = new Gson();
    private final String apiKey;
    private final String baseUrl;
    private final HttpClient http;
    private final Emails emails = new Emails();
    private final Domains domains = new Domains();

    public Pigeon(String apiKey) {
        this(apiKey, envOr("PIGEON_BASE_URL", "http://localhost:4005"));
    }

    public Pigeon(String apiKey, String baseUrl) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("Pigeon API key is required");
        }
        this.apiKey = apiKey;
        this.baseUrl = trimSlash(baseUrl);
        this.http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(20)).build();
    }

    public Emails emails() {
        return emails;
    }

    public Domains domains() {
        return domains;
    }

    public class Emails {
        public CreateEmailResponse send(CreateEmailOptions params) {
            JsonObject body = request("POST", "/api/emails", params);
            return GSON.fromJson(body, CreateEmailResponse.class);
        }
    }

    public class Domains {
        public JsonObject list() {
            return request("GET", "/api/domains", null);
        }

        public JsonObject create(String name) {
            return request("POST", "/api/domains", Map.of("name", name));
        }
    }

    JsonObject request(String method, String path, Object body) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + path))
                    .timeout(Duration.ofSeconds(30))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Accept", "application/json")
                    .header("User-Agent", "pigeon-java/0.1.0");

            if (body == null) {
                builder.method(method, HttpRequest.BodyPublishers.noBody());
            } else {
                builder.header("Content-Type", "application/json");
                builder.method(method, HttpRequest.BodyPublishers.ofString(GSON.toJson(body)));
            }

            HttpResponse<String> response = http.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            String raw = response.body() == null || response.body().isBlank() ? "{}" : response.body();
            JsonObject json = GSON.fromJson(raw, JsonObject.class);
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                String message = json.has("message") ? json.get("message").getAsString() : response.body();
                throw new PigeonException(message, response.statusCode(), json);
            }
            return json;
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PigeonException(e.getMessage(), 0, null);
        }
    }

    private static String envOr(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? fallback : value;
    }

    private static String trimSlash(String value) {
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }
}
