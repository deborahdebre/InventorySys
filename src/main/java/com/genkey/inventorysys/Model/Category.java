package com.genkey.inventorysys.Model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int categoryId;
    private String category;

    @OneToMany(mappedBy = "category")
    private List<AssetDetails> asset;

}
