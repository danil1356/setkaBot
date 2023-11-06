package com.example.setkabot.data.Repository;

import com.example.setkabot.data.Entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentsRepository extends JpaRepository<Payments, Long> {

}