package com.isep.bootstrapper.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RabbitmqConfig {

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
        Jackson2JsonMessageConverter jackson2JsonMessageConverter) {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
        return rabbitTemplate;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationListener(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    // Bootstrapper
    // Review
    @Bean
    public FanoutExchange rpcReviewExchange(){
        return new FanoutExchange("rpc.review.vote-query-bootstrapper");
    }

    @Bean
    public Queue rpcReviewQueue(){
        return new Queue("rpc.review.vote-query-bootstrapper");
    }

    @Bean
    public Binding bindRpcReview(FanoutExchange rpcReviewExchange, Queue rpcReviewQueue){
        return BindingBuilder.bind(rpcReviewQueue).to(rpcReviewExchange);
    }

    // Vote
    @Bean
    public FanoutExchange rpcVoteExchange(){
        return new FanoutExchange("rpc.vote.vote-query-bootstrapper");
    }

    @Bean
    public Queue rpcVoteQueue(){
        return new Queue("rpc.vote.vote-query-bootstrapper");
    }

    @Bean
    public Binding bindRpcVote(FanoutExchange rpcVoteExchange, Queue rpcVoteQueue){
        return BindingBuilder.bind(rpcVoteQueue).to(rpcVoteExchange);
    }

    // Temporary Vote
    @Bean
    public FanoutExchange rpcTemporaryVoteExchange(){
        return new FanoutExchange("rpc.temporary-vote.vote-query-bootstrapper");
    }

    @Bean
    public Queue rpcTemporaryVoteQueue(){
        return new Queue("rpc.temporary-vote.vote-query-bootstrapper");
    }

    @Bean
    public Binding bindRpcTemporaryVote(FanoutExchange rpcTemporaryVoteExchange, Queue rpcTemporaryVoteQueue){
        return BindingBuilder.bind(rpcTemporaryVoteQueue).to(rpcTemporaryVoteExchange);
    }
}