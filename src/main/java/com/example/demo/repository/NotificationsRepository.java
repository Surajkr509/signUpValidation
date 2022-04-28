package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Notifications;

public interface NotificationsRepository extends JpaRepository<Notifications, Long>{

	Long countofUnRead(int i);

}
