package com.example.sweater.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldErrors {
    private String fieldName;
    private String errorCode;
    private String errorMessage;
}
