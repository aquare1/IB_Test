package com.aquare.controller;

import com.aquare.common.ResultDTO;
import com.aquare.entity.PersonEntity;
import com.aquare.service.PersonService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/v1")
public class PersonController {

    @Autowired
    PersonService personService;

    @ApiOperation(value = "post-person", notes="post-person")
    @PostMapping(value = "/post-person" , produces = { "application/json; charset=UTF-8" })
    public ResultDTO postPerson(@RequestParam(value = "personName",required = true) String personName ){
        return personService.save(personName);
    }

    @ApiOperation(value = "get-person", notes="get-person")
    @GetMapping(value = "/get-person/{personId}" , produces = { "application/json; charset=UTF-8" })
    public ResultDTO findById(@PathVariable(value="personId" ,required = true) String personId ){
        return personService.findById(personId);
    }

    @ApiOperation(value = "delete-person", notes="delete-person")
    @DeleteMapping(value = "/delete-person/{personId}" , produces = { "application/json; charset=UTF-8" })
    public ResultDTO delete(@PathVariable(value="personId" ,required = true) String personId ){
        return personService.delete(personId);
    }

    @ApiOperation(value = "login", notes="login")
    @GetMapping(value = "/login" , produces = { "application/json; charset=UTF-8" })
    public ResultDTO login( ){
        return personService.login();
    }

}
