package com.uade.tpo.grupo7.marketplace.order.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.grupo7.marketplace.order.entity.PurchaseOrder;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    List<PurchaseOrder> findByBuyerIdOrderByCreatedAtDesc(Long buyerId);

    Optional<PurchaseOrder> findByIdAndBuyerId(Long id, Long buyerId);
}
