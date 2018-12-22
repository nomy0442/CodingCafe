package com.example.josee.codingcafe.client;

import com.google.gson.annotations.SerializedName;

public class ResponseBody {
    @SerializedName("response")
    private String response;

    public ResponseBody()
    {
        this.response = "";

    }
    public ResponseBody(String message)
    {
        this.response = message;

    }
    public String getResponse()
    {
        return this.response;

    }
}
