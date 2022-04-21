package edu.duke.ece651.risk.apiserver.repository;

import edu.duke.ece651.risk.apiserver.models.ChatRoomMessage;
import edu.duke.ece651.risk.apiserver.models.HistoryOrders;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HistoryOrdersRepository extends MongoRepository<HistoryOrders, Integer> {

}
