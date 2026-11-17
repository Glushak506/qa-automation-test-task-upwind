# Maven + JDK 17: everything needed to build and run the tests
FROM maven:3.9-eclipse-temurin-17

# Chrome browser for the ui tests
RUN apt-get update \
    && curl -fsSL -o /tmp/chrome.deb https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb \
    && apt-get install -y --no-install-recommends /tmp/chrome.deb \
    && rm /tmp/chrome.deb \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# dependences first: this layer is cached and re-used while only the tests change
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src

# headless Chrome; --no-sandbox is required because chrome already runs as root inside the container
CMD ["mvn", "-B", "test", "-Dselenide.headless=true", "-Dchromeoptions.args=--no-sandbox"]
