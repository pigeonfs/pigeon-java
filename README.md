# Pigeon Java SDK

Official Java client for the [Pigeon](https://github.com/pigeonfs/pigeon) email API. API shape follows [resend-java](https://github.com/resend/resend-java): `new Pigeon(apiKey).emails().send(params)`.

## Installation

Clone the repo and publish to Maven local, or depend on the GitHub source:

```gradle
implementation 'io.github.pigeonfs:pigeon-java:0.1.0'
```

```xml
<dependency>
    <groupId>io.github.pigeonfs</groupId>
    <artifactId>pigeon-java</artifactId>
    <version>0.1.0</version>
</dependency>
```

Until it is on Maven Central, build from source:

```bash
git clone https://github.com/pigeonfs/pigeon-java.git
cd pigeon-java
./gradlew publishToMavenLocal
```

## Example

```java
import io.github.pigeonfs.Pigeon;
import io.github.pigeonfs.emails.CreateEmailOptions;
import io.github.pigeonfs.emails.CreateEmailResponse;

public class Main {
    public static void main(String[] args) {
        Pigeon pigeon = new Pigeon("pg_xxxx");

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Ada <ada@yourdomain.com>")
                .to("person@example.com")
                .subject("Hello from Java!")
                .html("<strong>it works!</strong>")
                .build();

        CreateEmailResponse data = pigeon.emails().send(params);
        System.out.println(data.getId());
    }
}
```

Set `PIGEON_BASE_URL` when the API is not `http://localhost:4005`.

## License

MIT
