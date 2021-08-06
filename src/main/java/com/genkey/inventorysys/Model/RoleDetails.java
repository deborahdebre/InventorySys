package com.genkey.inventorysys.Model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class RoleDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int roleId;
    private String role; //admin,asset manager, regular user

    @OneToMany(mappedBy = "role")
    private List<EmployeeDetails> employee;

    @OneToMany(mappedBy = "role_")
    private List<LoginDetails> login;

}
