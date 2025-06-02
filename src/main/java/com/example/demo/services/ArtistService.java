package com.example.demo.services;

import com.example.demo.repositories.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository aristRepository;

    public int getNumberOfArtist() {
        return aristRepository.getNumberOfAllNotDeleted(false);
    }

}
