package com.example.demo.service;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Role;
import com.example.demo.model.Student;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.utils.Constants;

@Service
public class AuthService {
		@Autowired
		RoleRepository roleRepository;
		@Autowired
		StudentRepository studentRepository;
	
	public Object signUp(Student student) {
		Role role = roleRepository.findByRole("STUDENT");
		if(role!=null) {
			String password=Constants.getRandomPassword();
			student.setRoleId(role);
			student.setUsername(student.getEmail());
			student.setPassword(password);
			student.setCreatedAt(Constants.getDateAndTime());
			student.setUpdatedAt(Constants.getDateAndTime());
			studentRepository.save(student);
			HashMap<String, Object> userData= new HashMap<>();
			userData.put("username",student.getUsername());
			userData.put("password", password);
			return userData;
		} else {
			throw new RuntimeException("Role is not exist");
		}
		}

	
	public Object userlogin(Student student) {
		Optional <Student> existUser = studentRepository.findByEmail(student.getEmail());
		if (existUser.isPresent() && student.getPassword().equals(existUser.get().getPassword())) {
			Student user = existUser.get();
//			Authentication authentication = authenticationManager.authenticate(
//					new UsernamePasswordAuthenticationToken(user.getUsername(), jwtRequest.getUserpassword()));
//			SecurityContextHolder.getContext().setAuthentication(authentication);
//			String jwt = jwtUtil.generateJwtToken(user);
			HashMap<String, Object> userData = new HashMap<>();
			userData.put("username", user.getUsername());
			userData.put("email", user.getEmail());
			userData.put("mobileNo", user.getMobileNo());
//			userData.put("token", jwt);
//			System.err.println("token"+jwt);
			return userData;
		} else {
			throw new RuntimeException("Invalid Credentials");
		}
	}
	public boolean checkEmail(String email) {
		Optional<Student> existStudent = studentRepository.findByEmail(email);
		System.err.println("Email  "+existStudent);
		return existStudent.isPresent();
	}

	public Boolean checkPassword(String password) {
		Optional<Student> existStudent = studentRepository.findByPassword(password);
		return existStudent.isPresent();
	}

	
	public Object forgotPassword(Student user) {
     String otp=Constants.generateOTP();
     System.err.println(":::OTP:::"+otp);
     user.setOtp(otp);
     studentRepository.save(user);
     
     HashMap<String, Object> userData=new HashMap<>();
     userData.put("studentId", user.getId());
     userData.put("otp", otp);
   
     return userData;
	}
}
