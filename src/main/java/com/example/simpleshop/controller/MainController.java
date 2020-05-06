package com.example.simpleshop.controller;

import com.example.simpleshop.domain.Point;
import com.example.simpleshop.domain.User;
import com.example.simpleshop.json.App;
import com.example.simpleshop.repos.PointRepo;
import com.example.simpleshop.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private PointRepo pointRepo;

    @Autowired
    private UserRepo userRepo;



    @GetMapping("/")
    public String greeting(
            @RequestParam(name = "name", required = false, defaultValue = "World") String name,
            Map<String, Object> model
    ) {
        model.put("name", name);
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal UserDetails userDetails, Map<String, Object> model) {
        List<Point> points = pointRepo.findByOrdered(false);
        model.put("points", points);

        model.put("filter", " ");

        return "main";
    }

    @PostMapping("/main")
    public String add(@RequestParam String photo, @RequestParam String price, @RequestParam String name, Map<String, Object> model) {

        Point point = new Point(photo, price, name);
        pointRepo.save(point);

        return "redirect:/main";
    }

    @PostMapping("order")
    public String startOrder(@AuthenticationPrincipal UserDetails userDetails, @RequestParam Integer id) {
        Point point = pointRepo.findById(id);
        pointRepo.delete(point);
        User user = userRepo.findByUsername(userDetails.getUsername());
        point.setCustomer(user);
        point.setOrdered(true);
        pointRepo.save(point);
        return "redirect:/order";

    }


    @GetMapping("/order")
    public String order(@AuthenticationPrincipal UserDetails userDetails, Map<String, Object> model) throws IOException {
        User user = userRepo.findByUsername(userDetails.getUsername());
        Point point = pointRepo.findByCustomer(user);
        model.put("point", point);

        RestTemplate restTemplate = new RestTemplate();
        RestTemplate restTemplate1 = new RestTemplate();

        String url = "https://api.easypay.ua/";

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("client_id", "easypay-v2-android");
        map.add("grant_type", "password");
        map.add("username", "380660508166");
        map.add("password", "Kolya69133");

        App response = restTemplate.postForObject(url + "api/system/createApp", null, App.class);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        httpHeaders.add("Accept", "application/json");
        httpHeaders.add("PartnerKey", "easypay-v2");
        httpHeaders.add("locale", "UA");
        httpHeaders.add("koatuu", "8000000000");
        httpHeaders.add("AppId", response.getAppId());
        httpHeaders.add("PageId", response.getPageId());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>( map, httpHeaders);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url+"api/token", httpEntity, String.class);
        String body = responseEntity.getBody();
        model.put("body", body);

        return "order";
    }

    @PostMapping("cancel")
    public String cancel(@RequestParam Integer id) {
        Point point = pointRepo.findById(id);
        pointRepo.delete(point);
        point.setCustomer(null);
        point.setOrdered(false);
        pointRepo.save(point);
        return "redirect:/main";
    }

    @PostMapping("filter")
    public String filter(@RequestParam String filter, Map<String, Object> model) {
        List<Point> points = pointRepo.findByName(filter);
        model.put("points", points);
        model.put("filter", filter);
        return "main";
    }

//    @PostMapping("verify")
//    public String verify(HttpServletResponse httpServletResponse, @AuthenticationPrincipal UserDetails userDetails, @RequestParam String code, Map<String, Object> model) throws NoSuchAlgorithmException {
//        User user = userRepo.findByUsername(userDetails.getUsername());
//        Point point = pointRepo.findByCustomer(user);
//        model.put("point", point);
//
//        RestTemplate restTemplate1 = new RestTemplate();
//        HashMap<String, String> map = new HashMap<>();
//
//
//        HashMap<String, String> map2 = new HashMap<>();
//        map2.put("Accept", "application/json");
//        map2.put("PartnerKey", "easypay-v2");
//        map2.put("koatuu", "8000000000");
//        map2.put("AppId", "28b47f54-f60a-445c-9593-1d84f4100a95");
//        map2.put("PageId", "8ac990f2-382f-463a-b86a-d7f4781e046f");
//        map2.put("locale", "UA");
//        //map2.put("safetyData", "03AGdBq25QJi5kfD5D5FDzGouP1kRLM6pKqK0wJ6IrjF26oecgJmP9EObsW2YErfSi_ofjyyvxJlrgMG8dV_cLSxwAiqveORxi2Yrk6mfKCCtKsfjwDeFK7qMD1cwhkFbqtHDFrAKvthsZSeW2KICRPVS_wxL_Pi8nIXYmucM7xLeKDM5dC35sqxxnmu7ZYsQR2tzg-P48FT21hOPyNT7NI9wTakI93F-w__jndX3SFsaqVkDxqejcH7LrMp5QhP8ERD8ELVI3jNJqQ1V6pu2CL362oVxbVZlT4svreJ4zNyTu8ESUfE9HLo8fkMLsb61py_k8WCg9BK00IvO9-sFXTVE8Dg8bxGdtcHtz5X12b1IbYB_-2L3EwbMt6gpE2LpUXF0WAcagJCiLXlpNV9TPiq8Z8Qejz8_31g");
//        String url = "https://api.easypay.ua/";
//
//        App request = new App();
//
//        App response = restTemplate1.postForObject(url + "api/system/createApp", request, App.class, map2);
//
//        user.setEpayPassword("Kolya69133");
//        user.setEpayLogin("380660508166");
//        Login login = new Login("easypay-v2-android", "password", user.getEpayLogin(), user.getEpayPassword());
//
//        assert response != null;
//        map2.replace("AppId", response.getAppId());
//        map2.replace("PageId", response.getPageId());
//        map2.put("Content-Type", "application/x-www-form-urlencoded");
//
//        RestTemplate restTemplate2 = new RestTemplate();
//
//        restTemplate2.headForHeaders(url+"api/token");
//        Token token = restTemplate2.postForObject(url + "api/token", login, Token.class, map2);
//
//        assert token != null;
//        String accesToken = token.getAccess_token();
//        model.put("accesToken", accesToken);
//
//        return "order";
//
//
//    }


}