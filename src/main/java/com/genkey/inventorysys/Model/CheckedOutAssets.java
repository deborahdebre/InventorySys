package com.genkey.inventorysys.Model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class CheckedOutAssets {
    @Id
    private int checkedAssetID;
    private int employeeID;
    private String EmpName;
    private String job_title;
    private String assetNum;
    private String assetCategory;
    private String assetBrand;
    private String assetCondition;
}
