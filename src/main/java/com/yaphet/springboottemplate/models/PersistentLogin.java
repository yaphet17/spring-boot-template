package com.yaphet.springboottemplate.models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "persistent_logins")
public class PersistentLogin {

    @Id
    private String series;
    private String username;
    private String token;
    private Timestamp lastUsed;
}
