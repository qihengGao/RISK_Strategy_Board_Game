package edu.duke.ece651.risk.apiserver.controllers;

import edu.duke.ece651.risk.apiserver.APIGameHandler;
import edu.duke.ece651.risk.apiserver.models.HistoryGame;
import edu.duke.ece651.risk.apiserver.models.HistoryGameConcise;
import edu.duke.ece651.risk.apiserver.payload.response.HistoryRoomsAvailableResponse;
import edu.duke.ece651.risk.apiserver.payload.response.RoomsAvailableResponse;
import edu.duke.ece651.risk.apiserver.repository.HistoryGameRepository;
import edu.duke.ece651.risk.apiserver.repository.UserRepository;
import edu.duke.ece651.risk.apiserver.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/historyGame")
public class HistoryGameController {

    @Autowired
    HistoryGameRepository historyGameRepository;

    @Autowired
    MongoTemplate mongoTemplate;



    /**
     * This method handle the get request of /gameStatus.
     *
     * @param roomID The roomID to look up.
     * @return ResponseEntity which contains the http code to indicate the result and the room status if the roomID is valid.
     */
    @GetMapping("/gameStatus")
    public ResponseEntity<RoomsAvailableResponse> gameStatus(@RequestParam Long roomID, @RequestParam Long roundNumber) {
        List<APIGameHandler> res = historyGameRepository.findHistoryGameByRoomIDAndRoundNumber(roomID, roundNumber).stream().map(historyGame -> historyGame.getApiGameHandler()).collect(Collectors.toList());
        ;
        return ResponseEntity.status(HttpStatus.OK).body(new RoomsAvailableResponse(res));
    }




    /**
     * This method handle the get request of /gameStatus.
     *
     */
    @GetMapping("/rooms/available")
    public ResponseEntity<HistoryRoomsAvailableResponse> availableHistoryGame() {
        final Aggregation aggregation = newAggregation(
                match(new Criteria("_id").ne(null)),
                sort(Sort.by(Sort.Direction.DESC, "roundNumber")),

                group("roomID").max("roundNumber").as("roundNumber").first("roomID").as("roomID"),
                sort(Sort.by(Sort.Direction.ASC, "roomID"))

        );

        final AggregationResults<HistoryGame> results = mongoTemplate.aggregate(aggregation, "HistoryGame", HistoryGame.class);
        System.out.println(results.getRawResults());
        List<HistoryGameConcise> res = results.getMappedResults().stream().map(historyGame -> {
            System.out.println(historyGame.getRoomID());
            return new HistoryGameConcise(historyGame.getRoomID(), historyGame.getRoundNumber());
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(new HistoryRoomsAvailableResponse(res));
    }


}
