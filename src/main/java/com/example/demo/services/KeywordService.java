package com.example.demo.services;

import com.example.demo.dto.request.NewOrUpdateKeyword;
import com.example.demo.dto.response.display_for_admin.KeywordDisplayForAdmin;
import com.example.demo.entities.Keywords;
import com.example.demo.ex.NotFoundException;
import com.example.demo.ex.ValidationException;
import com.example.demo.repositories.KeywordRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class KeywordService {

    @Autowired
    private KeywordRepository keywordRepository;

    public int getNumberOfKeywords() {
        return keywordRepository.getNumberOfAll();
    }

    public List<KeywordDisplayForAdmin> getAllKeywordsForAdmin(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return keywordRepository.findAllPaging(pageable)
                .stream()
                .map(this::toKeywordDisplayForAdmin)
                .collect(Collectors.toList());
    }
    // để sử dụng được service trên cần có method dưới đây
    public KeywordDisplayForAdmin toKeywordDisplayForAdmin(Keywords keyword) {
        KeywordDisplayForAdmin res = new KeywordDisplayForAdmin();
        BeanUtils.copyProperties(keyword, res);
        return res;
    }


    public NewOrUpdateKeyword addNew(NewOrUpdateKeyword request) {
        List<Map<String, String>> errors = new ArrayList<>();

        Optional<Keywords> op = keywordRepository.findByContent(request.getContent());
        if (op.isPresent()) {
            errors.add(Map.of("contentError", "Already exist keyword"));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }


        Keywords newKeyword = new Keywords(
                request.getContent(),
                request.getIsActive(),
                new Date(),
                new Date()
        );

        keywordRepository.save(newKeyword);

        return request;
    }


    public void toggleKeywordActiveStatus(int id) {
        Optional<Keywords> op = keywordRepository.findById(id);
        if (op.isEmpty()) {
            throw new NotFoundException("Can't find any keyword with id: " + id);
        }
        Keywords keyword = op.get();
        keyword.setIsActive(!keyword.getIsActive());
        keyword.setModifiedAt(new Date());
        keywordRepository.save(keyword);

    }

    public boolean deleteById(int id) {
        Optional<Keywords> op = keywordRepository.findById(id);
        if (op.isEmpty()) {
            throw new NotFoundException("Can't find any genre with id: " + id);
        }
        Keywords existing = op.get();
        keywordRepository.delete(existing);
        return true;
    }
}
