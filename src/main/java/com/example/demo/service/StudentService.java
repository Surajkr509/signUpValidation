package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Role;
import com.example.demo.model.Student;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.utils.Constants;

@Service
public class StudentService {
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	public String add(Student student) {
		 studentRepository.save(student);
		 return null;
	}

	public List<Student> findAll() {
		return studentRepository.findAll();
	}

	public Optional<Student> getById(Long id) {
		return studentRepository.findById(id);
	}

	public String update(Student student) {
		System.err.println("Update:::");
		Optional<Student> existStudent=studentRepository.findById(student.getId());
		System.out.println("Student ID" +student);
		if(existStudent.isPresent()) {
			Student data=existStudent.get();
			data.setName(student.getName());
			data.setStandard(student.getStandard());
			data.setEmail(student.getEmail());
			data.setMobileNo(student.getMobileNo());
			studentRepository.save(data);
			return "Student Updated...";
		} else {
			return "Student not found by ID";
		}
		
	}

	public void delete(Long id) {
		Optional<Student> existStudent=studentRepository.findById(id);
		if(existStudent.isPresent())
			studentRepository.deleteById(id);
	}

	public Object signUp(Student student) {
	Role role = roleRepository.findByRole("STUDENT");
	if(role!=null) {
		String password=Constants.getRandomPassword();
		student.setRoleId(role);
		student.setUsername(student.getEmail());
		student.setPassword(student.getPassword());
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

}

