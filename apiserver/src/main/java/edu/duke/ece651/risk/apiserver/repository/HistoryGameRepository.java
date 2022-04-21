package edu.duke.ece651.risk.apiserver.repository;

import edu.duke.ece651.risk.apiserver.models.HistoryGame;
import edu.duke.ece651.risk.apiserver.models.HistoryOrders;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HistoryGameRepository extends MongoRepository<HistoryGame, Integer> {
    List<HistoryGame> findHistoryGameByRoomIDAndRoundNumber( Long roomID, Long roundNumber);
}
