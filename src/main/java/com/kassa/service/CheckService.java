package com.kassa.service;

import com.kassa.entity.Check;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class CheckService implements ICheckService {

    @Override
    public List<Check> getAllChecks() {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Check[]> response = restTemplate.getForEntity("http://localhost:8080/checks", Check[].class);


        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }

}
