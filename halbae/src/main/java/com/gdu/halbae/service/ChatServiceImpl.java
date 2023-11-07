package com.gdu.halbae.service;

import com.gdu.halbae.domain.ConversationDTO;
import com.gdu.halbae.domain.MessageDTO;
import com.gdu.halbae.mapper.ChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {
  
    private final ChatMapper chatMapper;

    @Autowired
    public ChatServiceImpl(ChatMapper chatMapper) {
        this.chatMapper = chatMapper;
    }

    @Override
    public List<ConversationDTO> getConversationList() {
        return chatMapper.getConversationList();
    }

    @Override
    public ConversationDTO getConversation(int conId) {
        return chatMapper.getConversation(conId);
    }

    @Override
    public List<MessageDTO> getMessageList(int conId) {
        return chatMapper.getMessageList(conId);
    }

    @Override
    public void sendMessage(int conId, int userNo, String message) {
        chatMapper.sendMessage(conId, userNo, message);
    }

    @Override
    public void exitChatRoom(int conId, int userNo) {
        chatMapper.exitChatRoom(conId, userNo);
    }

    @Override
    public void deleteChatRoom(int conId) {
        chatMapper.deleteChatRoom(conId);
    }
}
