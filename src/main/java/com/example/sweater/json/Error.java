package com.example.sweater.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Error {
    private String errorCode;
    private String title;
    private String description;
    private String errorMessage;
    private FieldErrors fieldErrors;
}
