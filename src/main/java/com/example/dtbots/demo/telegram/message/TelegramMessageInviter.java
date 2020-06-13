package com.example.dtbots.demo.telegram.message;

import com.example.dtbots.demo.annotations.Telegram;
import com.example.dtbots.demo.models.Message;
import com.example.dtbots.demo.models.TypeFrom;
import com.example.dtbots.demo.models.User;
import com.example.dtbots.demo.service.MessageService;
import com.example.dtbots.demo.service.UserService;
import com.example.dtbots.demo.service.WordLogic;
import com.example.dtbots.demo.transaction.TransactionManager;
import com.example.dtbots.demo.wordapi.YandexDict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//@Component
@Telegram
public class TelegramMessageInviter extends TelegramLongPollingBot {
    private static final String BOT_NAME = "WordBot";
    private static final String MAIN_CMD = "/";
    private static final String WORD_CMD = MAIN_CMD + "w";
    private static final String START_CMD = MAIN_CMD + "start";

    private List<Update> updateList = new ArrayList<>();
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
    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        if (message.startsWith(MAIN_CMD)) {
            if (message.equals(START_CMD)) {
                if (!userService.getUserBySystemId((long) update.getMessage().getFrom().getId()).isPresent()) {
                    User user = User.builder()
                            .systemId((long) update.getMessage().getFrom().getId())
                            .from(TypeFrom.TELEGRAM)
                            .build();
                    userService.addUser(user);
                    sendMessage(chatId, "Привет, " + update.getMessage().getFrom().getUserName());
                }
                lastLetter = wordLogic.getRandomLetter();
                sendMessage(chatId, "Начните вводить любое слово на букву \"" + lastLetter + "\"");
            }
            if (message.startsWith(WORD_CMD)) {
                if (userService.getUserBySystemId((long) update.getMessage().getFrom().getId()).isPresent()) {
                    String[] arrMessage = message.split(" ");
                    String[] arrLetter = arrMessage[1].split("");
                    if (arrLetter[0].equalsIgnoreCase(lastLetter)) {
                        YandexDict yandexDict = new YandexDict();
                        if (yandexDict.checkWord(arrMessage[1])) {
                            Message dbMessage = Message.builder()
                                    .userId((long) update.getMessage().getFrom().getId())
                                    .message(arrMessage[1])
                                    .build();
                            messageService.addMessage(dbMessage);
                            boolean flag = true;
                            if (!updateList.isEmpty()) {
                                for (Update value : updateList) {
                                    if (value.getMessage().getChatId().longValue() == chatId.longValue()) {
                                        flag = false;
                                        break;
                                    }
                                }
                                if (flag) {
                                    updateList.add(update);
                                }
                            } else {
                                updateList.add(update);
                            }
                            sendMessage(chatId, "Сообщение " + arrMessage[1] + " принято");
                            sendAllMessage(arrMessage[1]);
                        } else {
                            sendMessage(chatId, "Нет слова " + arrMessage[1]);
                        }
                    } else {
                        sendMessage(chatId, "Ваша буква не подходит");
                    }
                } else {
                    sendMessage(chatId, "Для начала введите команду /start");
                }
            }
        }
    }

    public synchronized void sendAllTgMessage(String word){
        lastLetter = wordLogic.getLastLetter(word);
        for (Update update : updateList) {
            sendMessage(update.getMessage().getChatId(), "Слово на букву \"" + lastLetter + "\"");
        }
    }
    private synchronized void sendAllMessage(String word) {
        lastLetter = wordLogic.getLastLetter(word);
        transactionManager.doTransaction(TelegramMessageInviter.class, word);
        for (Update update : updateList) {
            sendMessage(update.getMessage().getChatId(), "Слово на букву \"" + lastLetter + "\"");
        }
    }

    private synchronized void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Exception " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return getPropBotToken();
    }

    private String getPropBotToken() {
        FileInputStream fileInputStream;
        Properties properties = new Properties();
        try {
            fileInputStream = new FileInputStream("src/main/resources/telegram.properties");
            properties.load(fileInputStream);
            return properties.getProperty("telegramToken");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "File not found";
        } catch (IOException e) {
            e.printStackTrace();
            return "IO Exception";
        }
    }
}
