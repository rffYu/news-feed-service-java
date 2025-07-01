
# A thing

## Run listener-service

run command in root level:

```bash
./mvnw install && ./mvnw spring-boot:run -pl listener-service
```

## Setup dev environemt

install sdkman in zsh (zsh work well with sdkman).

```bash
curl -s "https://get.sdkman.io" | bash
```

install java stuff

```bash
sdk install java 17.0.15-tem
sdk install maven
```

create mvnw wrapper

```bash
mvn -N io.takari:maven:wrapper
```

