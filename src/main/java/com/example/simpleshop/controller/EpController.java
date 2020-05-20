package com.example.simpleshop.controller;

import com.example.simpleshop.domain.Point;
import com.example.simpleshop.domain.Token;
import com.example.simpleshop.domain.User;
import com.example.simpleshop.json.App;
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

import java.time.LocalDateTime;
import java.util.Iterator;
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
    public String addWallet(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepo.findByUsername(userDetails.getUsername());
        Iterable<Token> token0 = tokenRepo.findAll();
        Iterator<Token> token = token0.iterator();
        Token token1 = token.next();

        if (checkIfExpired(token1)) {
            tokenRepo.deleteAll();
            token1 = EpController.updateToken();
            tokenRepo.save(token1);
        }

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("name", user.getUsername());
        body.add("color", "string");

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "bearer " + token1.getAccess_token());
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.add("PartnerKey", "easypay-v2");
        headers.add("locale", "UA");
        headers.add("koatuu", "8000000000");
        headers.add("AppId", token1.getAppId());
        headers.add("PageId", token1.getPageId());

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
        Iterable<Token> token0 = tokenRepo.findAll();
        Iterator<Token> token = token0.iterator();
        Token token1 = token.next();

        if (checkIfExpired(token1)) {
            tokenRepo.deleteAll();
            token1 = EpController.updateToken();
            tokenRepo.save(token1);
        }

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "bearer " + token1.getAccess_token());
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.add("PartnerKey", "easypay-v2");
        headers.add("locale", "UA");
        headers.add("koatuu", "8000000000");
        headers.add("AppId", token1.getAppId());
        headers.add("PageId", token1.getPageId());
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> entity = restTemplate.exchange("https://api.easypay.ua/" + "api/wallets/delete/" + user.getWalletId(), HttpMethod.DELETE, httpEntity, String.class);
        user.setWalletNumber("0");
        user.setWalletId("0");
        userRepo.save(user);

        return "redirect:/main";
    }

    @PostMapping("checkBalance")
    public String checkBalance(@AuthenticationPrincipal UserDetails userDetails, Map<String, Object> model) {
        User user = userRepo.findByUsername(userDetails.getUsername());
        Iterable<Token> token0 = tokenRepo.findAll();
        Iterator<Token> token = token0.iterator();
        Token token1 = token.next();

        if (checkIfExpired(token1)) {
            tokenRepo.deleteAll();
            token1 = EpController.updateToken();
            tokenRepo.save(token1);
        }
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("name", user.getUsername());
        body.add("color", "string");

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "bearer " + token1.getAccess_token());
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.add("PartnerKey", "easypay-v2");
        headers.add("locale", "UA");
        headers.add("koatuu", "8000000000");
        headers.add("AppId", token1.getAppId());
        headers.add("PageId", token1.getPageId());
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> entity = restTemplate.exchange("https://api.easypay.ua/api/wallets/get/" + user.getWalletId(), HttpMethod.GET, httpEntity, String.class);

        String s0 = entity.getBody();
        int q = s0.indexOf("balance");
        String s = s0.substring(q + 9, q + 12);
        double a = Double.parseDouble(s);

        Point point = pointRepo.findByCustomerId(user.getId());

        if (a >= Integer.parseInt(point.getPrice())) {
            model.put("point", point);

            return "success";
        }
        else {
            return "redirect:/order?errorMessage="+"Error! Not enough balance";
        }
    }

    public static Token updateToken() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.easypay.ua/";

        App response = restTemplate.postForObject(url + "api/system/createApp", null, App.class);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", "easypay-v2-android");
        body.add("grant_type", "password");
        body.add("username", "380916033956");
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
        token.setPageId(response.getPageId());
        token.setAppId(response.getAppId());
        return token;
    }

    public static boolean checkIfExpired(Token token) {
        //********************************************8
        //*****CHEKING IF TOKEN EXPIRED. !!!!!HARDCODE!!!!!**************8
        //***********************************************
        String expires = token.getExpires();
        String formattedExpires = expires.substring(0, expires.indexOf('+'));
        String formattedExpires1 = formattedExpires.substring(11, 13);
        int num = Integer.parseInt(formattedExpires1) + 3;
        String num1 = "" + num;
        if(num == 24) num1 = "00";
        if(num == 25) num1 = "01";
        if(num == 26) num1 = "02";
        if(num == 27) num1 = "03";
        String result = formattedExpires.substring(0, 11) + num1 + formattedExpires.substring(13);
        LocalDateTime localDateTime = LocalDateTime.parse(result);
        LocalDateTime localDateTime1 = LocalDateTime.now();
        return localDateTime.compareTo(localDateTime1) < 0 ? true : false;
    }

}
