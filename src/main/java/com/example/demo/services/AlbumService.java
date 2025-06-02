package com.example.demo.services;

import com.example.demo.repositories.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    public int getNumberOfAlbum() {
        return albumRepository.getNumberOfAllNotDeleted(false);
    }

}
