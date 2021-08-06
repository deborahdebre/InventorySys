package com.genkey.inventorysys.RepositoriesDAO;

import com.genkey.inventorysys.Model.PairingDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PairRepository extends CrudRepository<PairingDetails,Integer> {
    @Override
    List<PairingDetails> findAll();
}
