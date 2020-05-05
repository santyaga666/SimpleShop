package com.example.sweater.json;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KunaTime {
    private Long timestamp;
    private Long timestamp_miliseconds;

    public Long getTimestamp() {
        return timestamp;
    }

    public Long getTimestamp_miliseconds() {
        return timestamp_miliseconds;
    }
}
