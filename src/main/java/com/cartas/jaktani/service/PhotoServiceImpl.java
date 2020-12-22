package com.cartas.jaktani.service;

import com.cartas.jaktani.model.Photo;
import com.cartas.jaktani.repository.PhotoRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Service
public class PhotoServiceImpl implements PhotoService {
    Logger logger = LoggerFactory.getLogger(PhotoServiceImpl.class);

    @Autowired
    PhotoRepository photoRepository;

    @Override
    public byte[] getPhoto(String urlPath) {
        List<Photo> photoList = photoRepository.findAllByUrlPath(urlPath);
        if (photoList == null || photoList.size() == 0) {
            logger.debug("Image not found from repository");
            return null;
        }
        String imageFilePath = photoList.get(0).getImageFilePath();
        if (imageFilePath == null || imageFilePath.trim().equalsIgnoreCase("")) {
            logger.debug("Image from repository is empty");
        }
        try {
            File initialFileImage = new File(imageFilePath);
            if (!initialFileImage.exists()) {
                logger.debug("initialFileImage Path not found");
                return null;
            }
            InputStream in = new FileInputStream(initialFileImage);
            return IOUtils.toByteArray(in);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }
}
