package com.example.demo.utils;

import java.util.ArrayList;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.stereotype.Component;

import com.example.demo.model.Student;

@Component
public class BeanValidator {

	public Validator getValidator() {
		return Validation.buildDefaultValidatorFactory().getValidator();
	}

	public ArrayList<String> userValidate(Student reqData) {
		ArrayList<String> arrayList = new ArrayList<>();
		Set<ConstraintViolation<Student>> constraintViolations = getValidator().validate(reqData);
		for (ConstraintViolation<Student> constraintViolation : constraintViolations) {
			if (constraintViolation.getPropertyPath().toString().equals("name")) {
				arrayList.add(constraintViolation.getMessage());
			}
			if (constraintViolation.getPropertyPath().toString().equals("email")) {
				arrayList.add(constraintViolation.getMessage());
			}
			if (constraintViolation.getPropertyPath().toString().equals("mobileNo")) {
				arrayList.add(constraintViolation.getMessage());
			}
			if(constraintViolation.getPropertyPath().toString().equals("standard")) {
				arrayList.add(constraintViolation.getMessage());
			}
		}
		System.err.println("BeanValidation ErrorList:::"+arrayList);
		return arrayList;
	}
	
	
	
	
	
}

