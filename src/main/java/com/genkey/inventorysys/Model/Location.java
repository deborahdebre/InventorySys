package com.genkey.inventorysys.Model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Location {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int locationId;
    private String location;

    @OneToMany(mappedBy = "location")
    private List<AssetDetails> asset;
}
