package com.example.dtbots.demo.config;

import com.example.dtbots.demo.discord.message.DiscordMessageInviter;
import com.example.dtbots.demo.service.UserService;
import com.example.dtbots.demo.service.UserServiceImpl;
import com.example.dtbots.demo.transaction.TransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class AppConfig {
    @Bean(name = "discord")
    public DiscordMessageInviter discord(){
        return new DiscordMessageInviter();
    }

    @Bean(name = "myTransactionManager")
    public TransactionManager myTransactionManager(){
        return new TransactionManager();
    }
}
