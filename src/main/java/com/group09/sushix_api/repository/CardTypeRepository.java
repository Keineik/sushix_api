package com.group09.sushix_api.repository;

import com.group09.sushix_api.entity.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardTypeRepository extends JpaRepository<CardType, Integer> {
}
