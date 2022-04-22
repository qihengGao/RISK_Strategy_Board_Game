package edu.duke.ece651.risk.apiserver.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece651.risk.apiserver.APIGameHandler;
import edu.duke.ece651.risk.apiserver.controllers.GameController;
import edu.duke.ece651.risk.apiserver.payload.request.CreateRoomRequest;
import edu.duke.ece651.risk.apiserver.payload.request.JoinRoomRequest;
import edu.duke.ece651.risk.apiserver.payload.request.PlaceOrderRequest;
import edu.duke.ece651.risk.apiserver.payload.request.PlaceUnitRequest;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@SpringBootTest
////@RunWith(SpringJUnit4ClassRunner.class)
//@AutoConfigureMockMvc(addFilters = false)
//@WithUserDetails("test")
//public class GameControllerTest {
//
//    private MockMvc mockMvc;
//
//    @InjectMocks
//    private GameController gameController;
//
////    @Mock
////    private HashMap<Long, APIGameHandler> rooms;
//
//    @BeforeEach
//    public void createMockMvc(){
//        MockitoAnnotations.initMocks(this);
//        this.mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
//    }
//
//    private String generateCreateRoomRequestString(int roomSize) throws JsonProcessingException {
//        CreateRoomRequest request = new CreateRoomRequest();
//        request.setRoomSize(roomSize);
//        String requestStr = new ObjectMapper().writeValueAsString(request);
//        return requestStr;
//    }
//
//    private String generateJoinRoomRequestString(int roomId) throws JsonProcessingException {
//        JoinRoomRequest request = new JoinRoomRequest();
//        request.setRoomID(roomId);
//        String requestStr = new ObjectMapper().writeValueAsString(request);
//        return requestStr;
//    }
//
//    @Test
//    public void test_createAndJoinRoom() throws Exception {
//        // success: create and join room
//        this.mockMvc.perform(
//            post("/api/game/createRoom")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(this.generateCreateRoomRequestString(3))
//        ).andExpect(status().isOk())
//        .andDo( result -> this.mockMvc.perform(
//            post("/api/game/joinRoom")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(this.generateJoinRoomRequestString(0))
//            )
//        );
//
//        // error: join a non-existing room
//        this.mockMvc.perform(
//            post("/api/game/joinRoom")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(this.generateJoinRoomRequestString(2))
//        ).andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void test_joinRoom(){
//        GameController controller = mock(GameController.class);
//        JoinRoomRequest request = mock(JoinRoomRequest.class);
//        HashMap<Long, APIGameHandler> rooms = mock(HashMap.class);
//        APIGameHandler handler = mock(APIGameHandler.class);
//        ReflectionTestUtils.setField(controller, "rooms", rooms);
//
//        doCallRealMethod().when(controller).joinRoom(request);
//        doReturn(handler).when(rooms).get(any());
//        doReturn(true).when(handler).tryAddPlayer(anyLong());
//
//        ResponseEntity entity = controller.joinRoom(request);
//        assertEquals(200, entity.getStatusCodeValue());
//    }
//
//    @Test
//    public void test_gameStatus() throws Exception {
//        // error: room does not exist
//        this.mockMvc.perform(
//                get("/api/game/gameStatus").param("roomID", "0")
//        ).andExpect(status().isBadRequest());
//
//        // success
//        this.mockMvc.perform(
//            post("/api/game/createRoom")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(this.generateCreateRoomRequestString(2))
//        ).andExpect(status().isOk())
//        .andDo( result -> this.mockMvc.perform(
//            get("/api/game/gameStatus").param("roomID", "0")).andExpect(status().isOk())
//        );
//    }
//
//    @Test
//    public void test_placeUnit(){
//        GameController controller = mock(GameController.class);
//        PlaceUnitRequest request = mock(PlaceUnitRequest.class);
//        APIGameHandler handler = mock(APIGameHandler.class);
//        HashMap<Long, APIGameHandler> rooms = mock(HashMap.class);
//        ReflectionTestUtils.setField(controller, "rooms", rooms);
//
//        doCallRealMethod().when(controller).placeUnit(request);
//        doReturn(0L).when(controller).getUserId();
//        doReturn(0L).when(request).getRoomID();
//        doReturn(null).when(rooms).get(anyLong());
//
//        ResponseEntity entity = controller.placeUnit(request);
//        assertEquals(400, entity.getStatusCodeValue());
//
//        doReturn(handler).when(rooms).get(anyLong());
//        doReturn(null).when(handler).tryPlaceUnit(anyLong(), any());
//        entity = controller.placeUnit(request);
//        assertEquals(200, entity.getStatusCodeValue());
//
//        doReturn("xxx").when(handler).tryPlaceUnit(anyLong(), any());
//        entity = controller.placeUnit(request);
//        assertEquals(400, entity.getStatusCodeValue());
//    }
//
//    @Test
//    public void test_placeOrder(){
//        GameController controller = mock(GameController.class);
//        PlaceOrderRequest request = mock(PlaceOrderRequest.class);
//        APIGameHandler handler = mock(APIGameHandler.class);
//        HashMap<Long, APIGameHandler> rooms = mock(HashMap.class);
//        ReflectionTestUtils.setField(controller, "rooms", rooms);
//
//        doCallRealMethod().when(controller).placeOrder(request);
//        doReturn(0L).when(controller).getUserId();
//        doReturn(0L).when(request).getRoomID();
//        doReturn(null).when(rooms).get(anyLong());
//
//        ResponseEntity entity = controller.placeOrder(request);
//        assertEquals(400, entity.getStatusCodeValue());
//
//        doReturn(handler).when(rooms).get(anyLong());
//        doReturn(null).when(handler).tryPreProcessOrder(anyLong(), any());
//        entity = controller.placeOrder(request);
//        assertEquals(200, entity.getStatusCodeValue());
//
//        doReturn("xxx").when(handler).tryPreProcessOrder(anyLong(), any());
//        entity = controller.placeOrder(request);
//        assertEquals(400, entity.getStatusCodeValue());
//    }
//
//    @Test
//    public void test_allRooms() throws Exception {
//        this.mockMvc.perform(
//            get("/api/game/rooms/available")
//            .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }
//
//    @Test
//    public void test_joinedRooms() throws Exception {
//        this.mockMvc.perform(
//                get("/api/game/rooms/joined")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }
//}
