package edu.duke.ece651.risk.apiserver.controllers;

import edu.duke.ece651.risk.apiserver.APIGameHandler;
import edu.duke.ece651.risk.apiserver.payload.response.GameStatusResponse;
import edu.duke.ece651.risk.apiserver.repository.HistoryGameRepository;
import edu.duke.ece651.risk.apiserver.repository.UserRepository;
import edu.duke.ece651.risk.apiserver.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/historyGame")
public class HistoryGameController {
    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Autowired
    private UserRepository userRepository;

    private HashMap<Long, APIGameHandler> rooms;

    @Autowired
    HistoryGameRepository historyGameRepository;


    /**
     * This is a helper function to get the current user id.
     *
     * @return The user ID of current user.
     */
    protected Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        userDetails.setElo(1000L);
        return userDetails.getId();
    }


    /**
     * This method handle the get request of /gameStatus.
     *
     * @param roomID The roomID to look up.
     * @return ResponseEntity which contains the http code to indicate the result and the room status if the roomID is valid.
     */
    @GetMapping("/gameStatus")
    public ResponseEntity<GameStatusResponse> gameStatus(@RequestParam Long roomID, @RequestParam Long roundNumber) {


        //TODO Using history game handler repo and check if round number valid
        if (apiGameHandlerRepository.existsAPIGameHandlerByRoomID(String.valueOf(roomID)) && apiGameHandlerRepository.findByRoomID(String.valueOf(roomID)).getPlayers().contains(playerID)) {
            //APIGameHandler apiGameHandler = rooms.get(roomID);

            APIGameHandler apiGameHandler = apiGameHandlerRepository.findByRoomID(String.valueOf(roomID));

            beanFactory.autowireBean(apiGameHandler);


            return ResponseEntity.status(HttpStatus.OK).body(new GameStatusResponse(
                    apiGameHandler.getPlayerState(playerID),
                    apiGameHandler.getRiskMapByState(),
                    apiGameHandler.checkWinner(),
                    apiGameHandler.getIdToColor(),
                    ""
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GameStatusResponse("Room not found!"));
        }
    }


    //TODO
    //{
    /**
     * {
     *     [
     *     {
     *          //all of history repo
     *     }
     *     ]
     * }
     */
    // }

    /**
     * This method handle the get request of /gameStatus.
     *
     * @param roomID The roomID to look up.
     * @return ResponseEntity which contains the http code to indicate the result and the room status if the roomID is valid.
     */
    @GetMapping("/availableHistoryGame")
    public ResponseEntity<GameStatusResponse> availableHistoryGame() {


        //TODO Using history game handler repo and check if round number valid
        if (apiGameHandlerRepository.existsAPIGameHandlerByRoomID(String.valueOf(roomID)) && apiGameHandlerRepository.findByRoomID(String.valueOf(roomID)).getPlayers().contains(playerID)) {
            //APIGameHandler apiGameHandler = rooms.get(roomID);

            APIGameHandler apiGameHandler = apiGameHandlerRepository.findByRoomID(String.valueOf(roomID));

            beanFactory.autowireBean(apiGameHandler);


            return ResponseEntity.status(HttpStatus.OK).body(new GameStatusResponse(
                    apiGameHandler.getPlayerState(playerID),
                    apiGameHandler.getRiskMapByState(),
                    apiGameHandler.checkWinner(),
                    apiGameHandler.getIdToColor(),
                    ""
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GameStatusResponse("Room not found!"));
        }
    }


}
