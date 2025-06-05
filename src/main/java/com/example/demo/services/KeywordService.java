package com.example.demo.services;

import com.example.demo.repositories.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeywordService {

    @Autowired
    private KeywordRepository keywordRepository;

    public int getNumberOfKeywords() {
        return keywordRepository.getNumberOfAll();
    }

}
