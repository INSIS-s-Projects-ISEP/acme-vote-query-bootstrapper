package com.isep.bootstrapper.messaging;

import java.io.IOException;
import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.isep.bootstrapper.dto.mapper.VoteMapper;
import com.isep.bootstrapper.dto.message.VoteMessage;
import com.isep.bootstrapper.model.Vote;
import com.isep.bootstrapper.repository.VoteRepository;
import com.rabbitmq.client.Channel;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class VoteConsumer {

    private final VoteMapper voteMapper;
    private final VoteRepository voteRepository;

    @RabbitListener(queues = "#{rpcVoteQueue.name}", ackMode = "MANUAL")
    public String rpcVote(String instanceId, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException{

        log.info("RPC Vote Request received: " + instanceId);
        try {
            List<Vote> votes = voteRepository.findAll();
            List<VoteMessage> messages = voteMapper.toMessageList(votes);
            String response = voteMapper.toJson(messages);

            log.info("RPC Vote Request sent to: " + instanceId);
            channel.basicAck(tag, false);
            return response;
        }
        catch (Exception e) {
            log.error("Error to send RPC Vote Request to: " + instanceId);
            channel.basicReject(tag, true);
            return "";
        }

    }

}
