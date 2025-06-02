package com.example.demo.services;

import com.example.demo.dto.request.NewOrUpdateGenres;
import com.example.demo.dto.request.UpdateFileModel;
import com.example.demo.dto.response.display_for_admin.GenreDisplayForAdmin;
import com.example.demo.entities.Colors;
import com.example.demo.entities.Genres;
import com.example.demo.ex.NotFoundException;
import com.example.demo.ex.ValidationException;
import com.example.demo.repositories.ColorRepository;
import com.example.demo.repositories.GenreSongRepository;
import com.example.demo.repositories.GenresRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GenresService {

    @Autowired
    private GenresRepository genresRepository;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private GenreSongRepository genreSongRepository;

//Service add new genre
    public NewOrUpdateGenres addNewGenre(NewOrUpdateGenres request){
        try{

            List<Map<String, String>> errors = new ArrayList<>();


            Optional<Genres> op = genresRepository.findByTitle(request.getTitle());
            if (op.isPresent()) {
                errors.add(Map.of("titleError", "Already exist title"));
            }
            Optional<Colors> colorOp = colorRepository.findById(request.getColorId());
            if (colorOp.isEmpty()) {
                errors.add(Map.of("colorError", "Can't find color"));
            }

            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }

            Genres newGenre = new Genres(
                    request.getTitle(),
                    request.getImage(),
                    false,
                    colorOp.get(),
                    new Date(),
                    new Date()
            );

            genresRepository.save(newGenre);

            return request;
        } catch (RuntimeException e) {
            // Xóa file nếu insert database thất bại
            fileService.deleteImageFile(request.getImage());
            throw e;
        }
    }

    //Service: find all genres for admin
    public GenreDisplayForAdmin findGenreDisplayForAdminById(int id){
        Optional<Genres> optional = genresRepository.findByIdAndIsDeleted(id, false);
        if(optional.isEmpty()){
            throw new RuntimeException("Can't find any genre with id: " + id);
        }
        return toGenreDisplayForAdmin(optional.get());
    }

    public GenreDisplayForAdmin toGenreDisplayForAdmin(Genres genre) {
        GenreDisplayForAdmin res = new GenreDisplayForAdmin();
        BeanUtils.copyProperties(genre, res);
        res.setTotalSong(genreSongRepository.findByGenreId(genre.getId(), false)
                .stream()
                .toList()
                .size());
        res.setColor(genre.getColorId().getTitle());
        return res;
    }

    public List<GenreDisplayForAdmin> getAllGenreDisplayForAdmin(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return genresRepository.findAllNotDeletedPaging(false, pageable)
                .stream()
                .map(this::toGenreDisplayForAdmin)
                .collect(Collectors.toList());
    }

    public void updateGenreImage(UpdateFileModel request) {
        Optional<Genres> op = genresRepository.findByIdAndIsDeleted(request.getId(), false);
        //check sự tồn tại
        if (op.isEmpty()) {
            fileService.deleteImageFile(request.getFileName());
            throw new NotFoundException("Can't find any genre with id: " + request.getId());
        }
        Genres genre = op.get();
        fileService.deleteImageFile(genre.getImage());
        genre.setImage(request.getFileName());

        genre.setModifiedAt(new Date());
        genresRepository.save(genre);

    }

    public int getNumberOfGenre() {
        return genresRepository.getNumberOfAllNotDeleted(false);
    }


}
