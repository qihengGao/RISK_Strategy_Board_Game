package edu.duke.ece651.risk.apiserver.controllers;

import edu.duke.ece651.risk.apiserver.payload.request.CreateRoomRequest;
import edu.duke.ece651.risk.apiserver.payload.request.JoinRoomRequest;
import edu.duke.ece651.risk.apiserver.payload.response.CreateRoomResponse;
import edu.duke.ece651.risk.apiserver.payload.response.JoinRoomResponse;
import edu.duke.ece651.risk.server.GameHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/game")
public class GameController {


    private HashMap<Long, GameHandler> rooms;

    public GameController() {
        rooms = new HashMap<>();
    }

    private long roomIDCounter;


    @PostMapping("/createRoom")
//    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<CreateRoomResponse> createRoom(@RequestBody CreateRoomRequest createRoomRequest) {
        int roomSize = createRoomRequest.getRoomSize();
        rooms.put(roomIDCounter++, new GameHandler(roomSize, roomIDCounter - 1));
        return ResponseEntity.ok(new CreateRoomResponse("Successfully create a game room!", roomIDCounter - 1));
    }

    @PostMapping("/joinRoom")
//    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<JoinRoomResponse> createRoom(@RequestBody JoinRoomRequest joinRoomRequest) {
        Long roomID = joinRoomRequest.getRoomID();
        GameHandler game = rooms.get(roomID);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JoinRoomResponse("Successfully joined a game room!", roomID));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new JoinRoomResponse("Successfully joined a game room!", roomID));
        }


    }


}