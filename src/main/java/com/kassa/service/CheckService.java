package com.kassa.service;

import com.kassa.client.ICheckClient;
import com.kassa.entity.Check;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return checkClient.addCheck(check);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Check> getAllChecks() {

        return checkClient.getAllChecks();
    }



}
