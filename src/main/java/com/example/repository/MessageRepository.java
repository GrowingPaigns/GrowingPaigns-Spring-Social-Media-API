package com.example.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{
    Message findByMessageId(int messageId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Message m WHERE m.messageId = :messageId")
    int deleteByMessageId(@Param("messageId") int messageId);

    @Modifying
    @Transactional
    @Query("UPDATE Message m SET m.messageText = :newMessageText WHERE m.messageId = :messageId")
    int updateByMessageId(@Param("newMessageText") String newMessage, @Param("messageId") int messageId);

    @Query("FROM Message m LEFT JOIN Account a ON m.messageId = a.accountId WHERE m.messageId = :messageId")
    List<Message> findAllMessagesByAccountId(@Param("messageId") int messageId);
}
