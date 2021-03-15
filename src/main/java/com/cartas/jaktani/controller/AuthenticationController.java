package com.cartas.jaktani.controller;

import com.cartas.jaktani.dto.*;
import com.cartas.jaktani.model.Student;
import com.cartas.jaktani.repository.StudentRepository;
import com.cartas.jaktani.service.AddressService;
import com.cartas.jaktani.service.CategoryService;
import com.cartas.jaktani.service.VwProductDetailsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "/authentication")
public class AuthenticationController {
    @Autowired
    VwProductDetailsService vwProductDetailsService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    AddressService addressService;

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

    @GetMapping(path = "/logistic/get_provinces")
    public ProvinceParent getProvinces() throws IOException {
        return addressService.getProvinces();
    }

    @GetMapping(path = "/logistic/get_cities")
    public CityParent getCities(@RequestParam(value = "id") String id) throws IOException {
        return addressService.getCitiesByProvinceId(id);
    }

    @GetMapping(path = "/logistic/rates")
    public CostParent getCities(@RequestParam(value = "origin_city_id") String originCityId,
                                @RequestParam(value = "destination_city_id") String destinationCityId,
                                @RequestParam(value = "weight") Long weight,
                                @RequestParam(value = "courier") String courier) throws IOException {
        return addressService.getCostByCityId(originCityId, destinationCityId, weight, courier);
    }

    @PostMapping(path = "/searchAllProduct")
    public Object searchAllProduct(@RequestBody ParamRequestDto paramRequestDto) {
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

    @GetMapping(path = "/payment/get_all")
    public List<PaymentGatewayDto> getAllPaymentType() {
        List<PaymentGatewayDto> paymentGatewayDtos = new ArrayList<>();
        PaymentGatewayDetail paymentGatewayDetailBCA = new PaymentGatewayDetail("bca");
        PaymentGatewayDetail paymentGatewayDetailBNI = new PaymentGatewayDetail("bni");
        PaymentGatewayDetail paymentGatewayDetailBRI = new PaymentGatewayDetail("bri");
        String paymentType = "bank_transfer";
        PaymentGatewayDto paymentGatewayDtoBCA = new PaymentGatewayDto(paymentType, "bca_image", "Deskripsi pembayaran dengan Virtual Account BCA",
                "Virtual Account BCA", paymentGatewayDetailBCA);
        PaymentGatewayDto paymentGatewayDtoBNI = new PaymentGatewayDto(paymentType, "bni_image", "Deskripsi pembayaran dengan Virtual Account BNI",
                "Virtual Account BNI", paymentGatewayDetailBNI);
        PaymentGatewayDto paymentGatewayDtoBRI = new PaymentGatewayDto(paymentType, "bri_image", "Deskripsi pembayaran dengan Virtual Account BRI",
                "Virtual Account BRI", paymentGatewayDetailBRI);
        PaymentGatewayDto paymentGatewayDtoMandiri = new PaymentGatewayDto("echannel", "mandiri_image", "Deskripsi pembayaran dengan E-Channel Mandiri",
                "E-Channel Mandiri", new PaymentGatewayDetail());
        PaymentGatewayDto paymentGatewayDtoPermata = new PaymentGatewayDto("permata", "permata_image", "Deskripsi pembayaran dengan Virtual Account Permata",
                "Virtual Account Permata", new PaymentGatewayDetail());
        paymentGatewayDtos.add(paymentGatewayDtoBCA);
        paymentGatewayDtos.add(paymentGatewayDtoBNI);
        paymentGatewayDtos.add(paymentGatewayDtoBRI);
        paymentGatewayDtos.add(paymentGatewayDtoMandiri);
        paymentGatewayDtos.add(paymentGatewayDtoPermata);
        return paymentGatewayDtos;
    }

}
