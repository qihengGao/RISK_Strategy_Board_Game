package edu.duke.ece651.risk.apiserver.controllers;

import edu.duke.ece651.risk.apiserver.APIGameHandler;
import edu.duke.ece651.risk.apiserver.APIGameHandlerComparator;
import edu.duke.ece651.risk.apiserver.models.GroceryItem;
import edu.duke.ece651.risk.apiserver.models.State;
import edu.duke.ece651.risk.apiserver.payload.request.CreateRoomRequest;
import edu.duke.ece651.risk.apiserver.payload.request.JoinRoomRequest;
import edu.duke.ece651.risk.apiserver.payload.request.PlaceOrderRequest;
import edu.duke.ece651.risk.apiserver.payload.request.PlaceUnitRequest;
import edu.duke.ece651.risk.apiserver.payload.response.*;
import edu.duke.ece651.risk.apiserver.repository.APIGameHandlerRepository;
import edu.duke.ece651.risk.apiserver.repository.ItemRepository;
import edu.duke.ece651.risk.apiserver.repository.UserRepository;
import edu.duke.ece651.risk.apiserver.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/game")
public class GameController {
    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Autowired
    private UserRepository userRepository;

    private HashMap<Long, APIGameHandler> rooms;

    public GameController() {
        rooms = new HashMap<>();
    }

    private long roomIDCounter;
    @Autowired
    APIGameHandlerRepository apiGameHandlerRepository;

    /**
     * This method handle the post request of /createRoom.
     *
     * @param createRoomRequest A deserialize json object contains roomSize.
     * @return ResponseEntity which contains the http code to indicate the result.
     */
    @PostMapping("/createRoom")
//    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<CreateRoomResponse> createRoom(@Valid @RequestBody CreateRoomRequest createRoomRequest) {

        Long userId = getUserId();
        System.out.println("User " + userId + " trying to create a Room");

        int roomSize = createRoomRequest.getRoomSize();
        APIGameHandler gameHandler = new APIGameHandler(roomSize, roomIDCounter++, userId);
        beanFactory.autowireBean(gameHandler);
        gameHandler.updateAverageElo();
        gameHandler.setCompetitive(createRoomRequest.isCompetitive());

        String message = "Successfully create a game room!";
        if(gameHandler.isCompetitive()){
            message = "Successfully create a competitive game room!";
        }

        System.out.println(message);

        apiGameHandlerRepository.save(gameHandler);

        return ResponseEntity.ok(new CreateRoomResponse(message, gameHandler.getRoomID()));
    }


    /**
     * This method handle the post request of /joinRoom.
     *
     * @param joinRoomRequest A deserialize json object contains roomID.
     * @return ResponseEntity which contains the http code to indicate the result.
     */
    @PostMapping("/joinRoom")
