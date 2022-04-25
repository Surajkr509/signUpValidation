package com.example.demo.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.bean.PaginationDTO;
import com.example.demo.model.FileUploadUtil;
import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;

@Service
public class StudentService {

	@Autowired
	StudentRepository studentRepository;

	public Object add(Student student, MultipartFile multipartFile) throws IOException {
		Student savedStudent = studentRepository.save(student);
		String uploadDir = "src/main/resources/static/images/" + savedStudent.getId();
		FileUploadUtil.saveFile(uploadDir, student.getPhotos(), multipartFile);
		return null;
	}

//	trying validation
	public Object adds(Student student, MultipartFile multipartFile) throws IOException {
		Student savedStudent = studentRepository.save(student);
		String uploadDir = "src/main/resources/static/images/" + savedStudent.getId();
		FileUploadUtil.saveFile(uploadDir, student.getPhotos(), multipartFile);
		return null;
	}

	public List<Student> findAll() {
		return studentRepository.findAll();
	}

	public Student getById(Long id) {
		Optional<Student> student= studentRepository.findById(id);
		return student.isPresent()?student.get():null;
	}

	public String update(Student student,MultipartFile multipartFile ) {
		System.err.println("Update:::");
		Optional<Student> existStudent = studentRepository.findById(student.getId());
		System.out.println("Student ID" + student);
		if (existStudent.isPresent()) {
			Student data = existStudent.get();
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
		Optional<Student> existStudent = studentRepository.findById(id);
		if (existStudent.isPresent())
			studentRepository.deleteById(id);
	}

	public Page<Student> getAll(int pageNumber, int pageSize, String sortBy, String sortDirection) {

		if (pageNumber == 0 || pageNumber < 0)
			pageNumber = 0;
		if (pageSize == 0 || pageSize < 0)
			pageSize = 10;
		if (sortBy == null || sortBy.trim().equals(""))
			sortBy = "id";
		Sort sort = Sort.by(Direction.ASC, sortBy);
		if (sortDirection == null || sortDirection.trim().equals("") || sortDirection.trim().equals("desc"))
			sort = Sort.by(Direction.DESC, sortBy);

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		return studentRepository.findAll(pageable);

	}

	public boolean checkName(String name) {
		Optional<Student> existStudent = studentRepository.findByName(name);
		return existStudent.isPresent();
	}

	public boolean checkEmail(String email) {
		Boolean existStudent = studentRepository.existsByEmail(email);
		return existStudent;
	}

	public boolean checkMob(String mobileNo) {
		boolean existMob = studentRepository.existsByMobileNo(mobileNo);
		return existMob;
	}

}
