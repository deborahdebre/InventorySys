package com.genkey.inventorysys.RepositoriesDAO;

import com.genkey.inventorysys.Model.OldEmployees;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OldEmployeeRepository extends CrudRepository<OldEmployees,Long> {
    @Override
    List<OldEmployees> findAll();
}
