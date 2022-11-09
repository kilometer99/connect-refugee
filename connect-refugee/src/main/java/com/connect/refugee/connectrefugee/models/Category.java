package com.connect.refugee.connectrefugee.models;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;
import lombok.*;

@Entity()
@Getter()
@Builder()
@Setter()
@AllArgsConstructor()
@RequiredArgsConstructor()
public class Category implements Serializable {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    Long id;

    @NonNull
    private String name;

    @NonNull
    private String type;

    @OneToMany()
    private Set<Post> posts;

    public Category() {
        this.name = null;
        this.type = null;
    }
}
