package com.example.dtbots.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@Data
@AllArgsConstructor
public class ChannelContainer {
    private MessageReceivedEvent event;
//    private String channelId;
}
