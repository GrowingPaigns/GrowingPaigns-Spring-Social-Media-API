package com.example.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.ClientErrorException;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.UnauthorizedUserException;
import com.example.service.AccountService;
import com.example.service.MessageService;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired AccountService accountService;
    @Autowired MessageService messageService;

    @PostMapping("register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account requestedAccount) {
        try {
            Account newAccount = accountService.registerAccount(requestedAccount);
            return ResponseEntity.ok(newAccount);
        } catch (DuplicateUsernameException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }  
    }

    @PostMapping("login")
    public ResponseEntity<Account> loginAccount(@RequestBody Account requestedAccount) {
        try {
            Account newAccount = accountService.loginAccount(requestedAccount);
            return ResponseEntity.ok(newAccount);
        } catch (UnauthorizedUserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("messages")
    public ResponseEntity<Message> createNewMessage(@RequestBody Message messageData) {
        try {
            Message newMessage = messageService.createNewMessage(messageData);
            return ResponseEntity.ok(newMessage);
        } catch (ClientErrorException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("messages")
    @ResponseBody
    public ResponseEntity<List<Message>> retrieveAllMessages() {
        List<Message> messages = messageService.retrieveAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("accounts/{accountId}/messages")
    @ResponseBody
    public ResponseEntity<List<Message>> retrieveAllMessagesByUser(@PathVariable("accountId") int accountId) {
        List<Message> messages = messageService.retrieveAllMessagesByAccount(accountId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("messages/{messageId}")
    @ResponseBody
    public ResponseEntity<Message> retrieveAllMessages(@PathVariable("messageId") int messageId) {
        Message messages = messageService.findMessageById(messageId);
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("messages/{messageId}")
    @ResponseBody
    public ResponseEntity<?> deleteMessageById(@PathVariable("messageId") int messageId) {
        int rowsUpdated = messageService.deleteMessageById(messageId);
        if (rowsUpdated > 0) return ResponseEntity.ok(rowsUpdated);
        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @PatchMapping("messages/{messageId}")
    public ResponseEntity<?> updateMessageById(@PathVariable("messageId") int messageId, @RequestBody Message newMessage) {
        try {
            
            int rowsUpdated = messageService.updateMessageById(messageId, newMessage);
            if (rowsUpdated > 0) {
                return ResponseEntity.ok(rowsUpdated);
            }
            throw new ClientErrorException("No rows updated");
        } catch (ClientErrorException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
    }
}
