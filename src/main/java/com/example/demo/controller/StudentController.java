package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.bean.ResultDTO;
import com.example.demo.model.Student;
import com.example.demo.service.StudentService;
import com.example.demo.utils.BeanValidator;
import com.example.demo.utils.Constants;

@Controller
@RequestMapping("/student")

public class StudentController {

	@Autowired
	StudentService studentService;

//	@PostMapping
//	public ModelAndView addStudent(Student student, @RequestParam("image") MultipartFile multipartFile)
//			throws IOException {
//
//		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//		student.setPhotos(fileName);
//		student.setPassword(Constants.getRandomPassword());
//		studentService.add(student, multipartFile);
//
//		return this.getStudents(null, null, null, null);
//	}

//	trying validation

	@GetMapping("/addStud")
	public String goToAddStudents(Student student) {
		return "addStud";
	}

	@PostMapping("/addStud")
	public Object addStudents(@Valid Student student, BindingResult bindingResult,
			@RequestParam("image") MultipartFile multipartFile,RedirectAttributes rA) throws IOException {

		try {
			System.err.println("::: AddSTud ::: ");
			String name = student.getName();
			if (name == null) {
				System.err.println("::: Checking Name ::: ");
				bindingResult.rejectValue("name", "errors.student.name", "Enter a valid name");
			}
			
			String mobileNo = student.getMobileNo();
			if (studentService.checkMob(mobileNo) == true) {
				bindingResult.rejectValue("mobileNo", "errors.student.mobileNo", "Enter mobile no. address already exists");
			}
		
			
			String email = student.getEmail();
			if (studentService.checkEmail(email) == true) {
				bindingResult.rejectValue("email", "errors.student.email", "Enter email address already exist");
			}
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			student.setPhotos(fileName);
			student.setPassword(Constants.getRandomPassword());
			studentService.adds(student, multipartFile);
			rA.addAttribute("pageNumber", null);
			rA.addAttribute("pageSize", null);
			rA.addAttribute("sortBy", null);
			rA.addAttribute("sortDirection", null);
			return "redirect:/student/getStudents";
		} catch (Exception e) {

		}
		return "/addStud";
	}


//	@PostMapping("/add")
//	public ResponseEntity<?> add(Student student) {
//		System.out.println("ADD controller::::");
//		student.setPassword(Constants.getRandomPassword());
//		return ResponseEntity.ok(studentService.add(student));
//	}
//
//	@GetMapping("/new")
//	public String goToAddStudent() {
//		return "/add-student";
//
//	}
//	@GetMapping
//	public List<Student> getAll() {
//		return studentService.findAll();
//	}
	
	@GetMapping("/view/{id}")
	public ModelAndView viewStudent(@PathVariable Long id) {

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/viewStud");
		Student studentdata = studentService.getById(id);
		if (studentdata == null)
			return null;

		modelAndView.addObject("studentdetails", studentdata);

		return modelAndView;

	}
	@GetMapping("/{id}")
	public Student getById(@PathVariable Long id) {
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

	@GetMapping("/getStudents")
	public String getStudents(@RequestParam(required = false) Integer pageNumber,
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String sortDirection,Model modelAndView ) {

		if (pageNumber == null)
			pageNumber = 0;
		if (pageSize == null)
			pageSize = 10;


		Page<Student> studentSearchResult = studentService.getAll(pageNumber, pageSize, sortBy, sortDirection);
		List<Student> studentData = studentSearchResult.getContent();
		modelAndView.addAttribute("studentlist", studentData);
		modelAndView.addAttribute("pageNumber", studentSearchResult.getNumber());
		modelAndView.addAttribute("pageSize", pageSize);
		modelAndView.addAttribute("sortBy", sortBy);
		modelAndView.addAttribute("sortDirection", sortDirection);
		List<Integer> pages = new ArrayList<>();
		List<Integer> pageSizes = new ArrayList<>();
		pageSizes.add(5);
		pageSizes.add(10);
		pageSizes.add(15);
		pageSizes.add(20);

		for (int i = 0; i < studentSearchResult.getTotalPages(); i++) {
			pages.add(i);
		}
		modelAndView.addAttribute("pages", pages);
		modelAndView.addAttribute("pageSizes", pageSizes);
		modelAndView.addAttribute("totalPages", studentSearchResult.getTotalPages());
		modelAndView.addAttribute("totalElements", studentSearchResult.getTotalElements());
		return "/students";
	}

}