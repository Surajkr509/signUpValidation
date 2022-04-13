package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Student;
import com.example.demo.service.StudentService;

@RestController
@RequestMapping("/student")

public class StudentController {
	
	@Autowired
	StudentService studentService;
	
	@PostMapping
	 public ResponseEntity<?> add(@RequestBody Student student){
		System.out.println("ADD controller::::");
		return ResponseEntity.ok(studentService.add(student));
	}
	
	@GetMapping
	public List<Student> getAll(){
		return studentService.findAll();
	}
	
	@GetMapping("/{id}")
	public Optional<Student> getById(@PathVariable Long id){
		return studentService.getById(id);
	}
	
	@PutMapping("/{id}")
	public String update(@PathVariable Long id,@RequestBody Student student) {
		student.setId(id);
		return studentService.update(student);
	}
	
	@DeleteMapping("/{id}")
	public void delete (@PathVariable Long id) {
		studentService.delete(id);
	}
	

}
