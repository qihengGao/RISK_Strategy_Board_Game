package edu.duke.ece651.risk.apiserver;

import edu.duke.ece651.risk.apiserver.models.ChatRoomMessage;
import edu.duke.ece651.risk.apiserver.payload.response.OutputMessage;
import edu.duke.ece651.risk.apiserver.repository.APIGameHandlerRepository;
import edu.duke.ece651.risk.apiserver.repository.ChatRoomMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.List;

@Component
public class WebSocketEventListener {
    @Autowired
    private APIGameHandlerRepository apiGameHandlerRepository;


    @Autowired
    private ChatRoomMessageRepository chatRoomMessageRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        GenericMessage message = (GenericMessage) event.getMessage();
        String simpDestination = (String) message.getHeaders().get("simpDestination");
        System.out.println("subscribe:" + simpDestination);
        String simpDestinations[] = simpDestination.split("/");
        if (simpDestinations.length == 5) {
            Long roomID = Long.parseLong(simpDestinations[2]);
            Long userID = Long.parseLong(simpDestinations[4]);

            List<ChatRoomMessage> history_message = chatRoomMessageRepository.findAllByRoomID(roomID);

            for (ChatRoomMessage m : history_message) {
                messagingTemplate.convertAndSend(String.format("/topic/%s/user/%d", roomID, userID), new OutputMessage(m.getSenderID(), m.getMessage(), "topic",m.getMessageTime()));
            }

        }


//        messagingTemplate.convertAndSend("/topic/0", new OutputMessage("System", "System Message", "topic"));

//        if (simpDestination.startsWith("/topic/group/1")) {
//            // do stuff
//        }
    }
}