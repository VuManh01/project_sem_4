package com.example.demo.services;

import com.example.demo.dto.request.NewOrUpdateCategory;
import com.example.demo.dto.response.display_for_admin.CategoryDisplayForAdmin;
import com.example.demo.entities.Categories;
import com.example.demo.ex.NotFoundException;
import com.example.demo.ex.ValidationException;
import com.example.demo.repositories.CategoryAlbumRepository;
import com.example.demo.repositories.CategoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryAlbumRepository categoryAlbumRepository;

    public int getNumberOfCate() {
        return categoryRepository.getNumberOfAllNotDeleted(false);
    }

    public CategoryDisplayForAdmin findByIdForAdmin(int id) {
        Optional<Categories> op = categoryRepository.findByIdAndIsDeleted(id, false);
        if (op.isPresent()) {
            Categories subjects = op.get();
            return toCategoryCategoryDisplayForAdmin(subjects);
        } else {
            throw new NotFoundException("Can't find any category with id: " + id);
        }
    }
    // để sử dụng service trên cần có method bên dưới
    private CategoryDisplayForAdmin toCategoryCategoryDisplayForAdmin(Categories sub) {
        CategoryDisplayForAdmin res = new CategoryDisplayForAdmin();
        res.setIsDeleted(sub.getIsDeleted());
        res.setTotalAlbum(categoryAlbumRepository.findAllByCategoryId(sub.getId(), false).size());
        BeanUtils.copyProperties(sub, res);
        return res;
    }

    public List<CategoryDisplayForAdmin> getAllCategoriesDisplayForAdmin(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return categoryRepository.findAllNotDeletedPaging(false, pageable)
                .stream()
                .map(this::toCategoryCategoryDisplayForAdmin)
                .collect(Collectors.toList());
    }

    public NewOrUpdateCategory addNewSubject(NewOrUpdateCategory request) {
        List<Map<String, String>> errors = new ArrayList<>();

        Optional<Categories> op = categoryRepository.findByTitle(request.getTitle());
        if (op.isPresent()) {
            errors.add(Map.of("titleError", "Already exist title"));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        Date currentDate = new Date();
        Categories newSub = new Categories(request.getTitle(), request.getDescription(), false, currentDate, currentDate);
        categoryRepository.save(newSub);
        return request;
    }

    public void deleteById(int id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Can't find any category with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

}
