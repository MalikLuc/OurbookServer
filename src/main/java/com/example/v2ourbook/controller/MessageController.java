package com.example.v2ourbook.controller;
import com.example.v2ourbook.error.ExceptionBlueprint;
import com.example.v2ourbook.models.Message;
import com.example.v2ourbook.services.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
public class MessageController {

    public MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/messages/create")
    public String createMessage(String text, Long chatId) throws InterruptedException, ExecutionException, ExceptionBlueprint {
        return messageService.createMessage(text, chatId);
    }



}
