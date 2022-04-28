package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.service.NotificationsService;

@Controller
@RequestMapping("/admin")

public class NotificationsController {
	
	@Autowired
	NotificationsService notificationsService;
	
	
	@GetMapping
	public Long getAllUnReadNotificationsCount() {
		return notificationsService.countAllUnReadNotifications();
	}
	
	

}
