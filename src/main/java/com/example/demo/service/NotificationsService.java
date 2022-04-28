package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.NotificationsRepository;

@Service
public class NotificationsService {

	@Autowired
	NotificationsRepository notificationsRepository;
	
	public Long countAllUnReadNotifications() {
		return notificationsRepository.countofUnRead(0);
	}

}
