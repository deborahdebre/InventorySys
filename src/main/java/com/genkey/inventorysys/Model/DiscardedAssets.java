package com.genkey.inventorysys.Model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class DiscardedAssets {
    @Id
    private int assetId;
    private String status; //FK
    private String location; //FK
    private String category; //FK
    private String serialNum;
    private String brand;
    private String condition;



}
