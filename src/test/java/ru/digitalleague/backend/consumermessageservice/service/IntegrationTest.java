package ru.digitalleague.backend.consumermessageservice.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@DirtiesContext
@Testcontainers
@ContextConfiguration(initializers = IntegrationTest.TestcontainersInitializers.class)
public class IntegrationTest {

    @Container
    protected static PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:12.9")
                    .withDatabaseName("postgres");


    @Container
    protected static KafkaContainer kafka =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.0.0")).withReuse(true).withEmbeddedZookeeper();

    public static class TestcontainersInitializers implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @BeforeAll
        public static void before() {
            Startables.deepStart(Stream.of(kafka, postgres));
        }

        @AfterAll
        public static void after() {
            kafka.stop();
            postgres.stop();
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.datasource.url=" + postgres.getJdbcUrl(),
                    "spring.datasource.username=" + postgres.getUsername(),
                    "spring.datasource.password=" + postgres.getPassword(),
                    "spring.kafka.bootstrap-servers=" + kafka.getBootstrapServers()
            );
        }
    }

    @Test
    void isCreated() {
        assertEquals(true, postgres.isCreated());
        assertEquals(true, kafka.isCreated());
    }

    @Test
    void isRaning() {
        assertEquals(true, postgres.isRunning());
        assertEquals(true, kafka.isRunning());
    }


}
