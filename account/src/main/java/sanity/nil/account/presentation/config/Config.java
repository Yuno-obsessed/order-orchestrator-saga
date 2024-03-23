package sanity.nil.account.presentation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.JacksonUtils;
import sanity.nil.account.application.AccountCommands;
import sanity.nil.account.application.command.VerifyBalanceCommand;
import sanity.nil.account.application.interfaces.AccountRepository;
import sanity.nil.account.application.interfaces.OutboxRepository;
import sanity.nil.account.infrastructure.db.impl.AccountRepositoryImpl;
import sanity.nil.account.infrastructure.db.impl.OutboxRepositoryImpl;

@Configuration
public class Config {

    @Bean
    public ObjectMapper objectMapper() {
        return JacksonUtils.enhancedObjectMapper();
    }

    @Bean
    public OutboxRepository outboxRepository(EntityManager entityManager, ObjectMapper objectMapper) {
        return new OutboxRepositoryImpl(entityManager, objectMapper);
    }

    @Bean
    public AccountRepository accountRepository(EntityManager entityManager) {
        return new AccountRepositoryImpl(entityManager);
    }

    @Bean
    public AccountCommands accountCommands(OutboxRepository outboxRepository,
                                                     AccountRepository accountRepository, ObjectMapper objectMapper) {
        return new AccountCommands(
                new VerifyBalanceCommand(outboxRepository, accountRepository, objectMapper)
        );
    }
}
