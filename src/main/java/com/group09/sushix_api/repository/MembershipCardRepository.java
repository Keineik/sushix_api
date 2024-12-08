package com.group09.sushix_api.repository;

import com.group09.sushix_api.entity.MembershipCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipCardRepository extends JpaRepository<MembershipCard, Integer> {
}
