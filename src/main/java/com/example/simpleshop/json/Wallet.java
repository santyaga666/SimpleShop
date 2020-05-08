package com.example.simpleshop.json;

public class Wallet {
    private Integer id;
    private Integer instrumentId;
    private String name;
    private String number;
    private String walletType;
    private Double balance;
    private Double pendingBalance;

    public  Wallet() {}

    public Wallet(Integer id, Integer instrumentId, String name, String number, String walletType, Double balance, Double pendingBalance) {
        this.id = id;
        this.instrumentId = instrumentId;
        this.name = name;
        this.number = number;
        this.walletType = walletType;
        this.balance = balance;
        this.pendingBalance = pendingBalance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(Integer instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getWalletType() {
        return walletType;
    }

    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getPendingBalance() {
        return pendingBalance;
    }

    public void setPendingBalance(Double pendingBalance) {
        this.pendingBalance = pendingBalance;
    }
}
