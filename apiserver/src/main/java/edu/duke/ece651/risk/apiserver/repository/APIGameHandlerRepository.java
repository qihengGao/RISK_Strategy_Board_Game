package edu.duke.ece651.risk.apiserver.repository;

import edu.duke.ece651.risk.apiserver.APIGameHandler;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface APIGameHandlerRepository extends MongoRepository<APIGameHandler, Integer> {

    APIGameHandler findByRoomID(String roomID);

    Boolean existsAPIGameHandlerByRoomID(String roomID);
    public long count();
}