//    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<JoinRoomResponse> joinRoom(@Valid @RequestBody JoinRoomRequest joinRoomRequest) {

        Long userId = getUserId();
        Long roomID = joinRoomRequest.getRoomID();
        //APIGameHandler apiGameHandler = rooms.get(roomID);
        APIGameHandler apiGameHandler = apiGameHandlerRepository.findByRoomID(String.valueOf(roomID));
        beanFactory.autowireBean(apiGameHandler);
        if (apiGameHandler == null || !apiGameHandler.tryAddPlayer(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JoinRoomResponse("Failed to join! Room not found or Room full!", roomID));
        } else {
            apiGameHandlerRepository.save(apiGameHandler);
            return ResponseEntity.status(HttpStatus.OK).body(new JoinRoomResponse("Successfully joined a game room!", roomID));
        }


    }

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
    public ResponseEntity<GameStatusResponse> gameStatus(@RequestParam Long roomID) {
        Long userId = getUserId();
//        System.out.println(userRepository.findByid(userId).orElse(null).getElo());

        if (apiGameHandlerRepository.existsAPIGameHandlerByRoomID(String.valueOf(roomID)) && apiGameHandlerRepository.findByRoomID(String.valueOf(roomID)).getPlayers().contains(userId)) {
            //APIGameHandler apiGameHandler = rooms.get(roomID);

            APIGameHandler apiGameHandler = apiGameHandlerRepository.findByRoomID(String.valueOf(roomID));

            beanFactory.autowireBean(apiGameHandler);


            return ResponseEntity.status(HttpStatus.OK).body(new GameStatusResponse(
                    apiGameHandler.getPlayerState(userId),
                    apiGameHandler.getRiskMapByState(),
                    apiGameHandler.checkWinner(),
                    apiGameHandler.getIdToColor(),
                    ""
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GameStatusResponse("Room not found!"));
        }
    }


    /**
     * This method handle the post request of /place/unit.
     *
     * @param placeUnitRequest A deserialize json object contains some Unit object.
     * @return ResponseEntity which contains the http code to indicate the result.
     */
    @PostMapping("/place/unit")
    public ResponseEntity<PlaceUnitResponse> placeUnit(@Valid @RequestBody PlaceUnitRequest placeUnitRequest) {
        Long userId = getUserId();
        Long roomID = placeUnitRequest.getRoomID();
        //APIGameHandler currGame = rooms.get(roomID);
        APIGameHandler currGame = apiGameHandlerRepository.findByRoomID(String.valueOf(roomID));
        beanFactory.autowireBean(currGame);
        if (currGame == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PlaceUnitResponse("Cannot find room " + roomID + "!"));
        }

        String place_error_message = currGame.tryPlaceUnit(userId, placeUnitRequest.getUnitPlaceOrders());

        if (place_error_message != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PlaceUnitResponse(place_error_message));
        } else {
            apiGameHandlerRepository.save(currGame);
            return ResponseEntity.status(HttpStatus.OK).body(new PlaceUnitResponse("Successfully placed unit into map."));
        }

    }

    /**
     * This method handle the post request of /place/order.
     *
     * @param placeOrderRequest A deserialize json object contains some Order object.
     * @return ResponseEntity which contains the http code to indicate the result.
     */
    @PostMapping("/place/order")
    public ResponseEntity<PlaceUnitResponse> placeOrder(@Valid @RequestBody PlaceOrderRequest placeOrderRequest) {
        Long userId = getUserId();
        Long roomID = placeOrderRequest.getRoomID();

        APIGameHandler currGame = apiGameHandlerRepository.findByRoomID(String.valueOf(roomID));
        beanFactory.autowireBean(currGame);

        if (currGame == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PlaceUnitResponse("Cannot find room " + roomID + "!"));
        }

        String preprocess_message = currGame.tryPreProcessOrder(userId, placeOrderRequest.getOrders());

        if (preprocess_message != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PlaceUnitResponse(preprocess_message));
        } else {
            apiGameHandlerRepository.save(currGame);
            return ResponseEntity.status(HttpStatus.OK).body(new PlaceUnitResponse("Successfully placed order into map!"));
        }

    }

    /**
     * This method handle the get request of /rooms/available.
     *
     * @return ResponseEntity which contains the http code to indicate the result and the available rooms.
     */
    @Transactional
    @GetMapping("/rooms/available")
    public ResponseEntity<RoomsAvailableResponse> allRooms(@RequestParam Boolean competitive) {
        Long userId = getUserId();
//        System.out.println("all room elo "+userRepository.findByid(userId).orElse(null).getElo());

        List<APIGameHandler> res = apiGameHandlerRepository.findAll().stream()
                .filter(e -> (competitive.equals(e.isCompetitive()) && (State.WaitingToStartState.name().equals(e.getCurrentState())
                        && !e.getPlayers().contains(userId))))
                .collect(Collectors.toList());

        Collections.sort(res, new APIGameHandlerComparator(userRepository.findByid(userId).orElse(null).getElo()));
        return ResponseEntity.status(HttpStatus.OK).body(new RoomsAvailableResponse(res));
    }


    /**
     * This method handle the get request of /rooms/joined.
     *
     * @return ResponseEntity which contains the http code to indicate the result and the joined rooms.
     */
    @GetMapping("/rooms/joined")
    public ResponseEntity<RoomsAvailableResponse> joinedRooms() {
        Long userId = getUserId();
//        System.out.println("Join room elo "+userRepository.findByid(userId).orElse(null).getElo());

        List<APIGameHandler> res = apiGameHandlerRepository.findAll().stream()
                .filter(e -> e.getPlayers().contains(userId)).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(new RoomsAvailableResponse(res));
    }

    @GetMapping("/userElo")
    public ResponseEntity<Long> getUserRank(){
        Long userId = getUserId();
        Long currElo = userRepository.findByid(userId).orElse(null).getElo();
        return ResponseEntity.status(HttpStatus.OK).body(currElo);
    }


}