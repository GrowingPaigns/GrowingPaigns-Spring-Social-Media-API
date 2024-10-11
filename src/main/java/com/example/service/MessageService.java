package com.example.service;

import java.util.List;
import java.util.Optional;

import javax.management.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.ClientErrorException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Message createNewMessage (Message messageData) {
        String messageText = messageData.getMessageText();
        Integer postedBy = messageData.getPostedBy();

        if (messageText.length() <= 255 && !messageText.isEmpty()) {
            Account verifiedAccount = accountRepository.findByAccountId(postedBy);
            if (verifiedAccount != null) {
                return messageRepository.save(messageData);
            }
            else throw new ClientErrorException("Referenced account not found");
        }
        else throw new ClientErrorException("Message text outside expected range (0-255)");
    }

    public List<Message> retrieveAllMessages () {
        return messageRepository.findAll();
    }

    public Message findMessageById(int id) {
        Message message = messageRepository.findByMessageId(id);
        return message;
    }

    @Transactional
    public int deleteMessageById(int id) {
        return messageRepository.deleteByMessageId(id);
    }

    @Transactional
    public int updateMessageById(int id, Message newMessageText) {
        System.out.println("Message Text: " + newMessageText.getMessageText());
        String messageText = newMessageText.getMessageText();
        if (messageText.length() <= 255 
        && !messageText.isEmpty()) {
            int rowsUpdated = messageRepository.updateByMessageId(messageText, id);
            return rowsUpdated;
        }
        else throw new ClientErrorException("Message length invalid");
    }

    public List<Message> retrieveAllMessagesByAccount (int accountId) {
        Account account = accountRepository.findByAccountId(accountId);
        if (account != null) {
            return messageRepository.findAllMessagesByAccountId(accountId);
        }
        return null;
    }
}
