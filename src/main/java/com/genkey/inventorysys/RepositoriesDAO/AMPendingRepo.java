package com.genkey.inventorysys.RepositoriesDAO;

import com.genkey.inventorysys.Model.AMPending;
import com.genkey.inventorysys.Model.CheckedOutAssets;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AMPendingRepo extends CrudRepository<AMPending,Integer> {
    @Override
    List<AMPending> findAll();
}
