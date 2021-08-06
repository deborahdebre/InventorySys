package com.genkey.inventorysys.RepositoriesDAO;

import com.genkey.inventorysys.Model.LoginDetails;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface LoginRepository extends CrudRepository<LoginDetails,Long> {

    @Override
    List<LoginDetails> findAll();

}
