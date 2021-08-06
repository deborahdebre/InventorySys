package com.genkey.inventorysys.Model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class OldEmployees {
    @Id
    private int empId;
    private String firstName;
    private String lastName;
    private String jobDescription;
    private String email;
}
