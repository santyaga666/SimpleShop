package com.example.simpleshop.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WalletInfo {
    private String id;
    private String instrumentId;
    private String activateCodeType;
    private String error;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getActivateCodeType() {
        return activateCodeType;
    }

    public void setActivateCodeType(String activateCodeType) {
        this.activateCodeType = activateCodeType;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
