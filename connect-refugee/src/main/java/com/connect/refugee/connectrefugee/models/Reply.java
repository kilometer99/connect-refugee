package com.connect.refugee.connectrefugee.models;

import java.io.Serializable;
import java.util.*;

import lombok.*;
import javax.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity()
@Getter()
@Builder()
@Setter()
@AllArgsConstructor()
@EntityListeners(AuditingEntityListener.class)
public class Reply implements Serializable{
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    Long id;
    
    @Lob 
    @Column(length=1024)
    private String message;
    
    @ManyToOne()
    @JoinColumn(name="post_id", nullable=true)
    private Post post;  
    
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Reply parentReply;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "parentReply")
    @JsonBackReference
    private Collection<Reply> linkedReplies;
    
    @CreatedBy()
    private String repliedBy;

    @CreatedDate()
    private String repliedDate;
    
    public Reply() {
        this.message=null;
    }
}
