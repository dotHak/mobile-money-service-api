package com.hubert.momoservice.service;

import com.hubert.momoservice.entity.Token;
import com.hubert.momoservice.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TokenService  implements GenericService<Token, Long> {

    private final TokenRepository repository;

    @Autowired
    public TokenService(TokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Token> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Token> getOne(Long id) {
        return repository.findById(id);
    }

    @Override
    public Token save(Token token) {
        return repository.save(token);
    }
}
