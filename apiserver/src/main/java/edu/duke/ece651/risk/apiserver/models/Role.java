package edu.duke.ece651.risk.apiserver.models;

import javax.persistence.*;
@Entity
@Table(name = "roles")
/**
 * Role class to be used for authentication
 */
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    /**
     * default constructor
     */
    public Role() {
    }

    /**
     * constructor to create role with an ERole
     * @param name
     */
    public Role(ERole name) {
        this.name = name;
    }

    /**
     * get ID
     * @return
     */
    public Integer getId() {
        return id;
    }

    /**
     * set ID
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * get name
     * @return
     */
    public ERole getName() {
        return name;
    }

    /**
     * set name
     * @param name
     */
    public void setName(ERole name) {
        this.name = name;
    }
}
