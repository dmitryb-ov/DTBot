package com.example.dtbots.demo.transaction;

import com.example.dtbots.demo.discord.message.DiscordMessageInviter;
import com.example.dtbots.demo.telegram.message.TelegramMessageInviter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;

public class TransactionManager {
    private static final String DISCORD_VAL = "discord";
    private static final String TELEGRAM_VAL = "telegram";

    @Autowired
    private ApplicationContext applicationContext;

    public void doTransaction(Class<?> fromClass, String word){
        String annotationName = "";
        for (Annotation annotation : fromClass.getAnnotations()) {
            Class<? extends Annotation> type = annotation.annotationType();
            annotationName = type.getSimpleName();
            if (annotationName.equalsIgnoreCase(TELEGRAM_VAL)
                    || annotationName.equalsIgnoreCase(DISCORD_VAL)) {
                break;
            }
        }
        if (annotationName.equalsIgnoreCase(TELEGRAM_VAL)) {
            applicationContext.getBean(DiscordMessageInviter.class).sendAllMessages(word);

        }
        if (annotationName.equalsIgnoreCase(DISCORD_VAL)) {
            applicationContext.getBean(TelegramMessageInviter.class).sendAllTgMessage(word);
        }
    }
}
