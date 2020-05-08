package com.example.simpleshop.json;

public class WalletResponse {
    private Wallet wallets;
    private Error error;

    public  WalletResponse() {}

    public WalletResponse(Wallet wallets, Error error) {
        this.wallets = wallets;
        this.error = error;
    }

    public Wallet getWallets() {
        return wallets;
    }

    public void setWallets(Wallet wallets) {
        this.wallets = wallets;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
