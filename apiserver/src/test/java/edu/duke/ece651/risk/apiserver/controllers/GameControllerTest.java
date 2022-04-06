package edu.duke.ece651.risk.apiserver.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece651.risk.apiserver.controllers.GameController;
import edu.duke.ece651.risk.apiserver.payload.request.CreateRoomRequest;
import edu.duke.ece651.risk.apiserver.payload.request.JoinRoomRequest;
import edu.duke.ece651.risk.apiserver.payload.request.PlaceOrderRequest;
import edu.duke.ece651.risk.apiserver.payload.request.PlaceUnitRequest;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
//@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc(addFilters = false)
@WithUserDetails("test")
public class GameControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private GameController gameController;

    @BeforeEach
    public void createMockMvc(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
    }

    private String generateCreateRoomRequestString(int roomSize) throws JsonProcessingException {
        CreateRoomRequest request = new CreateRoomRequest();
        request.setRoomSize(roomSize);
        String requestStr = new ObjectMapper().writeValueAsString(request);
        return requestStr;
    }

    private String generateJoinRoomRequestString(int roomId) throws JsonProcessingException {
        JoinRoomRequest request = new JoinRoomRequest();
        request.setRoomID(roomId);
        String requestStr = new ObjectMapper().writeValueAsString(request);
        return requestStr;
    }

    @Test
    public void test_createAndJoinRoom() throws Exception {
        // success: create and join room
        this.mockMvc.perform(
            post("/api/game/createRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.generateCreateRoomRequestString(3))
        ).andExpect(status().isOk())
        .andDo( result -> this.mockMvc.perform(
            post("/api/game/joinRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.generateJoinRoomRequestString(0))
            )
        );

        // error: join a non-existing room
        this.mockMvc.perform(
            post("/api/game/joinRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.generateJoinRoomRequestString(2))
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void test_gameStatus() throws Exception {
        // error: room does not exist
        this.mockMvc.perform(
                get("/api/game/gameStatus").param("roomID", "0")
        ).andExpect(status().isBadRequest());

        // success
        this.mockMvc.perform(
            post("/api/game/createRoom")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.generateCreateRoomRequestString(2))
        ).andExpect(status().isOk())
        .andDo( result -> this.mockMvc.perform(
            get("/api/game/gameStatus").param("roomID", "0")).andExpect(status().isOk())
        );
    }

    @Test
    public void test_placeUnit() throws Exception {
        PlaceUnitRequest request = new PlaceUnitRequest();
        String requestStr = new ObjectMapper().writeValueAsString(request);

        // error: room and order not exist
        this.mockMvc.perform(
            post("/api/game/place/unit")
            .contentType(MediaType.APPLICATION_JSON)
                    .content(requestStr)
        ).andExpect(status().isBadRequest());

        // success

    }

    @Test
    public void test_placeOrder() throws Exception {
        PlaceOrderRequest request = new PlaceOrderRequest();
        String requestStr = new ObjectMapper().writeValueAsString(request);

        // error: room and order not exist
        this.mockMvc.perform(
                post("/api/game/place/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestStr)
        ).andExpect(status().isBadRequest());

        // success

    }

    @Test
    public void test_allRooms() throws Exception {
        this.mockMvc.perform(
            get("/api/game/rooms/available")
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void test_joinedRooms() throws Exception {
        this.mockMvc.perform(
                get("/api/game/rooms/joined")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
}
