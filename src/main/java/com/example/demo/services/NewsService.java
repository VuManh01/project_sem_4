package com.example.demo.services;

import com.example.demo.dto.request.NewOrUpdateNews;
import com.example.demo.dto.request.UpdateFileModel;
import com.example.demo.dto.response.display_for_admin.NewsDisplayForAdmin;
import com.example.demo.entities.News;
import com.example.demo.ex.NotFoundException;
import com.example.demo.ex.ValidationException;
import com.example.demo.repositories.NewsRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private FileService fileService;

    public int getNumberOfNews() {
        return newsRepository.getNumberOfAll();
    }

    public List<NewsDisplayForAdmin> getAllNewsForAdmin(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return newsRepository.findAllPaging(pageable)
                .stream()
                .map(this::toNewsDisplayForAdmin)
                .collect(Collectors.toList());
    }
    // để sử dụng được service trên cần method dưới đây
    public NewsDisplayForAdmin toNewsDisplayForAdmin(News news) {
        NewsDisplayForAdmin res = new NewsDisplayForAdmin();
        BeanUtils.copyProperties(news, res);
        return res;
    }

    public NewOrUpdateNews addNew(NewOrUpdateNews request) {
        try {

            List<Map<String, String>> errors = new ArrayList<>();

            Optional<News> op = newsRepository.findByTitle(request.getTitle());
            if (op.isPresent()) {
                errors.add(Map.of("titleError", "Already exist title"));
            }

            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }

            News newNews = new News(
                    request.getTitle(),
                    request.getImage(),
                    request.getContent(),
                    true,
                    new Date(),
                    new Date()
            );

            newsRepository.save(newNews);

            return request;
        } catch (RuntimeException e) {
            // Xóa file nếu insert database thất bại
            fileService.deleteImageFile(request.getImage());
            throw e;
        }
    }

    public NewsDisplayForAdmin findDisplayForAdminById(int id) {
        Optional<News> op = newsRepository.findById(id);
        if (op.isEmpty()) {
            throw new NotFoundException("Can't find any news with id: " + id);
        }
        return toNewsDisplayForAdmin(op.get());
    }

    public boolean deleteById(int id) {
        Optional<News> news = newsRepository.findById(id);
        if (news.isEmpty()) {
            throw new NotFoundException("Can't find any genre with id: " + id);
        }
        News existing = news.get();
        newsRepository.delete(existing);
        return true;
    }

    public void updateNewsImage(UpdateFileModel request) {
        Optional<News> op = newsRepository.findById(request.getId());
        //check sự tồn tại
        if (op.isEmpty()) {
            throw new NotFoundException("Can't find any news with id: " + request.getId());
        }
        News news = op.get();
        fileService.deleteImageFile(news.getImage());
        news.setImage(request.getFileName());

        news.setModifiedAt(new Date());
        newsRepository.save(news);

    }

    public NewOrUpdateNews updateNews(NewOrUpdateNews request) {
        List<Map<String, String>> errors = new ArrayList<>();

        Optional<News> op = newsRepository.findById(request.getId());
        if (op.isEmpty()) {
            throw new NotFoundException("Can't find any news with id: " + request.getId());

        }

        Optional<News> opTitle = newsRepository.findByTitle(request.getTitle());
        if (opTitle.isPresent() && opTitle.get().getTitle() != op.get().getTitle()) {
            errors.add(Map.of("titleError", "Already exist title"));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }


        News news = op.get();
        if (!StringUtils.isEmpty(request.getImage())) {
            //check xem có ảnh ko, có thì thay mới, ko thì thôi
            fileService.deleteImageFile(news.getImage());
            news.setImage(request.getImage());
        }
        news.setTitle(request.getTitle());
        news.setContent(request.getContent());
        news.setImage(request.getImage());
        news.setModifiedAt(new Date());
        news.setIsActive(request.getIsActive());
        newsRepository.save(news);

        return request;
    }

    public void toggleNewsActiveStatus(int id) {
        Optional<News> op = newsRepository.findById(id);
        if (op.isEmpty()) {
            throw new NotFoundException("Can't find any news with id: " + id);
        }
        News news = op.get();
        news.setIsActive(!news.getIsActive());
        news.setModifiedAt(new Date());
        newsRepository.save(news);

    }
}
