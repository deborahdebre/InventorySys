package com.genkey.inventorysys.RepositoriesDAO;

import com.genkey.inventorysys.Model.EmployeeDetails;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
public interface EmployeeRepository extends CrudRepository<EmployeeDetails,Long> {

    @Override
    List<EmployeeDetails> findAll();
}
