package com.example.simpleshop.controller;

import com.example.simpleshop.domain.Point;
import com.example.simpleshop.domain.Token;
import com.example.simpleshop.domain.User;
import com.example.simpleshop.json.MyAuthenticator;
import com.example.simpleshop.repos.PointRepo;
import com.example.simpleshop.repos.TokenRepo;
import com.example.simpleshop.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.net.Authenticator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {
    @Autowired
    private PointRepo pointRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TokenRepo tokenRepo;

    @GetMapping("/order")
    public String order(@AuthenticationPrincipal UserDetails userDetails, Map<String, Object> model) {
        User user = userRepo.findByUsername(userDetails.getUsername());
        Point point = pointRepo.findByCustomer(user);
        model.put("wallet", user.getWalletNumber());
        model.put("point", point);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = restTemplate.getForEntity("https://tnf.fastfen.club/api/Info?action=getSessionState", String.class);
        String s0 = entity.getBody();
        int a = s0.indexOf("Ip");
        String s = s0.substring(a+6, a+19);
        model.put("ip", s);

        return "order";
    }
    @PostMapping("cancel")
    public String cancel(@AuthenticationPrincipal UserDetails userDetails) {
        Point point = pointRepo.findByCustomerId(userRepo.findByUsername(userDetails.getUsername()).getId());
        pointRepo.delete(point);
        point.setCustomer(null);
        point.setOrdered(false);
        pointRepo.save(point);
        return "forward:/easypay/deleteWallet";
    }
    @PostMapping("order")
    public String startOrder(@AuthenticationPrincipal UserDetails userDetails, @RequestParam Integer id) {
        User user = userRepo.findByUsername(userDetails.getUsername());
        if(pointRepo.findByCustomerId(user.getId()) != null)
            return "redirect:/main?message=" + "Error! Please complete exist order!";

        Iterable<Token> token0 = tokenRepo.findAll();
        Iterator<Token> token = token0.iterator();

        if(!token.hasNext()) {
            Token token2 = EpController.updateToken();
            tokenRepo.save(token2);
        }

        Point point = pointRepo.findById(id);
        pointRepo.delete(point);
        point.setCustomer(user);
        point.setOrdered(true);
        pointRepo.save(point);


        return "forward:/easypay/addWallet";
    }
    @PostMapping("goToOrder")
    public String goToOrder() {
        return "redirect:/order";
    }
}
