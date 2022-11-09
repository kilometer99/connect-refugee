package com.connect.refugee.connectrefugee.models;

import java.io.Serializable;
import javax.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.*;

@Entity()
@Getter()
@Builder()
@Setter()
@AllArgsConstructor()
@RequiredArgsConstructor()
@EntityListeners(AuditingEntityListener.class)
public class Post implements Serializable{
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    Long id;
    
    @NonNull
    private String title;
    
    @Lob 
    @Column(length=1024)
    @NonNull
    private String description;
    
    @ManyToOne()
    @JoinColumn(name="category_id", nullable=false)
    @NonNull
    private Category category;
    
    @CreatedBy()
    private String createdBy;

    @CreatedDate()
    private String createdDate;
    
    
    public Post() {
        this.title=null;
        this.description=null;
    }
}
