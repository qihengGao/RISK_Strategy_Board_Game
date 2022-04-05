package edu.duke.ece651.risk.apiserver.controllers;

import edu.duke.ece651.risk.apiserver.APIGameHandler;
import edu.duke.ece651.risk.apiserver.models.State;
import edu.duke.ece651.risk.apiserver.payload.request.CreateRoomRequest;
import edu.duke.ece651.risk.apiserver.payload.request.JoinRoomRequest;
import edu.duke.ece651.risk.apiserver.payload.request.PlaceOrderRequest;
import edu.duke.ece651.risk.apiserver.payload.request.PlaceUnitRequest;
import edu.duke.ece651.risk.apiserver.payload.response.*;
import edu.duke.ece651.risk.apiserver.security.services.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

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
//        if (roomSize<2 || roomSize > 5) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CreateRoomResponse("Failed to create a room! Room size must be between 2~5!", null));
//        }
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

        if (apiGameHandler == null || !apiGameHandler.tryAddPlayer(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JoinRoomResponse("Failed joined a game room! Room not found or Room full!", roomID));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new JoinRoomResponse("Successfully joined a game room!", roomID));
        }


    }

    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }

    @GetMapping("/gameStatus")
    public ResponseEntity<GameStatusResponse> gameStatus(@RequestParam Long roomID){
        Long userId = getUserId();

        if(rooms.containsKey(roomID)&&rooms.get(roomID).getPlayers().contains(userId)){
            APIGameHandler apiGameHandler = rooms.get(roomID);
            return ResponseEntity.status(HttpStatus.OK).body(new GameStatusResponse(
                    apiGameHandler.getPlayerState(userId),
                    apiGameHandler.getRiskMapByState(),
                    apiGameHandler.checkWinner(),
                    apiGameHandler.getIdToColor(),
                    ""
            ));
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GameStatusResponse("Room not found!"));
        }
    }

    @PostMapping("/place/unit")
    public ResponseEntity<PlaceUnitResponse> placeUnit(@Valid @RequestBody PlaceUnitRequest placeUnitRequest) {
        Long userId = getUserId();
        Long roomID = placeUnitRequest.getRoomID();
        APIGameHandler currGame = rooms.get(roomID);
        if (currGame == null || !currGame.tryPlaceUnit(userId,placeUnitRequest.getUnitPlaceOrders())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PlaceUnitResponse("Failed to palce the units! Room not found or palce action invalid right now!"));
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(new PlaceUnitResponse("Successfully placed unit into map."));
        }

    }


    @PostMapping("/place/order")
    public ResponseEntity<PlaceUnitResponse> placeUnit(@Valid @RequestBody PlaceOrderRequest placeOrderRequest) {
        Long userId = getUserId();
        Long roomID = placeOrderRequest.getRoomID();

        APIGameHandler currGame = rooms.get(roomID);

        if (currGame == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PlaceUnitResponse("Cannot find room "+roomID+"!"));
        }

        String preprocess_message = currGame.tryPreProcessOrder(userId, placeOrderRequest.getOrders());

        if (preprocess_message != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PlaceUnitResponse(preprocess_message));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new PlaceUnitResponse("Successfully placed order into map!"));
        }

    }

    @GetMapping("/rooms/available")
    public ResponseEntity<RoomsAvailableResponse> allRooms( ){
        Long userId = getUserId();

        List<APIGameHandler> res = rooms.entrySet().stream()
                .filter(e -> (State.WaitingToStartState.name().equals(e.getValue().getCurrentState())
                        && !e.getValue().getPlayers().contains(userId)))
                .map(Map.Entry::getValue).collect(Collectors.toList());;
        return ResponseEntity.status(HttpStatus.OK).body(new RoomsAvailableResponse(res));
    }

    @GetMapping("/rooms/joined")
    public ResponseEntity<RoomsAvailableResponse> joinedRooms( ){
        Long userId = getUserId();

        List<APIGameHandler> res = rooms.entrySet().stream()
                .filter(e -> e.getValue().getPlayers().contains(userId)).map(Map.Entry::getValue).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(new RoomsAvailableResponse(res));
    }



}