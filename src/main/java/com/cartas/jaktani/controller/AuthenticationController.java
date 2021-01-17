package com.cartas.jaktani.controller;

import com.cartas.jaktani.dto.ParamRequestDto;
import com.cartas.jaktani.model.Student;
import com.cartas.jaktani.repository.StudentRepository;
import com.cartas.jaktani.service.CategoryService;
import com.cartas.jaktani.service.VwProductDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/authentication")
public class AuthenticationController {
    @Autowired VwProductDetailsService vwProductDetailsService;
    @Autowired CategoryService categoryService;

    Integer grade = 1;
    public static String staticKey = "Eng000";
    @Autowired
    StudentRepository studentRepository;

    @GetMapping(path = "/student/insert_data")
    public String insertDatas() {
        Student student = new Student(
                staticKey + grade, "John Doe " + grade, Student.Gender.MALE, grade);
        studentRepository.save(student);
        grade++;
        return student.toString();
    }

    @GetMapping(path = "/student/get_all_data")
    public String getAllDatas() {
        List<Student> students = new ArrayList<>();
        studentRepository.findAll().forEach(students::add);
        return students.toString();
    }

    @GetMapping(path = "/student/get_data_by_key")
    public String getAllDatas(@RequestParam(value = "id") String id) {
        System.out.println(staticKey + id);
        Student retrievedStudent = studentRepository.findById(staticKey + id).get();
        return retrievedStudent.toString();
    }

    @PostMapping(path = "/searchAllProduct")
    public Object searchAllProduct(@RequestBody ParamRequestDto paramRequestDto){
        return vwProductDetailsService.searchAllProduct(paramRequestDto);
    }
    
    @GetMapping(path = "/findProductById/{productId}")
    public Object findProductById(@PathVariable(name = "productId") Integer productId) {
        return vwProductDetailsService.findByProductId(productId);
    }
    
    @GetMapping(path = "/allCategory")
    public Object getAllCategory() {
        return categoryService.getAllCategoryName();
    }
    
    @GetMapping(path = "/allProductTypeByProductId/{productId}")
    public Object allProductTypeByProductId(@PathVariable(name = "productId") Integer productId) {
        return vwProductDetailsService.allProductTypeByProductId(productId);
    }

}
