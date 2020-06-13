package com.example.dtbots.demo.service;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class WordLogicImpl implements WordLogic {
    private char[] alphabet = "абвгдеёжзийклмнопрстуфхцчшщэюя".toCharArray();

    @Override
    public String getLastLetter(String word) {
        String[] wordArr = word.split("");
        if (wordArr[wordArr.length - 1].equals("ь")
                || wordArr[wordArr.length - 1].equals("ы")
                || wordArr[wordArr.length - 1].equals("ъ")) {
            return wordArr[wordArr.length - 2];
        }
        return wordArr[wordArr.length - 1];
    }

    @Override
    public String getRandomLetter() {
        Random random = new Random();
        int ranLet = random.nextInt(alphabet.length);
        return String.valueOf(alphabet[ranLet]);
    }
}
