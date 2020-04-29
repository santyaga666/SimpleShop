package com.example.sweater.controller;

import com.example.sweater.domain.Point;
import com.example.sweater.domain.User;
import com.example.sweater.repos.PointRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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


    @GetMapping("/")
    public String greeting(
            @RequestParam(name = "name", required = false, defaultValue = "World") String name,
            Map<String, Object> model
    ) {
        model.put("name", name);
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal User user, Map<String, Object> model) {
        List<Point> points = pointRepo.findByOrdered(false);

        model.put("points", points);

        return "main";
    }

    @PostMapping("/main")
    public String add(@RequestParam String photo, @RequestParam String price, @RequestParam String name, Map<String, Object> model) {

        Point point = new Point(photo, price, name);
        pointRepo.save(point);

        return "redirect:/main";
    }

    @PostMapping("order")
    public String startOrder(@AuthenticationPrincipal User user, @RequestParam Integer id) {
        Point point = pointRepo.findById(id);
        pointRepo.delete(point);
        point.setCustomer(user);
        point.setOrdered(true);
        pointRepo.save(point);
        return "redirect:/order";
    }
    @GetMapping("/order")
    public String order(@AuthenticationPrincipal User user, Map<String, Object> model) {
        Point point = pointRepo.findByCustomer(user);
        model.put("point", point);

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


}
