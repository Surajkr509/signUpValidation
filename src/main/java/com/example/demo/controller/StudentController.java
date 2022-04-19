package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.Student;
import com.example.demo.service.StudentService;
import com.example.demo.utils.Constants;

@Controller
@RequestMapping("/student")

public class StudentController {

	@Autowired
	StudentService studentService;

	@PostMapping
	public ModelAndView addStudent(Student student, @RequestParam("image") 
	MultipartFile multipartFile) throws IOException {
		 String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
	        student.setPhotos(fileName);
		student.setPassword(Constants.getRandomPassword());
		studentService.add(student, multipartFile);
		
		return this.getStudents(null, null, null, null);
	}

//	@PostMapping("/add")
//	public ResponseEntity<?> add(Student student) {
//		System.out.println("ADD controller::::");
//		student.setPassword(Constants.getRandomPassword());
//		return ResponseEntity.ok(studentService.add(student));
//	}

	@GetMapping("/new")
	public String goToAddStudent() {
		return "/add-student";

	}
//	@GetMapping
//	public List<Student> getAll() {
//		return studentService.findAll();
//	}

	@GetMapping("/{id}")
	public Optional<Student> getById(@PathVariable Long id) {
		return studentService.getById(id);
	}

	@PutMapping("/{id}")
	public String update(@PathVariable Long id, @RequestBody Student student) {
		student.setId(id);
		return studentService.update(student);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		studentService.delete(id);
	}

	@GetMapping
	public ModelAndView getStudents(@RequestParam(required = false) Integer pageNumber,
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String sortDirection) {

		if (pageNumber == null)
			pageNumber = 0;
		if (pageSize == null)
			pageSize = 10;

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/students");

		Page<Student> studentSearchResult = studentService.getAll(pageNumber, pageSize, sortBy, sortDirection);
		List<Student> studentData = studentSearchResult.getContent();
		modelAndView.addObject("studentlist", studentData);
		modelAndView.addObject("pageNumber", studentSearchResult.getNumber());
		modelAndView.addObject("pageSize", pageSize);
		modelAndView.addObject("sortBy", sortBy);
		modelAndView.addObject("sortDirection", sortDirection);
		List<Integer> pages = new ArrayList<>();
		List<Integer> pageSizes = new ArrayList<>();
		pageSizes.add(5);
		pageSizes.add(10);
		pageSizes.add(15);
		pageSizes.add(20);

		for (int i = 0; i < studentSearchResult.getTotalPages(); i++) {
			pages.add(i);
		}
		modelAndView.addObject("pages", pages);
		modelAndView.addObject("pageSizes", pageSizes);
		modelAndView.addObject("totalPages", studentSearchResult.getTotalPages());
		modelAndView.addObject("totalElements", studentSearchResult.getTotalElements());
		return modelAndView;
	}

}