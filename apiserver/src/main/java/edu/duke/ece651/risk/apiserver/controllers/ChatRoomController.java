package edu.duke.ece651.risk.apiserver.controllers;


import edu.duke.ece651.risk.apiserver.payload.request.Message;
import edu.duke.ece651.risk.apiserver.payload.response.OutputMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatRoomController {
    @MessageMapping("/chat/{topic}")
    @SendTo("/topic/{topic}")
    public OutputMessage send(
            @DestinationVariable("topic") String topic, Message message)
            throws Exception {
        System.out.println(topic);
        return new OutputMessage(message.getFrom(), message.getText(), topic);
    }
}
