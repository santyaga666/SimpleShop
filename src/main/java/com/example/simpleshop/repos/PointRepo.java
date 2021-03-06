package com.example.simpleshop.repos;

import com.example.simpleshop.domain.Point;
import com.example.simpleshop.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PointRepo extends CrudRepository<Point, Long> {
    List<Point> findByNameAndOrdered(String name, Boolean ordered);
    Point findByCustomerId(Long id);
    Point findById(Integer id);
    Point findByCustomer(User user);
    List<Point> findByOrdered(Boolean ordered);
    List<Point> findByCustomerIsNull();
}
