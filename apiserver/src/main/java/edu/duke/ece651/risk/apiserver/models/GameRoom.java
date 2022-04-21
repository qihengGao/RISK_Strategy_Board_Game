package edu.duke.ece651.risk.apiserver.models;




import edu.duke.ece651.risk.apiserver.models.order.Order;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "GameRoom",
        uniqueConstraints = {

        })
public class GameRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roomID;

    //room related fields
    @Max(5)
    @Min(2)
    private int roomSize;


    private String currentState; //game's current state

    // Using table predefine_colors, shared by all rooms.
//    private ArrayList<Color> predefineColorList = new ArrayList<>();


    @ElementCollection // 1
    @CollectionTable(name = "game_room_player_lists", joinColumns = @JoinColumn(name = "roomid")) // 2
    @Column(name = "players") // 3
    private Set<Long> players; //all joined players


//    private TreeMap<Long, Color> idToColor; //player id to color
//
//
    @ElementCollection // 1
    @CollectionTable(name = "game_room_commitedPlayer_lists", joinColumns = @JoinColumn(name = "roomid")) // 2
    @Column(name = "commitedPlayer") // 3
    private Set<Long> commitedPlayer; //all committed players
//
//

    @ElementCollection
    @CollectionTable(name = "game_room_lostPlayer_lists", joinColumns = @JoinColumn(name = "roomid"))
    @Column(name = "lostPlayer")
    private Set<Long> lostPlayer;
//    private RISKMap riskMap; //the map to play with

    private Integer InitUnitAmountPerPlayer; //Initial Total Unit amount available for each player

    @ElementCollection
    @CollectionTable(name = "game_room_temporaryOrders_lists", joinColumns = @JoinColumn(name = "roomid"))
    @Column(name = "temporaryOrders")
    private List<Order> temporaryOrders; // temporary order holder
//
//
    private long averageElo;
}
