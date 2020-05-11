package com.chloe.payment.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferDataRepository extends JpaRepository<TransferDataInfo, Integer> {}