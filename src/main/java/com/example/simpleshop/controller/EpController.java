package com.example.simpleshop.controller;

import com.example.simpleshop.domain.Point;
import com.example.simpleshop.domain.Token;
import com.example.simpleshop.domain.User;
import com.example.simpleshop.json.App;
import com.example.simpleshop.json.WalletInfo;
import com.example.simpleshop.json.WalletResponse;
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

import java.util.Map;

@Controller
public class EpController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PointRepo pointRepo;
    @Autowired
    private TokenRepo tokenRepo;

    @PostMapping("/easypay/getToken")
    public String createToken(@AuthenticationPrincipal UserDetails userDetails, Map<String, Object> model) {
        User user = userRepo.findByUsername(userDetails.getUsername());
        Point point = pointRepo.findByCustomer(user);
        model.put("point", point);

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
        token.setOwnerId(user.getId().longValue());
        token.setPageId(response.getPageId());
        token.setAppId(response.getAppId());
        tokenRepo.save(token);

        return "forward:/easypay/addWallet";
    }
    @PostMapping("/easypay/addWallet")
    public String addWallet(@AuthenticationPrincipal UserDetails userDetails, Map<String, Object> model) {
        User user = userRepo.findByUsername(userDetails.getUsername());
        Point point = pointRepo.findByCustomer(user);
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
        token.setWalletId(walletInfo.getId());
        httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> entity = restTemplate.exchange(url + "api/wallets/get/" + walletInfo.getId(), HttpMethod.GET, httpEntity, String.class);



        // Не знаю как получить автоматически 2 объекта . Решил захардкодить на время
        // Буду выделять подстроку

        String s0 = entity.getBody();
        int q = s0.indexOf("number");
        String s = s0.substring(q + 9, q+17);



        token.setWalletNumber(s);

        tokenRepo.save(token);

        return "redirect:/order";
    }
}
