package com.gdu.halbae.mapper;

import com.gdu.halbae.domain.ConversationDTO;
import com.gdu.halbae.domain.MessageDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMapper {
    List<ConversationDTO> getConversationList();
    ConversationDTO getConversation(@Param("conId") int conId);
    List<MessageDTO> getMessageList(@Param("conId") int conId);
    void sendMessage(@Param("conId") int conId, @Param("userNo") int userNo, @Param("message") String message);
    void exitChatRoom(@Param("conId") int conId, @Param("userNo") int userNo);
    void deleteChatRoom(@Param("conId") int conId);
}
