package com.example.demo.services;

import com.example.demo.repositories.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    public int getNumberOfNews() {
        return newsRepository.getNumberOfAll();
    }

}
