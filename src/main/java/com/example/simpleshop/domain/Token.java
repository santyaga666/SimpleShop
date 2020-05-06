package com.example.simpleshop.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String access_token;
    private String token_type;
    private String refresh_token;
    private String user_name;
    private String user_id;
    private String client_id;
    private String issued;
    private String expires;
    //private String expires_in; Убрал, так как у нас есть поле expires

    public Token() {
    }

    public Token(String access_token, String token_type, String refresh_token, String user_name, String user_id, String client_id, String issued, String expires) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.refresh_token = refresh_token;
        this.user_name = user_name;
        this.user_id = user_id;
        this.client_id = client_id;
        this.issued = issued;
        this.expires = expires;
    }
}
