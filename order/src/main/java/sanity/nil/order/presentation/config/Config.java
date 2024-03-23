package sanity.nil.order.presentation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.support.JacksonUtils;
import sanity.nil.order.application.OrderCommands;
import sanity.nil.order.application.OrderQueries;
import sanity.nil.order.application.command.CreateOrderCommand;
import sanity.nil.order.application.command.RejectOrderCommand;
import sanity.nil.order.application.command.StartCreateOrderCommand;
import sanity.nil.order.application.interfaces.OrderRepository;
import sanity.nil.order.application.interfaces.OutboxRepository;
import sanity.nil.order.application.query.GetOrderByTraceID;
import sanity.nil.order.infrastructure.db.impl.OrderRepositoryImpl;
import sanity.nil.order.infrastructure.db.impl.OutboxRepositoryImpl;
import sanity.nil.order.infrastructure.db.impl.SagaRepositoryImpl;
import sanity.nil.order.infrastructure.saga.SagaRepository;
import sanity.nil.order.infrastructure.saga.impl.OrderCreationSaga;

@Configuration
public class Config {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JacksonUtils.enhancedObjectMapper();
    }

    @Bean
    public OrderRepository orderRepository(EntityManager entityManager) {
        return new OrderRepositoryImpl(entityManager);
    }

    @Bean
    public OutboxRepository outboxRepository(EntityManager entityManager, ObjectMapper objectMapper) {
        return new OutboxRepositoryImpl(entityManager, objectMapper);
    }

    @Bean
    public SagaRepository sagaRepository(EntityManager entityManager) {
        return new SagaRepositoryImpl(entityManager);
    }

    @Bean
    public OrderCommands orderCommands(OutboxRepository outboxRepository, OrderRepository orderRepository) {
       return new OrderCommands(
               new StartCreateOrderCommand(outboxRepository, orderRepository),
               new RejectOrderCommand(orderRepository),
               new CreateOrderCommand(orderRepository)
       );
    }

    @Bean
    public OrderQueries orderQueries(OrderRepository orderRepository) {
        return new OrderQueries(
                new GetOrderByTraceID(orderRepository)
        );
    }

    @Bean
    public OrderCreationSaga orderCreationSaga(OrderCommands orderCommands, OutboxRepository outboxRepository,
                                               SagaRepository sagaRepository, ObjectMapper objectMapper) {
        return new OrderCreationSaga(orderCommands, sagaRepository, outboxRepository, objectMapper);
    }

}
