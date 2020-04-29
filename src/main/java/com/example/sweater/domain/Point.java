package com.example.sweater.domain;

import javax.persistence.*;

@Entity
public class Point {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String photo;
    private String price;
    private String name;

    private Boolean ordered;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    public Point() {
    }

    public Point(String photo, String price, String name) {
        this.photo = photo;
        this.price = price;
        this.name = name;
        this.ordered = false;
    }

    public Boolean getOrdered() {
        return ordered;
    }

    public void setOrdered(Boolean ordered) {
        this.ordered = ordered;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String location) {
        this.price = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
