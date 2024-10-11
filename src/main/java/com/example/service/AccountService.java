package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.UnauthorizedUserException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account registerAccount (Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank()) 
            throw new IllegalArgumentException("Username cannot be blank");
        if (account.getPassword() == null || account.getPassword().length() < 4) 
            throw new IllegalArgumentException("Password must be at least 4 characters");
        if (accountRepository.findByUsername(account.getUsername()) != null) {
            throw new DuplicateUsernameException("Username already taken");
        }

        return accountRepository.save(account);
    }

    public Account loginAccount (Account account) {
        Account foundAccount = accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword());
        if (foundAccount != null) return foundAccount;
        else throw new UnauthorizedUserException("Account not found");
    }
}
