package com.example.sweater.repos;

import com.example.sweater.domain.Point;
import com.example.sweater.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PointRepo extends CrudRepository<Point, Long> {
    Point findById(Integer id);
    Point findByCustomer(User user);
    List<Point> findByOrdered(Boolean ordered);
    List<Point> findByCustomerIsNull();
    Point findByCustomerId(Integer id);
}
