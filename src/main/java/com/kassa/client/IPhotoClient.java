package com.kassa.client;

import com.kassa.entity.Photo;

import java.util.List;

public interface IPhotoClient {
    List<Photo> getNotProcessedPhotos();
}
