package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.Role;
import com.example.demo.model.Student;
import com.example.demo.repository.RoleRepository;

@RestController
@RequestMapping("/admin")

public class AdminController {
		
	@Autowired
	RoleRepository roleRepository;
	
	@PostMapping("/addRole")
	public ResponseEntity<?> addRole(@RequestBody Role role){
		System.err.println("::::::::::::AdminController(ROLE)::::::::::::");
		return ResponseEntity.ok(roleRepository.save(role));
	}
	
	@GetMapping("/home")
	public ModelAndView home(Student student) {
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.setViewName("/home");
		return modelAndView;
	}
}


