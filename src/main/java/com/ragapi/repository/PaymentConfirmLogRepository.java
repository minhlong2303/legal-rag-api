package com.ragapi.repository;

import com.ragapi.entity.PaymentConfirmLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentConfirmLogRepository extends MongoRepository<PaymentConfirmLog, String> {

    List<PaymentConfirmLog> findByPaymentId(String paymentId);

    List<PaymentConfirmLog> findByAdminId(String adminId);
}
