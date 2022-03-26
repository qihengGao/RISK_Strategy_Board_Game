package edu.duke.ece651.risk.apiserver.controllers;

import edu.duke.ece651.risk.apiserver.APIGameHandler;
import edu.duke.ece651.risk.apiserver.payload.request.CreateRoomRequest;
import edu.duke.ece651.risk.apiserver.payload.request.JoinRoomRequest;
import edu.duke.ece651.risk.apiserver.payload.request.PlaceUnitRequest;
import edu.duke.ece651.risk.apiserver.payload.response.CreateRoomResponse;
import edu.duke.ece651.risk.apiserver.payload.response.GameStatusResponse;
import edu.duke.ece651.risk.apiserver.payload.response.JoinRoomResponse;
import edu.duke.ece651.risk.apiserver.security.services.UserDetailsImpl;
import edu.duke.ece651.risk.server.Client;
import edu.duke.ece651.risk.server.GameHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/game")
public class GameController {


    private HashMap<Long, APIGameHandler> rooms;

    public GameController() {
        rooms = new HashMap<>();
    }

    private long roomIDCounter;


    @PostMapping("/createRoom")
//    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<CreateRoomResponse> createRoom(@Valid @RequestBody CreateRoomRequest createRoomRequest) {

        Long userId = getUserId();
        System.out.println(userId);

        int roomSize = createRoomRequest.getRoomSize();
        APIGameHandler gameHandler = new APIGameHandler(roomSize, roomIDCounter++,userId);

        rooms.put(gameHandler.getRoomID(), gameHandler);
        return ResponseEntity.ok(new CreateRoomResponse("Successfully create a game room!", gameHandler.getRoomID()));

    }

    @PostMapping("/joinRoom")
//    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<JoinRoomResponse> createRoom(@Valid @RequestBody JoinRoomRequest joinRoomRequest) {

        Long userId = getUserId();
        Long roomID = joinRoomRequest.getRoomID();
        APIGameHandler apiGameHandler = rooms.get(roomID);

        if (apiGameHandler == null || !apiGameHandler.addPlayer(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JoinRoomResponse("Failed joined a game room! Room not found or Room full!", roomID));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new JoinRoomResponse("Successfully joined a game room!", roomID));
        }


    }

    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();
        return userId;
    }

    @GetMapping("/gameStatus")
    public ResponseEntity<GameStatusResponse> gameStatus(@RequestParam Long roomID){
        Long userId = getUserId();

        if(rooms.containsKey(roomID)){
            APIGameHandler apiGameHandler = rooms.get(roomID);
            return ResponseEntity.status(HttpStatus.OK).body(new GameStatusResponse(
                    apiGameHandler.getPlayeState(userId),
                    apiGameHandler.getRiskMap(),
                    apiGameHandler.getWinner(),
                    apiGameHandler.getIdToColor(),
                    ""
            ));
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GameStatusResponse("Room not found!"));
        }
    }

    @PostMapping("/placeUnit")
    public ResponseEntity<GameStatusResponse> placeUnit(@Valid @RequestBody PlaceUnitRequest placeUnitRequest) {
        Long userId = getUserId();
        Long roomID = placeUnitRequest.getRoomID();
        APIGameHandler currGame = rooms.get(roomID);
        if (currGame == null ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JoinRoomResponse("Failed joined a game room! Room not found or Room full!", roomID));
        }

    }

}