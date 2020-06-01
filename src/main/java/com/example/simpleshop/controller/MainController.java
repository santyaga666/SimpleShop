package com.example.simpleshop.controller;

import com.example.simpleshop.domain.Point;
import com.example.simpleshop.domain.User;
import com.example.simpleshop.repos.PointRepo;
import com.example.simpleshop.repos.TokenRepo;
import com.example.simpleshop.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private PointRepo pointRepo;
    @Autowired
    private  TokenRepo tokenRepo;

    @GetMapping("/main")
    public String main(@RequestParam(value = "errorMessage", required = false, defaultValue = "") String errorMessage,
                       @RequestParam(value = "filter", required = false, defaultValue = "") String filter,
                       Map<String, Object> model) {
        List<Point> points = pointRepo.findByOrdered(false);
        model.put("errorMessage", errorMessage);
        model.put("points", points);
        model.put("filter", "");

        return "main";
    }

    @PostMapping("filter")
    public String filter(@RequestParam String filter) {
        return "redirect:/main?filter=" + filter;
    }

    @PostMapping("clear")
    public String clear(){
        pointRepo.deleteAll();
        tokenRepo.deleteAll();

        return "redirect:/main";
    }
}
