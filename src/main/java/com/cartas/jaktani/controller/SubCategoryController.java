package com.cartas.jaktani.controller;

import com.cartas.jaktani.dto.SubCategoryDto;
import com.cartas.jaktani.exceptions.ResourceNotFoundException;
import com.cartas.jaktani.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/subCategory")
public class SubCategoryController {
    @Autowired
    SubCategoryService subCategoryService;

    @GetMapping(path = "/id/{id}")
    public Object getSubCategoryByID(@PathVariable(name = "id") Integer id) throws ResourceNotFoundException {
        return subCategoryService.getSubCategoryByID(id);
    }

    @GetMapping(path = "/all")
    public Object getAllSubCategory() {
        return subCategoryService.getAllSubCategorys();
    }

	
	@PostMapping(path = "/add") 
	public Object addSubCategory(@RequestBody SubCategoryDto subCategory) { 
		return subCategoryService.addSubCategory(subCategory); 
	}
	 

	@PostMapping(path = "/edit") 
	public Object editSubCategory(@RequestBody SubCategoryDto subCategory) { 
		return subCategoryService.editSubCategory(subCategory); 
	}

    @PostMapping(path = "/delete/{id}")
    public Object deleteSubCategoryByID(@PathVariable(name = "id") Integer id) throws ResourceNotFoundException {
        return subCategoryService.deleteSubCategoryByID(id);
    }

}
