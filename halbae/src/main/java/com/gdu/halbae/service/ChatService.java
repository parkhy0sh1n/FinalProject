package com.gdu.halbae.service;

import com.gdu.halbae.domain.ConversationDTO;
import com.gdu.halbae.domain.MessageDTO;

import java.util.List;

public interface ChatService {
    List<ConversationDTO> getConversationList();
    ConversationDTO getConversation(int conId);
    List<MessageDTO> getMessageList(int conId);
    void sendMessage(int conId, int userNo, String message);
    void exitChatRoom(int conId, int userNo);
    void deleteChatRoom(int conId);
}