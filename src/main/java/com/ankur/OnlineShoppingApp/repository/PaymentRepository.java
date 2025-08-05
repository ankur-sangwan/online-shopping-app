package com.ankur.OnlineShoppingApp.repository;

import com.ankur.OnlineShoppingApp.model.Order;
import com.ankur.OnlineShoppingApp.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    @Query("SELECT p.order FROM Payment p WHERE p.paymentId = :paymentId")
    Order findOrderByPaymentId(@Param("paymentId") String paymentId);

    List<Payment> findByOrderId(int id);
}
