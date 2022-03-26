package edu.duke.ece651.risk.apiserver.controllers;

import edu.duke.ece651.risk.apiserver.models.ERole;
import edu.duke.ece651.risk.apiserver.models.Role;
import edu.duke.ece651.risk.apiserver.models.User;
import edu.duke.ece651.risk.apiserver.payload.request.CreateRoomRequest;
import edu.duke.ece651.risk.apiserver.payload.request.JoinRoomRequest;
import edu.duke.ece651.risk.apiserver.payload.request.SignupRequest;
import edu.duke.ece651.risk.apiserver.payload.response.CreateRoomResponse;
import edu.duke.ece651.risk.apiserver.payload.response.JoinRoomResponse;
import edu.duke.ece651.risk.apiserver.payload.response.MessageResponse;
import edu.duke.ece651.risk.server.GameHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/game")
public class GameController {


    private HashMap<Long,GameHandler> rooms;

    public GameController() {
        rooms = new HashMap<>();
    }

    private long roomIDCounter;


    @PostMapping("/createRoom")
//    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<CreateRoomResponse> createRoom(@RequestBody CreateRoomRequest createRoomRequest) {
        int roomSize = createRoomRequest.getRoomSize();
        rooms.put(roomIDCounter++,new GameHandler(roomSize,roomIDCounter-1));
        return ResponseEntity.ok(new CreateRoomResponse("Successfully create a game room!",roomIDCounter-1));
    }

    @PostMapping("/joinRoom")
//    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<JoinRoomResponse> createRoom(@RequestBody JoinRoomRequest joinRoomRequest) {
        Long roomID = joinRoomRequest.getRoomID();
        GameHandler game = rooms.get(roomID);
        if (game==null) return ResponseEntity.badRequest(new JoinRoomResponse("Cannot find room",roomID));
        return ResponseEntity.ok(new JoinRoomResponse("Successfully joined a game room!",roomID));
    }






}