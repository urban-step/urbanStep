package com.spa.urbanstep.network;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shweta on 1/6/18.
 */

public class ResponseBean<T> {
    @SerializedName("status_code")
    @Expose
    private int status;
    @SerializedName("success_message")
    @Expose
    private String message;
    @SerializedName("data")
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
