package com.connect.refugee.connectrefugee.models;

import java.io.Serializable;
import javax.persistence.*;
import lombok.*;

@Entity()
@Getter()
@Builder()
@Setter()
@AllArgsConstructor()
public class User implements Serializable {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    Long id;
    
    @Column(unique = true)
    private String username;
    private String password;

    public User() {
        this.username = null;
        this.password = null;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
