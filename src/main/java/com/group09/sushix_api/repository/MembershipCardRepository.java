package com.group09.sushix_api.repository;

import com.group09.sushix_api.entity.Customer;
import com.group09.sushix_api.entity.MembershipCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MembershipCardRepository extends JpaRepository<MembershipCard, Integer> {
    Optional<MembershipCard> findByCustomer_CustId(Integer custId);
}
