package com.example.home_zhang.cpms.services;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class QuestionService {

    String baseURL = "https://cpms-java.herokuapp.com";

    public QuestionService(){

    }

    public String getAuthToken(){
        String respnose = "";
        try {

            URL url = new URL(baseURL + "/api/auth/signin");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String input = "{\"password\": \"jwtpass\",\"usernameOrEmail\":\"admin@admin\"}";

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                respnose = output;
                JSONParser parser = new JSONParser();
                JSONObject responsJSON = (JSONObject) parser.parse(output);
                String accessToken = (String) responsJSON.get("accessToken");
                String tokenType = (String) responsJSON.get("tokenType");
                System.out.println(accessToken);
                System.out.println(tokenType);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return respnose;
    }
}
