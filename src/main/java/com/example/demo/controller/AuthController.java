package com.example.demo.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bean.ResultDTO;
import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.StudentService;
import com.example.demo.utils.BeanValidator;
import com.example.demo.utils.Constants;
import static com.example.demo.utils.Constants.requestSuccess;

@RestController
@RequestMapping("/auth")

public class AuthController {
	

	@Autowired
	StudentRepository studentRepository;
	@Autowired
	StudentService studentService;
	@Autowired
	BeanValidator beanValidator;

	
	@PostMapping("/signupStudent")
	public ResponseEntity<?> signUp(@ModelAttribute Student student){
		System.err.println("SignUp Controller::;");
		ResultDTO<?> responsePacket = null;
		try {
			ArrayList<String> errorList=beanValidator.userSignupValidate(student);
			if(errorList.size()==0) {
				ResultDTO<ArrayList<String>> errorPacket = new ResultDTO<>(false, errorList, Constants.invalidData);
				return new ResponseEntity<>(errorPacket,HttpStatus.BAD_REQUEST);
			}
			if(studentRepository.existsByEmail(student.getEmail()) || studentRepository.existsByMobileNo(student.getMobileNo())) {
				responsePacket=new ResultDTO<>(false,null,"Student Already Exist by email or mobileNo");
				return new ResponseEntity<>(responsePacket,HttpStatus.BAD_REQUEST);
			} else {
				responsePacket=new ResultDTO<>(true,studentService.signUp(student), requestSuccess);
				return new ResponseEntity<>(responsePacket,HttpStatus.OK);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			responsePacket=new ResultDTO<>(false,null,e.getMessage());
			return new ResponseEntity<>(responsePacket,HttpStatus.BAD_REQUEST);
		}
	}

}
