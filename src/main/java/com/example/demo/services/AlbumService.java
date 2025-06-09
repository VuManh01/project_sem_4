package com.example.demo.services;

import com.example.demo.dto.request.NewOrUpdateAlbum;
import com.example.demo.dto.request.NewOrUpdateCategoryAlbum;
import com.example.demo.dto.request.UpdateFileModel;
import com.example.demo.dto.response.auth_response.AlbumDisplayForAdmin;
import com.example.demo.entities.Albums;
import com.example.demo.entities.Artists;
import com.example.demo.entities.FavouriteAlbums;
import com.example.demo.ex.NotFoundException;
import com.example.demo.ex.ValidationException;
import com.example.demo.repositories.AlbumRepository;
import com.example.demo.repositories.ArtistRepository;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.FavouriteAlbumRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private FavouriteAlbumRepository favouriteAlbumRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private CategoryAlbumService categoryAlbumService;

    public int getNumberOfAlbum() {
        return albumRepository.getNumberOfAllNotDeleted(false);
    }

    public List<AlbumDisplayForAdmin> getAllAlbumsDisplayForAdmin(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return albumRepository.findAllNotDeletedPaging(false, pageable)
                .stream()
                .map(this::toAlbumDisplayForAdmin)
                .collect(Collectors.toList());
    }

    public AlbumDisplayForAdmin toAlbumDisplayForAdmin(Albums album) {
        AlbumDisplayForAdmin res = new AlbumDisplayForAdmin();
        BeanUtils.copyProperties(album, res);
        res.setIsDeleted(album.getIsDeleted());
        res.setIsReleased(album.getIsReleased());
        res.setArtistName(album.getArtistId().getArtistName());
        res.setArtistImage(album.getArtistId().getImage());
        res.setTotalSong(album.getSongsCollection().size());
        res.setTotalFavourite(favouriteAlbumRepository.findFAByAlbumId(album.getId(), false).size());
        return res;
    }

    public NewOrUpdateAlbum addNewAlbum(NewOrUpdateAlbum request) {
        try {
            List<Map<String, String>> errors = new ArrayList<>();

            Optional<Albums> op = albumRepository.findByTitle(request.getTitle());
            if (op.isPresent()) {
                errors.add(Map.of("titleError", "Already exist title"));
            }

            Optional<Artists> artist = artistRepository.findByIdAndIsDeleted(request.getArtistId(), false);
            if (artist.isEmpty()) {
                errors.add(Map.of("artistError", "Can't find artist"));
            }
            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }
            Albums newAlbum = new Albums(request.getTitle(), request.getImage(), false, request.getReleaseDate(),
                    false, new Date(), new Date(), artist.get());

            albumRepository.save(newAlbum);

            request.getCateIds()
                    .stream()
                    .map(it -> new NewOrUpdateCategoryAlbum(null, newAlbum.getId(), it))
                    .forEach(newOrUpdateCategoryAlbum -> categoryAlbumService.addNewCategoryAlbum(newOrUpdateCategoryAlbum));

            return request;
        } catch (RuntimeException e) {
            // Xóa file nếu insert database thất bại
            fileService.deleteImageFile(request.getImage());
            throw e;
        }
    }

    public List<AlbumDisplayForAdmin> getAllFavAlbumsByUserIdForAdmin(Integer id, int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return favouriteAlbumRepository.findFAByUserIdPaging(id, false, pageable)
                .stream()
                .map(this::toAlbumDisplayForAdmin)
                .collect(Collectors.toList());
    }

    public AlbumDisplayForAdmin toAlbumDisplayForAdmin(FavouriteAlbums favAlbum) {
        int albumId = favAlbum.getAlbumId().getId();
        Albums album = albumRepository.findByIdAndIsDeleted(albumId, false).get();

        AlbumDisplayForAdmin res = new AlbumDisplayForAdmin();
        res.setTitle(album.getTitle());
        res.setImage(album.getImage());
        res.setReleaseDate(album.getReleaseDate());
        res.setIsDeleted(album.getIsDeleted());
        res.setIsReleased(album.getIsReleased());
        res.setArtistName(album.getArtistId().getArtistName());
        res.setArtistImage(album.getArtistId().getImage());
        res.setId(albumId);
        res.setCreatedAt(album.getCreatedAt());
        res.setModifiedAt(album.getModifiedAt());
        return res;
    }

    public AlbumDisplayForAdmin findDisplayForAdminById(int id) {
        Optional<Albums> op = albumRepository.findByIdAndIsDeleted(id, false);
        if (op.isEmpty()) {
            throw new NotFoundException("Can't find any album with id: " + id);
        }
        return toAlbumDisplayForAdmin(op.get());
    }

    public void updateAlbumImage(UpdateFileModel request) {
        Optional<Albums> op = albumRepository.findByIdAndIsDeleted(request.getId(), false);
        //check sự tồn tại
        if (op.isEmpty()) {
            fileService.deleteImageFile(request.getFileName());
            throw new NotFoundException("Can't find any album with id: " + request.getId());
        }
        Albums album = op.get();
        fileService.deleteImageFile(album.getImage());
        album.setImage(request.getFileName());

        album.setModifiedAt(new Date());
        albumRepository.save(album);
    }

    public void toggleAlbumReleaseStatus(int albumId) {
        Optional<Albums> op = albumRepository.findByIdAndIsDeleted(albumId, false);
        if (op.isEmpty()) {
            throw new NotFoundException("Can't find any album with id: " + albumId);
        }
        Albums album = op.get();
        album.setIsReleased(!album.getIsReleased());
        album.setModifiedAt(new Date());
        albumRepository.save(album);

    }

    public boolean deleteById(int id) {
        Optional<Albums> album = albumRepository.findById(id);
        if (album.isEmpty()) {
            throw new NotFoundException("Can't find any album with id: " + id);
        }
        Albums existing = album.get();
        existing.setIsDeleted(true);
        albumRepository.save(existing);
        return true;
    }

    public List<AlbumDisplayForAdmin> getAllAlbumsByArtistIdForAdmin(int artistId, int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return albumRepository.findAllByArtistIdPaging(artistId, false, pageable)
                .stream()
                .map(this::toAlbumDisplayForAdmin)
                .collect(Collectors.toList());
    }
}
