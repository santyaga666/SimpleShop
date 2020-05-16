package com.example.simpleshop.controller;

import com.example.simpleshop.domain.Token;
import com.example.simpleshop.domain.User;
import com.example.simpleshop.json.App;
import com.example.simpleshop.json.MyAuthenticator;
import com.example.simpleshop.json.WalletInfo;
import com.example.simpleshop.repos.PointRepo;
import com.example.simpleshop.repos.TokenRepo;
import com.example.simpleshop.repos.UserRepo;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.net.Authenticator;
import java.util.Map;

@Controller
public class EpController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PointRepo pointRepo;
    @Autowired
    private TokenRepo tokenRepo;

    @PostMapping("/easypay/addWallet")
    public String addWallet(@AuthenticationPrincipal UserDetails userDetails, Map<String, Object> model) {
        User user = userRepo.findByUsername(userDetails.getUsername());
        Token token = tokenRepo.findByOwnerId(user.getId().longValue());
        //model.put("point", point);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("name", user.getUsername());
        body.add("color", "string");

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "bearer " + token.getAccess_token());
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.add("PartnerKey", "easypay-v2");
        headers.add("locale", "UA");
        headers.add("koatuu", "8000000000");
        headers.add("AppId", token.getAppId());
        headers.add("PageId", token.getPageId());

        Gson gson = new Gson();
        String jsonHeaders = gson.toJson(headers);
        String jsonBody = gson.toJson(body, LinkedMultiValueMap.class);
        jsonBody = "{\n" +
                "  \"name\": \"string\",\n" +
                "  \"color\": \"string\"\n" +
                "}";


        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.easypay.ua/";
        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonBody, headers);
        WalletInfo walletInfo = restTemplate.postForObject(url + "api/wallets/add", httpEntity, WalletInfo.class);
        httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> entity = restTemplate.exchange(url + "api/wallets/get/" + walletInfo.getId(), HttpMethod.GET, httpEntity, String.class);

        String s0 = entity.getBody();
        int q = s0.indexOf("number");
        String s = s0.substring(q + 9, q + 17);

        user.setWalletId(walletInfo.getId());
        user.setWalletNumber(s);
        userRepo.save(user);

        return "redirect:/order";
    }

    @PostMapping("/easypay/deleteWallet")
    public String delete(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepo.findByUsername(userDetails.getUsername());
        Token token = tokenRepo.findByOwnerId(user.getId().longValue());
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "bearer " + token.getAccess_token());
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.add("PartnerKey", "easypay-v2");
        headers.add("locale", "UA");
        headers.add("koatuu", "8000000000");
        headers.add("AppId", token.getAppId());
        headers.add("PageId", token.getPageId());
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> entity = restTemplate.exchange("https://api.easypay.ua/" + "api/wallets/delete/" + user.getWalletId(), HttpMethod.DELETE, httpEntity, String.class);
        user.setWalletNumber("0");
        user.setWalletId("0");
        userRepo.save(user);
        tokenRepo.delete(token);

        return "redirect:/main";
    }

    @PostMapping("/easypay/checkBalance")
    public String checkBalance(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepo.findByUsername(userDetails.getUsername());

        return "redirect:/order";
    }
    public static Token updateToken(int userId) {

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.easypay.ua/";

        App response = restTemplate.postForObject(url + "api/system/createApp", null, App.class);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", "easypay-v2-android");
        body.add("grant_type", "password");
        body.add("username", "380660508166");
        body.add("password", "Kolya69133");

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Accept", "application/json");
        headers.add("PartnerKey", "easypay-v2");
        headers.add("locale", "UA");
        headers.add("koatuu", "8000000000");
        headers.add("AppId", response.getAppId());
        headers.add("PageId", response.getPageId());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url + "api/token", httpEntity, String.class);

        Gson gson = new Gson();
        Token token = gson.fromJson(responseEntity.getBody(), Token.class);
        token.setOwnerId((long) userId);
        token.setPageId(response.getPageId());
        token.setAppId(response.getAppId());
        return token;
    }

}
