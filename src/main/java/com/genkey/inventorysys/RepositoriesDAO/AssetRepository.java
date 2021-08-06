package com.genkey.inventorysys.RepositoriesDAO;

import com.genkey.inventorysys.Model.AssetDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface AssetRepository extends CrudRepository<AssetDetails,Integer> {
    @Override
    List<AssetDetails> findAll();

}
