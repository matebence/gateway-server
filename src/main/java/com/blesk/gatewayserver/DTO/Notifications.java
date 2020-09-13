package com.blesk.gatewayserver.DTO;

import java.util.Map;

public class Notifications {

    private String title;
    private String body;
    private String token;
    private Map<String, String> data;
    private String imageUrl = "https://firebasestorage.googleapis.com/v0/b/blesk-microservice-app.appspot.com/o/blesk-default-notifcation-icon.jpg?alt=media&token=c9dd721b-8cac-4d8b-bb00-4f419bdfbe51";

    public Notifications(String title, String body, String token) {
        this.title = title;
        this.body = body;
        this.token = token;
    }

    public Notifications() {
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public String addData(String key, String value){
        return this.data.put(key,value);
    }

    public String removeData(String key){
        return this.data.remove(key);
    }

    public String getData(String key){
        return this.data.get(key);
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}