package com.example.demo.services;

import com.example.demo.dto.request.NewOrUpdateCategoryAlbum;
import com.example.demo.entities.Albums;
import com.example.demo.entities.Categories;
import com.example.demo.entities.CategoryAlbum;
import com.example.demo.ex.AlreadyExistedException;
import com.example.demo.ex.NotFoundException;
import com.example.demo.repositories.AlbumRepository;
import com.example.demo.repositories.CategoryAlbumRepository;
import com.example.demo.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryAlbumService {

    @Autowired
    private CategoryAlbumRepository categoryAlbumRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public NewOrUpdateCategoryAlbum addNewCategoryAlbum(NewOrUpdateCategoryAlbum request) {
        Optional<CategoryAlbum> existingCategoryAlbum = categoryAlbumRepository.findByCategoryIdAndAlbumId(request.getCategoryId(), request.getAlbumId());
        if (existingCategoryAlbum.isPresent()) {
            throw new AlreadyExistedException("A CategoryAlbum already exists");
        }

        Optional<Albums> album = albumRepository.findByIdAndIsDeleted(request.getAlbumId(), false);
        if (album.isEmpty()) {
            throw new NotFoundException("Can't find any album with id: " + request.getAlbumId());
        }


        Optional<Categories> cate = categoryRepository.findByIdAndIsDeleted(request.getCategoryId(), false);
        if (cate.isEmpty()) {
            throw new NotFoundException("Can't find any category with id: " + request.getCategoryId());
        }

        CategoryAlbum newSubjectAlbum = new CategoryAlbum(
                album.get(),
                cate.get()
        );
        categoryAlbumRepository.save(newSubjectAlbum);
        return request;
    }
}
