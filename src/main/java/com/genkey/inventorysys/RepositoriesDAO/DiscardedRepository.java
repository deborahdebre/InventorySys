package com.genkey.inventorysys.RepositoriesDAO;

import com.genkey.inventorysys.Model.DiscardedAssets;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DiscardedRepository extends CrudRepository<DiscardedAssets,Integer> {
    @Override
    List<DiscardedAssets> findAll();

}
