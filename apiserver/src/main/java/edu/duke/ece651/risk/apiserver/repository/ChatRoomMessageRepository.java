package edu.duke.ece651.risk.apiserver.repository;

import edu.duke.ece651.risk.apiserver.APIGameHandler;
import edu.duke.ece651.risk.apiserver.models.ChatRoomMessage;
import edu.duke.ece651.risk.apiserver.payload.response.MessageResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRoomMessageRepository extends MongoRepository<ChatRoomMessage, Integer> {
    List<ChatRoomMessage> findAllByRoomID(Long roomID);

    public long count();
}
