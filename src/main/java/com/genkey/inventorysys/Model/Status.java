package com.genkey.inventorysys.Model;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Status {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int statusId;
    private String status;

    @OneToMany(mappedBy = "status")
    private List<AssetDetails> asset;
}
