package com.kassa.service;

import com.kassa.entity.Check;
import com.kassa.entity.Photo;

import java.util.List;

public interface IPhotoService {

    List<Photo> getNotProcessedPhotos();
}
