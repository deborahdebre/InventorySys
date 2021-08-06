package com.genkey.inventorysys.Model;

import lombok.Data;

import javax.persistence.*;

@Entity

// This holds the login details of all staff
// When users login the details they provide will be crosschecked with these

@Data public class LoginDetails {

    @Id
    private String username;

    @ManyToOne
    @JoinColumn(name="role_id")
    private RoleDetails role_; //FK

    private String password;


}
