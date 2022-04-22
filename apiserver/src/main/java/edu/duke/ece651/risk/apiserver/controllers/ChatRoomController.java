package edu.duke.ece651.risk.apiserver.controllers;


import edu.duke.ece651.risk.apiserver.models.ChatRoomMessage;
import edu.duke.ece651.risk.apiserver.payload.request.Message;
import edu.duke.ece651.risk.apiserver.payload.response.OutputMessage;
import edu.duke.ece651.risk.apiserver.repository.APIGameHandlerRepository;
import edu.duke.ece651.risk.apiserver.repository.ChatRoomMessageRepository;
import edu.duke.ece651.risk.apiserver.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Controller
public class ChatRoomController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private APIGameHandlerRepository apiGameHandlerRepository;


    @Autowired
    private ChatRoomMessageRepository chatRoomMessageRepository;


    /**
     * This is a chat room controller, using mongoDB to persist store the history message.
     * @param roomID RoomID of the message.
     * @param message Message it self.
     * @param accessor Stomp accessor contains the raw message of Stomp protocol.
     */
    @MessageMapping("/chat/{roomID}")
    public void simple(@DestinationVariable String roomID, Message message, StompHeaderAccessor accessor) {

        UserDetailsImpl userDetails = (UserDetailsImpl) ((Authentication)accessor.getUser()).getDetails();
        //System.out.println(userDetails.getId());

        ChatRoomMessage new_message = new ChatRoomMessage(Long.parseLong(roomID),message.getText(),userDetails.getId(),"public", LocalDateTime.now());
        chatRoomMessageRepository.save(new_message);
        Set<Long> playerList = apiGameHandlerRepository.findByRoomID(roomID).getPlayers();
        for(Long playerID : playerList){
            messagingTemplate.convertAndSend(String.format("/topic/%s/user/%d",roomID,playerID), new OutputMessage(new_message.getSenderID(), new_message.getMessage(), "topic",new_message.getMessageTime()));
        }

    }
}
