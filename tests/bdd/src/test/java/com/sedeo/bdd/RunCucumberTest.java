package com.sedeo.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.PullPolicy;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RunWith(Cucumber.class)
@CucumberOptions(glue = "com.sedeo.bdd.glue", features = "src/test/resources/bdd", plugin = {"pretty",
        "html:target/report/cucumber.html",
        "json:target/report/cucumber.json"
})
public class RunCucumberTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RunCucumberTest.class);
    private static final String POSTGRES_MAGIC_STRING = "sedeo-bdd";
    private static final Network network = Network.newNetwork();
    private static final Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(LOGGER);
    public static GenericContainer<?> sedeoContainer;

    @BeforeClass
    public static void setup() throws IOException {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        String databaseAlias = "db";
        String sedeoAlias = "sedeo";

        PostgreSQLContainer<?> postgreSqlContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgis/postgis").asCompatibleSubstituteFor("postgres"))
                .withPassword(POSTGRES_MAGIC_STRING)
                .withUsername(POSTGRES_MAGIC_STRING)
                .withDatabaseName(POSTGRES_MAGIC_STRING)
                .withNetwork(network)
                .withNetworkAliases(databaseAlias)
                .waitingFor(new LogMessageWaitStrategy().withRegEx(".*database system is ready to accept connections.*\\s")
                        .withTimes(2)
                        .withStartupTimeout(Duration.of(60L, ChronoUnit.SECONDS)));
        postgreSqlContainer.start();

        sedeoContainer = new GenericContainer<>(Files.readString(Path.of(System.getenv("DOCKER_IMAGE_FILE"))).trim())
                .withImagePullPolicy(PullPolicy.defaultPolicy())
                .withNetwork(network)
                .withExposedPorts(8080, 5005)
                .withNetworkAliases(sedeoAlias)
                .withEnv("SPRING_PROFILES_ACTIVE", "prod")
                .withEnv("MAIL_SENDER_ADDRESS", System.getenv("MAIL_SENDER_ADDRESS"))
                .withEnv("FRONTEND_BASE_URL", System.getenv("FRONTEND_BASE_URL"))
                .withEnv("AZURE_CONNECTION_STRING", System.getenv("AZURE_CONNECTION_STRING"))
                .withEnv("JWT_SECRET_KEY", "62876091e82d9c3894a464ba4abf06bb1d1ee163affc6c2c9bad487ee65a192b031eb162f1")
                .withEnv("SPRING_DATASOURCE_PASSWORD", POSTGRES_MAGIC_STRING)
                .withEnv("SPRING_DATASOURCE_URL", "jdbc:postgresql://" + databaseAlias + ":5432/" + POSTGRES_MAGIC_STRING)
                .withEnv("SPRING_DATASOURCE_USERNAME", POSTGRES_MAGIC_STRING)
                .withLogConsumer(logConsumer)
                .waitingFor(Wait.forHttp("/actuator/health").forStatusCode(200).withReadTimeout(Duration.ofSeconds(15)));
        sedeoContainer.setPortBindings(List.of("8080:8080", "5005:5005"));
        sedeoContainer.start();
    }

    public static String extractLogs(GenericContainer container) {
        return container.getLogs();
    }
}