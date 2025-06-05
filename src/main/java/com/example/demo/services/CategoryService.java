package com.example.demo.services;

import com.example.demo.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public int getNumberOfCate() {
        return categoryRepository.getNumberOfAllNotDeleted(false);
    }

}
