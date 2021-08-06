package com.genkey.inventorysys.Model;

import lombok.Data;

import javax.inject.Named;

import javax.persistence.*;

@Data
@Entity

public class PairingDetails {
@Id
@GeneratedValue(strategy= GenerationType.AUTO)
    private int pairID;
    private int assetId;
    private int staffId;
    private String name;
    private String job_title;
    private String assetNum;
    private String assetCategory;
    private String assetBrand;
    private String assetCondition;
}
