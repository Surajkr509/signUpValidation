package com.example.demo.controller;

import static com.example.demo.utils.Constants.requestSuccess;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.bean.ResultDTO;
import com.example.demo.model.Student;
import com.example.demo.model.UserExcelExporter;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.AuthService;
import com.example.demo.utils.BeanValidator;
import com.example.demo.utils.Constants;

@Controller
@RequestMapping("/auth")

public class AuthController {
	

	@Autowired
	StudentRepository studentRepository;
	@Autowired
	AuthService authService;
	@Autowired
	BeanValidator beanValidator;

	
	@PostMapping("/signupStudent")
	public ResponseEntity<?> signUp(@ModelAttribute Student student){
		System.err.println("SignUp Controller::;");
		ResultDTO<?> responsePacket = null;
		try {
			ArrayList<String> errorList=beanValidator.userValidate(student);
			if(errorList.size()!=0) {
				ResultDTO<ArrayList<String>> errorPacket = new ResultDTO<>(false, errorList, Constants.invalidData);
				return new ResponseEntity<>(errorPacket,HttpStatus.BAD_REQUEST);
			}
			if(studentRepository.existsByEmail(student.getEmail()) || studentRepository.existsByMobileNo(student.getMobileNo())) {
				responsePacket=new ResultDTO<>(false,null,"Student Already Exist by email or mobileNo");
				return new ResponseEntity<>(responsePacket,HttpStatus.BAD_REQUEST);
			} else {
				responsePacket=new ResultDTO<>(true,authService.signUp(student), requestSuccess);
				return new ResponseEntity<>(responsePacket,HttpStatus.OK);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			responsePacket=new ResultDTO<>(false,null,e.getMessage());
			return new ResponseEntity<>(responsePacket,HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/signIn")
	public String login(Student student) {
		return "signIn";
	}
	
	@PostMapping("/signIn")
	public ModelAndView userSignIn(@Valid Student student, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			System.err.println("::: SignIn ::: ");
			String email = student.getEmail();
			if (authService.checkEmail(email) == false) {
				bindingResult.rejectValue("email", "errors.student.email", "Enter email address does not exist");
			}
			String password = student.getPassword();
			if (authService.checkPassword(password) == false) {
				bindingResult.rejectValue("password", "errors.student.password", "Please Enter correct password");
			}
			authService.userlogin(student);
			modelAndView.setViewName("/home");
			return modelAndView;
		}catch (Exception e) {
		e.printStackTrace();
		
		}
		modelAndView.setViewName("/signIn");
		return modelAndView;
	}
	
	@GetMapping("/checkEmail/{email}")
	public ResponseEntity<?> checkEmail(@PathVariable String email) {
		System.out.println("email" + email);
		return ResponseEntity.ok(authService.checkEmail(email));
	}

	@GetMapping("/checkPassword/{password}")
	public ResponseEntity<?> checkPassword(@PathVariable String password) {
		System.out.println("password" + password);
		return ResponseEntity.ok(authService.checkPassword(password));
	}
	
	@PostMapping("/forgotPassword/{username}")
	public ResponseEntity<?> forgotPassword(@PathVariable ("username") String username){
		ResultDTO<?> responsePacket=null;
		try {
			Student email=studentRepository.findByUsername(username);
			if(email!=null) {
				responsePacket=new ResultDTO<>(true,authService.forgotPassword(email),"OTP sent Successfully");
				return new ResponseEntity<>(responsePacket,HttpStatus.OK);
			} else {
				responsePacket=new ResultDTO<>(false,"Student not exist");
				return new ResponseEntity<>(responsePacket,HttpStatus.BAD_REQUEST);
			} 
		}catch (Exception e) {
			e.printStackTrace();
			responsePacket=new ResultDTO<>(false,e.getMessage());
			return new ResponseEntity<>(responsePacket,HttpStatus.BAD_REQUEST);
		}
	}
	
	  @GetMapping("/users/export/excel")
	    public void exportToExcel(HttpServletResponse response) throws IOException {
	        response.setContentType("application/octet-stream");
	        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	        String currentDateTime = dateFormatter.format(new Date());
	         
	        String headerKey = "Content-Disposition";
	        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
	        response.setHeader(headerKey, headerValue);
	         
	        List<Student> listStudents = studentRepository.findAll();
	         
	        UserExcelExporter excelExporter = new UserExcelExporter(listStudents);
	         
	        excelExporter.export(response);    
	    } 
}
