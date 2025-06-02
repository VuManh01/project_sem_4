package com.example.demo.services;

import com.example.demo.repositories.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    public int getNumberOfPlaylist() {
        return playlistRepository.getNumberOfAllNotDeleted(false);
    }

}
