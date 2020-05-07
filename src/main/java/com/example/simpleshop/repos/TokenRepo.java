package com.example.simpleshop.repos;

import com.example.simpleshop.domain.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepo extends CrudRepository<Token, Long> {
    Token findByOwnerId(Long id);
}
