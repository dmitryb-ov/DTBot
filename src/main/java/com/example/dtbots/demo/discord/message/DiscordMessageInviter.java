package com.example.dtbots.demo.discord.message;

import com.example.dtbots.demo.annotations.Discord;
import com.example.dtbots.demo.models.ChannelContainer;
import com.example.dtbots.demo.models.Message;
import com.example.dtbots.demo.models.TypeFrom;
import com.example.dtbots.demo.models.User;
import com.example.dtbots.demo.service.MessageService;
import com.example.dtbots.demo.service.UserService;
import com.example.dtbots.demo.service.WordLogic;
import com.example.dtbots.demo.transaction.TransactionManager;
import com.example.dtbots.demo.wordapi.YandexDict;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Discord
public class DiscordMessageInviter extends ListenerAdapter {
    private static final String MAIN_CMD = "!";
    private static final String CMD_W = MAIN_CMD + "w";
    private static final String CMD_START = MAIN_CMD + "start";

    private List<ChannelContainer> channelContainers = new ArrayList<>();
    private String lastLetter;
    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private WordLogic wordLogic;

    @Autowired
    private TransactionManager transactionManager;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getRawContent();
        if (message.startsWith(MAIN_CMD)) {
            if (message.equals(CMD_START)) {
                if (!userService.getUserBySystemId(event.getAuthor().getIdLong()).isPresent()) {
                    User user = User.builder()
                            .systemId(event.getAuthor().getIdLong())
                            .from(TypeFrom.DISCORD)
                            .build();
                    userService.addUser(user);
                }
                lastLetter = wordLogic.getRandomLetter();
                event.getChannel().sendMessage("Начните вводить любое слово на букву \"" + lastLetter + "\"").queue();
            }
            if (message.startsWith(CMD_W)) {
                if (userService.getUserBySystemId(event.getAuthor().getIdLong()).isPresent()) {
                    String[] arrMessage = message.split(" ");
                    String[] arrLetter = arrMessage[1].split("");
                    if (arrLetter[0].equalsIgnoreCase(lastLetter)) {
                        YandexDict yandexDict = new YandexDict();
                        if (yandexDict.checkWord(arrMessage[1])) {
                            Message dbMessage = Message.builder()
                                    .userId(event.getAuthor().getIdLong())
                                    .message(arrMessage[1])
                                    .build();
                            messageService.addMessage(dbMessage);
                            boolean flag = true;
                            if (!channelContainers.isEmpty()) {
                                for (ChannelContainer channelContainer : channelContainers) {
                                    if (channelContainer.getEvent().getGuild().getId()
                                            .equals(event.getGuild().getId())) {
                                        flag = false;
                                        break;
                                    }
                                }
                                if (flag) {
                                    channelContainers.add(new ChannelContainer(event));
                                }
                            } else {
                                channelContainers.add(new ChannelContainer(event));
                            }
                            event.getChannel().sendMessage("Сообщение " + arrMessage[1] + " принято!").queue();
                            sendMessages(arrMessage[1]);
                        } else {
                            event.getChannel().sendMessage("Нет слова " + arrMessage[1]).queue();
                        }
                    } else {
                        event.getChannel().sendMessage("Не та буква").queue();
                    }
                } else {
                    event.getChannel().sendMessage("Введите " + CMD_START).queue();
                }
            }
        }
    }

    private void sendMessages(String word) {
        lastLetter = wordLogic.getLastLetter(word);
        transactionManager.doTransaction(DiscordMessageInviter.class, word);
        for (ChannelContainer channelContainer : channelContainers) {
            channelContainer.getEvent().getChannel().sendMessage("Слово на букву \"" + lastLetter + "\"").queue();
        }
    }

    public void sendAllMessages(String word){
        lastLetter = wordLogic.getLastLetter(word);
        for (ChannelContainer channelContainer : channelContainers) {
            channelContainer.getEvent().getChannel().sendMessage("Слово на букву \"" + lastLetter + "\"").queue();
        }
    }
}
