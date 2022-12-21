package com.kassa.service;

import com.kassa.client.ICheckClient;
import com.kassa.entity.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class CheckService implements ICheckService {
    private final ICheckClient checkClient;

    public CheckService(ICheckClient checkClient) {
        this.checkClient = checkClient;
    }

    @Override
    public Check addNewCheck(Check check){
        //проверки
        try {
        return checkClient.addNewCheck(check);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Check> getAllChecks() {

        return checkClient.getAllChecks();
    }



}
