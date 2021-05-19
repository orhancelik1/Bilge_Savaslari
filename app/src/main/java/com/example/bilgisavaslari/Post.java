package com.example.bilgisavaslari;

import com.google.gson.annotations.SerializedName;

public class Post {

    private String a;
    private String b;
    private String c;
    private String d;
    private String e;
    private int answer;

    @SerializedName("context")
    private String text;

    public String getA() {
        return a;
    }

    public String getB() {
        return b;
    }

    public String getC() {
        return c;
    }

    public String getD() {
        return d;
    }

    public String getE() {
        return e;
    }

    public int getAnswer(){ return answer;}

    public String getText() {
        return text;
    }
}
