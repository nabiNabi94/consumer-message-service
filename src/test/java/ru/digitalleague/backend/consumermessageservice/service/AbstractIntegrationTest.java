package ru.digitalleague.backend.consumermessageservice.service;

import org.jetbrains.annotations.NotNull;
import org.junit.ClassRule;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;
import ru.digitalleague.backend.consumermessageservice.ConsumerMessageServiceApplication;

import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.datasource.url=jdbc:tc:postgresql:12.9:///postgres?TC_DEMON=true&TC_REUSABLE=true",
        "spring.datasource.Driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver"
})
@ContextConfiguration(
        initializers = AbstractIntegrationTest.Initializer.class
)
public abstract class AbstractIntegrationTest {


    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @ClassRule
        static KafkaContainer kafkaContainer =
                new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.0.0"))
                        .withReuse(true);


        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            Startables.deepStart(Stream.of(kafkaContainer)).join();

            TestPropertyValues.of("spring.kafka.bootstrap-servers=" + kafkaContainer.getBootstrapServers(),
                            "auto-offset-reset=" + "earliest"
                            )
                    .applyTo(applicationContext);
        }

    }

}

