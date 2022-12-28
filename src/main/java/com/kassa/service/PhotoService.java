package com.kassa.service;

import com.kassa.client.IPhotoClient;
import com.kassa.entity.Photo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoService implements IPhotoService {
    private final IPhotoClient photoClient;

    public PhotoService(IPhotoClient photoClient) {
        this.photoClient = photoClient;
    }

    @Override
    public List<Photo> getNotProcessedPhotos() {
        return photoClient.getNotProcessedPhotos();
    }
}
