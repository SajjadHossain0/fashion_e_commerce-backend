package com.fashion_e_commerce.User.Entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

        /*registration*/
    private String fullname;
    @Column(unique = true)
    private String email;
    private String number;
    private String password;
    private String role;

        /*login*/
    //private String email;
    //private String password;

        /*contact info*/
    //private String fullname;
    //private String email;
    //private String number;

        /*Shipping info*/
    private String address;
    private String city;
    private String altnumber;
    private String notes;

    // Constructor for setting only the ID
    public User(Long id) {
        this.id = id;
    }
}
