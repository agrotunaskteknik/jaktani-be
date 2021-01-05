package com.cartas.jaktani.controller;

import com.cartas.jaktani.exceptions.ResourceNotFoundException;
import com.cartas.jaktani.model.CategoryRelTypeGroup;
import com.cartas.jaktani.service.CategoryRelTypeGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/categoryRelTypeGroup")
public class CategoryRelTypeGroupController {
    @Autowired
    CategoryRelTypeGroupService service;

    @GetMapping(path = "/getById/{id}")
    public Object getByID(@PathVariable(name = "id") Integer id) throws ResourceNotFoundException {
        return service.getByID(id);
    }

    @GetMapping(path = "/all")
    public Object getAll() {
        return service.getAll();
    }
	
	@PostMapping(path = "/add") 
	public Object addCategory(@RequestBody CategoryRelTypeGroup categoryRelTypeGroup) { 
		return service.add(categoryRelTypeGroup); 
	}

	@PostMapping(path = "/edit") 
	public Object editCategory(@RequestBody CategoryRelTypeGroup categoryRelTypeGroup) { 
		return service.edit(categoryRelTypeGroup); 
	}

    @PostMapping(path = "/delete/{id}")
    public Object deleteByID(@PathVariable(name = "id") Integer id) throws ResourceNotFoundException {
        return service.deleteByID(id);
    }
    
    @GetMapping(path = "/allByCategoryId/{categoryId}")
    public Object getAllTypeByCategoryID(@PathVariable(name = "categoryId") Integer categoryId) throws ResourceNotFoundException {
        return service.getAllByCategoryId(categoryId);
    }

}
