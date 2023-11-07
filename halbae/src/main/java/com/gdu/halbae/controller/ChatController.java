package com.gdu.halbae.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdu.halbae.domain.ConversationDTO;
import com.gdu.halbae.domain.MessageDTO;
import com.gdu.halbae.service.ChatService;

@Controller
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/list")
    public String chatList(Model model) {
        List<ConversationDTO> conversations = chatService.getConversationList();
        model.addAttribute("conversations", conversations);
        return "chat/chatList";
    }
    
    @GetMapping("/list2")
    public String chatList2(Model model) {
        List<ConversationDTO> conversations = chatService.getConversationList();
        model.addAttribute("conversations", conversations);
        return "chat/chatList2";
    }

    @GetMapping("/{conId}")
    public String chatRoom(@PathVariable int conId, Model model) {
        ConversationDTO conversation = chatService.getConversation(conId);
        List<MessageDTO> messages = chatService.getMessageList(conId);
        model.addAttribute("conversation", conversation);
        model.addAttribute("messages", messages);
        return "chat/chat";
    }

    @PostMapping("/{conId}/send")
    @ResponseBody
    public String sendMessage(@PathVariable int conId, @RequestParam int userNo, @RequestParam String message) {
        chatService.sendMessage(conId, userNo, message);
        return "success";
    }

    @PostMapping("/{conId}/exit")
    @ResponseBody
    public String exitChatRoom(@PathVariable int conId, @RequestParam int userNo) {
        chatService.exitChatRoom(conId, userNo);
        return "success";
    }

    @PostMapping("/{conId}/delete")
    @ResponseBody
    public String deleteChatRoom(@PathVariable int conId) {
        chatService.deleteChatRoom(conId);
        return "success";
    }
}