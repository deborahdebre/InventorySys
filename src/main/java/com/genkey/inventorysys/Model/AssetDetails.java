package com.genkey.inventorysys.Model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class AssetDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int assetId;

    @ManyToOne
    @JoinColumn(name = "status")
    private Status status; //FK

    @ManyToOne
    @JoinColumn(name="location")
    private Location location; //FK

    @ManyToOne
    @JoinColumn(name = "category")
    private Category category; //FK

    private String serialNum;
    private String brand;
    private String condition;



}
