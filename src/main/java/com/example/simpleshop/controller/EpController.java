package com.example.simpleshop.controller;

import com.example.simpleshop.domain.Point;
import com.example.simpleshop.domain.Token;
import com.example.simpleshop.domain.User;
import com.example.simpleshop.json.App;
import com.example.simpleshop.repos.PointRepo;
import com.example.simpleshop.repos.UserRepo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
public class EpController {
    @Autowired
    UserRepo userRepo;
    @Autowired
    PointRepo pointRepo;

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
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Accept", "application/json");
        headers.add("PartnerKey", "easypay-v2");
        headers.add("locale", "UA");
        headers.add("koatuu", "8000000000");
        headers.add("AppId", response.getAppId());
        headers.add("PageId", response.getPageId());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url + "api/token", httpEntity, String.class);
        JSONObject object = new JSONObject(responseEntity.getBody());
        Token token = new Token(object.getString("access_token"), object.getString("token_type"), object.getString("refresh_token"), object.getString("userName"), object.getString("userId"), object.getString("as:client_id"), object.getString(".issued"), object.getString(".expires"));
        user.setToken(token);
        userRepo.save(user);

        return "order";
    }
}
