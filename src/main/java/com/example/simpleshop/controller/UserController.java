package com.example.simpleshop.controller;

import com.example.simpleshop.domain.Point;
import com.example.simpleshop.domain.User;
import com.example.simpleshop.repos.PointRepo;
import com.example.simpleshop.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PointRepo pointRepo;

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userRepo.findAll());

        return "adminPanel";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);

        return "userEdit";
    }

    @PostMapping
    public String add(@RequestParam String photo, @RequestParam String price, @RequestParam String name, Map<String, Object> model) {

        Point point = new Point(photo, price, name);
        pointRepo.save(point);

        return "redirect:/user";
    }
}
