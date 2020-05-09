package com.example.simpleshop.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestProxy {
    private String CallerIp;

    public TestProxy() {
    }

    public TestProxy(String callerIp) {
        CallerIp = callerIp;
    }

    public String getCallerIp() {
        return CallerIp;
    }

    public void setCallerIp(String callerIp) {
        CallerIp = callerIp;
    }
}
