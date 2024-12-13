package com.group09.sushix_api.repository;

import com.group09.sushix_api.entity.CustomerRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRatingRepository extends JpaRepository<CustomerRating, Integer> {
}
