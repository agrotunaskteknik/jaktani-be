package com.cartas.jaktani.controller;

import com.cartas.jaktani.dto.TypeGroupDto;
import com.cartas.jaktani.exceptions.ResourceNotFoundException;
import com.cartas.jaktani.service.TypeGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/typeGroup")
public class TypeGroupController {
    @Autowired
    TypeGroupService typeGroupService;

    @GetMapping(path = "/id/{id}")
    public Object getTypeGroupByID(@PathVariable(name = "id") Integer id) throws ResourceNotFoundException {
        return typeGroupService.getTypeGroupByID(id);
    }

    @GetMapping(path = "/all")
    public Object getAllTypeGroup() {
        return typeGroupService.getAllTypeGroups();
    }
	
	@PostMapping(path = "/add") 
	public Object addTypeGroup(@RequestBody TypeGroupDto typeGroup) { 
		return typeGroupService.addTypeGroup(typeGroup); 
	}

	@PostMapping(path = "/edit") 
	public Object editTypeGroup(@RequestBody TypeGroupDto typeGroup) { 
		return typeGroupService.editTypeGroup(typeGroup); 
	}

    @PostMapping(path = "/delete/{id}")
    public Object deleteTypeGroupByID(@PathVariable(name = "id") Integer id) throws ResourceNotFoundException {
        return typeGroupService.deleteTypeGroupByID(id);
    }
    

}
