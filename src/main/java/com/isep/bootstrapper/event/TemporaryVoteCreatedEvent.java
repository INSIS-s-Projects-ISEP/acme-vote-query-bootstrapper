package com.isep.bootstrapper.event;

import java.util.UUID;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.isep.bootstrapper.enumarate.VoteType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TemporaryVoteCreatedEvent {

    @TargetAggregateIdentifier
    private UUID temporaryVoteId;
    private VoteType voteType;
    private String user;

}
