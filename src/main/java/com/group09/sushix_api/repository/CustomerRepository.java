package com.group09.sushix_api.repository;

import com.group09.sushix_api.entity.Customer;
import com.group09.sushix_api.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @Procedure(procedureName = "usp_FetchCustomers")
    List<Customer> fetchCustomers(
            @Param("Page") Integer page,
            @Param("Limit") Integer limit,
            @Param("SearchTerm") String searchTerm
    );

    @Procedure(procedureName = "usp_FetchCustomers_count")
    Integer countCustomers(
            @Param("SearchTerm") String searchTerm
    );
}
