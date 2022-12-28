package com.kassa.client;

import com.kassa.entity.Check;
import com.kassa.entity.Photo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class PhotoClient implements IPhotoClient{

    @Override
    public List<Photo> getNotProcessedPhotos() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Photo[]> response = restTemplate.getForEntity("http://localhost:8080/photos", Photo[].class);
        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }
}
