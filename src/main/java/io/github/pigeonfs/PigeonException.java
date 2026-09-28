package io.github.pigeonfs;

import com.google.gson.JsonObject;

public class PigeonException extends RuntimeException {
    private final int statusCode;
    private final JsonObject body;

    public PigeonException(String message, int statusCode, JsonObject body) {
        super(message);
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public JsonObject getBody() {
        return body;
    }
}
