package com.group09.sushix_api.repository;

import com.group09.sushix_api.entity.OnlineAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OnlineAccessRepository extends JpaRepository<OnlineAccess,String> {
    Optional<OnlineAccess> findByCustomer_CustId(Integer custId);
}
