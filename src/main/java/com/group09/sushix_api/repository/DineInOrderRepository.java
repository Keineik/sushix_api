package com.group09.sushix_api.repository;

import com.group09.sushix_api.entity.DineInOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DineInOrderRepository extends JpaRepository<DineInOrder, Integer> {
}
