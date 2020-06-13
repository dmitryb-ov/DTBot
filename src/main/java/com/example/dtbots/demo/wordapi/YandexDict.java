package com.example.dtbots.demo.wordapi;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class YandexDict {
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    public boolean checkWord(String word) {
        String API_KEY = getApiKey();
        final String REQUEST_URL = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=" + API_KEY +
                "&lang=ru-ru&text=" + word;
        HttpGet request = new HttpGet(REQUEST_URL);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity httpEntity = response.getEntity();
            String result = EntityUtils.toString(httpEntity);
            System.out.println(result);
//            System.err.println(getWordFromJson(result));
            if(!getWordFromJson(result).isEmpty()){
                return true;
            } else {
                return false;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getApiKey() {
        FileInputStream fileInputStream;
        Properties properties = new Properties();
        try {
            fileInputStream = new FileInputStream("src/main/resources/yandex.properties");
            properties.load(fileInputStream);
            return properties.getProperty("yandexDictToken");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "File not found";
        } catch (IOException e) {
            e.printStackTrace();
            return "IO Exception";
        }
    }

    private static final String DEF_KEY = "def";
    private static final String TEXT_KEY = "text";

    private String getWordFromJson(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonObject1 = jsonObject.getJSONArray(DEF_KEY);
            JSONObject jsonObject2 = jsonObject1.getJSONObject(0);
            return jsonObject2.getString(TEXT_KEY);
        } catch (JSONException e){
            return "";
        }
    }
}
