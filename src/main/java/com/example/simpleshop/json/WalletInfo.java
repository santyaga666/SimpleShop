package com.example.simpleshop.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WalletInfo {
    private String number;
    private String id;
    private String instrumentId;
    private String activateCodeType;

    public WalletInfo() {}

    public WalletInfo(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

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
}
