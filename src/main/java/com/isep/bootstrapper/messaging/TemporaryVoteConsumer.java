package com.isep.bootstrapper.messaging;

import java.io.IOException;
import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.isep.bootstrapper.dto.mapper.TemporaryVoteMapper;
import com.isep.bootstrapper.dto.message.TemporaryVoteMessage;
import com.isep.bootstrapper.model.TemporaryVote;
import com.isep.bootstrapper.repository.TemporaryVoteRepository;
import com.rabbitmq.client.Channel;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class TemporaryVoteConsumer {

    private final TemporaryVoteRepository temporaryVoteRepository;
    private final TemporaryVoteMapper temporaryVoteMapper;
    
    @RabbitListener(queues = "#{rpcTemporaryVoteQueue.name}", ackMode = "MANUAL")
    public String rpcTemporaryVote(String instanceId, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException{

        log.info("RPC Temporary Vote Request received: " + instanceId);
        try {
            List<TemporaryVote> temporaryVotes = temporaryVoteRepository.findAll();
            List<TemporaryVoteMessage> messages = temporaryVoteMapper.toMessageList(temporaryVotes);
            String response = temporaryVoteMapper.toJson(messages);

            log.info("RPC Temporary Vote Request sent to: " + instanceId);
            channel.basicAck(tag, false);
            return response;
        }
        catch (Exception e) {
            log.error("Error to send RPC Temporary Vote Request to: " + instanceId);
            channel.basicReject(tag, true);
            return "";
        }

    }

}
