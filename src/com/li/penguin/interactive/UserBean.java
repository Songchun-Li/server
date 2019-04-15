package com.li.penguin.interactive;

import com.google.gson.annotations.SerializedName;


public class UserBean {
    @SerializedName("score")
    private int score;
    @SerializedName("name")
    private String name;
    @SerializedName("date")
    private String date;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}