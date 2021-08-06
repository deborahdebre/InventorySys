package com.genkey.inventorysys.Model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
// Contains details of employees
@Data public class EmployeeDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int staffId;

    private String fName;
    private String lName;

    private String jobDescription;
    private String password;

    @ManyToOne
    @JoinColumn(name="role_id")
    private RoleDetails role; // set default role to 1 which is regular user

    private String email; //user name which is their email for now
}
