package com.example.springskillaryback.repository;

import com.example.springskillaryback.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Byte> { }
