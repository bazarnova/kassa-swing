package com.kassa.client;

import com.kassa.entity.Check;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.WebServiceClient;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class CheckClient implements ICheckClient{
    @Override
    public Check addCheck(Check check) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Check> response = restTemplate.postForEntity("http://localhost:8080/check", check, Check.class);
        return response.getBody();
    }

    @Override
    public List<Check> getAllChecks() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Check[]> response = restTemplate.getForEntity("http://localhost:8080/checks", Check[].class);
        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }
}
