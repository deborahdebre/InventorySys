package com.genkey.inventorysys.RepositoriesDAO;

import com.genkey.inventorysys.Model.CheckedOutAssets;
import com.genkey.inventorysys.Model.EmployeeDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CheckedOutRepository extends CrudRepository<CheckedOutAssets,Integer>{
    @Override
    List<CheckedOutAssets> findAll();
}
