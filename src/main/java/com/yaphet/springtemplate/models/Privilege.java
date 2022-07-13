package com.yaphet.springtemplate.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "privilege",
        uniqueConstraints = @UniqueConstraint(
                name = "privilege_unique",
                columnNames = "privilege_name")
)
public class Privilege {
    @Id
    @SequenceGenerator(
            name = "privilege_sequence",
            sequenceName = "privilege_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "privilege_sequence"
    )
    @Column(name = "privilege_id")
    private Long id;
    @Column(name = "privilege_name")
    private String privilegeName;
    public Privilege(String privilegeName){
        this.privilegeName = privilegeName;
    }

}
