package com.kassa.rest;

import com.kassa.entity.Check;
import com.kassa.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CheckController {
    @Autowired
    CheckService checkService;

    @RequestMapping(value = "checks", method = RequestMethod.GET)
    public List<Check> getAllChecks (){
        return checkService.getAllChecks();
    }

}
