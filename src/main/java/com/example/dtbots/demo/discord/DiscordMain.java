package com.example.dtbots.demo.discord;

import com.example.dtbots.demo.discord.message.DiscordMessageInviter;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

@Component
public class DiscordMain implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

//    public static void main(String[] args) {
//    }

    @Override
    public void run(String... args) throws Exception {
        FileInputStream fileInputStream;
        Properties properties = new Properties();
        try {
            fileInputStream = new FileInputStream("src/main/resources/discord.properties");
            properties.load(fileInputStream);
            String token = properties.getProperty("discordToken");
            JDABuilder builder = new JDABuilder(AccountType.BOT);
            builder.setToken(token);
            DiscordMessageInviter discordMessageInviter = applicationContext.getBean(DiscordMessageInviter.class);
            builder.addEventListener(discordMessageInviter);
            builder.buildAsync();


        } catch (LoginException e) {
            System.err.println("LoginException");
            e.printStackTrace();
        } catch (RateLimitedException e) {
            System.err.println("Rate Limit Exception");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
