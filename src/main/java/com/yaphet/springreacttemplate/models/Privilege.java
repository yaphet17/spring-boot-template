package com.yaphet.springreacttemplate.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Privilege {
    @Id
    @SequenceGenerator(name = "privilege_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "privilege_sequence")
    private Long id;
    private String privilegeName;
    public Privilege(String privilegeName){
        this.privilegeName=privilegeName;
    }

}
