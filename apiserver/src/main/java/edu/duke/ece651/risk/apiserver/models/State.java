package edu.duke.ece651.risk.apiserver.models;

import javax.persistence.*;

@Entity
@Table(name = "game_states")
/**
 * Role class to be used for authentication
 */
public class State {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EState name;

    /**
     * default constructor
     */
    public State() {
    }

    /**
     * constructor to create role with an ERole
     *
     * @param name
     */
    public State(EState name) {
        this.name = name;
    }

    /**
     * get ID
     *
     * @return
     */
    public Integer getId() {
        return id;
    }

    /**
     * set ID
     *
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * get name
     *
     * @return
     */
    public EState getName() {
        return name;
    }

    /**
     * set name
     *
     * @param name
     */
    public void setName(EState name) {
        this.name = name;
    }
}
