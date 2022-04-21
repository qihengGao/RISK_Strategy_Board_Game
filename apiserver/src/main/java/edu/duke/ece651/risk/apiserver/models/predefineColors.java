package edu.duke.ece651.risk.apiserver.models;


import javax.persistence.*;

@Entity
@Table(name = "predefineColors",
        uniqueConstraints = {

        })
public class predefineColors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String colorName;
    private String colorValue;

}
