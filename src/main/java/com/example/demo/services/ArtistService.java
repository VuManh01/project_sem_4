package com.example.demo.services;

import com.example.demo.dto.request.NewOrUpdateArtist;
import com.example.demo.dto.request.UpdateFileModel;
import com.example.demo.dto.response.common_response.ArtistResponse;
import com.example.demo.dto.response.display_for_admin.ArtistDisplayForAdmin;
import com.example.demo.entities.Artists;
import com.example.demo.entities.Songs;
import com.example.demo.entities.Users;
import com.example.demo.ex.NotFoundException;
import com.example.demo.ex.ValidationException;
import com.example.demo.repositories.ArtistRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository aristRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserRepository userRepository;

    public int getNumberOfArtist() {
        return aristRepository.getNumberOfAllNotDeleted(false);
    }

    public List<ArtistDisplayForAdmin> getAllArtistsDisplayForAdmin(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return aristRepository.findAllNotDeletedPaging(false, pageable)
                .stream()
                .map(this::toArtistDisplayForAdmin)
                .collect(Collectors.toList());
    }
    public ArtistDisplayForAdmin toArtistDisplayForAdmin(Artists artist) {
        ArtistDisplayForAdmin res = new ArtistDisplayForAdmin();
        BeanUtils.copyProperties(artist, res);
        res.setIsDeleted(artist.getIsDeleted());
        res.setTotalSong(artist.getSongsCollection().size());
        res.setTotalAlbum(artist.getAlbumsId().size());
        int totalListenAmount = artist.getSongsCollection()
                .stream().mapToInt(Songs::getListenAmount).sum();
        res.setTotalListenAmount(totalListenAmount);
        Users user = artist.getUsersId();
        if (user != null) {
            res.setUsername(artist.getUsersId().getUsername());
            res.setIsActive(true);
            return res;
        }
        res.setUsername("");
        res.setIsActive(false);
        return res;
    }

    public NewOrUpdateArtist addNewArtist(NewOrUpdateArtist request) {
        try {

            List<Map<String, String>> errors = new ArrayList<>();

            Optional<Artists> op = aristRepository.findByArtistName(request.getArtistName());
            if (op.isPresent()) {
                errors.add(Map.of("artistNameError", "Already exist artist name"));
            }

            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }

            Artists newArtist = new Artists(request.getArtistName(), request.getImage(),
                    request.getBio(), false, new Date(), new Date());
            aristRepository.save(newArtist);
            return request;
        } catch (RuntimeException e) {
            // Xóa file nếu insert database thất bại
            fileService.deleteImageFile(request.getImage());
            throw e;
        }
    }

    public ArtistDisplayForAdmin findByIdForAdmin(int id) {
        Optional<Artists> op = aristRepository.findByIdAndIsDeleted(id, false);
        if (op.isEmpty()) {
            throw new NotFoundException("Can't find any artist with id: " + id);
        }
        return toArtistDisplayForAdmin(op.get());
    }

    // method cho api:/admin/artists/change/image
    public void updateArtistImage(UpdateFileModel request) {
        Optional<Artists> op = aristRepository.findByIdAndIsDeleted(request.getId(), false);
        //check sự tồn tại
        if (op.isEmpty()) {
            fileService.deleteImageFile(request.getFileName());
            throw new NotFoundException("Can't find any artist with id: " + request.getId());
        }
        Artists artist = op.get();
        fileService.deleteImageFile(artist.getImage());
        artist.setImage(request.getFileName());

        artist.setModifiedAt(new Date());
        aristRepository.save(artist);

    }

    public ArtistResponse findById(int id) {
        Optional<Artists> op = aristRepository.findByIdAndIsDeleted(id, false);
        if (op.isEmpty()) {
            throw new NotFoundException("Can't find any artist with id: " + id);
        }
        return toArtistResponse(op.get());
    }
    public ArtistResponse toArtistResponse(Artists artist) {
        ArtistResponse res = new ArtistResponse();
        BeanUtils.copyProperties(artist, res);
        res.setIsDeleted(artist.getIsDeleted());
        return res;
    }

    public boolean deleteById(int id) {
        Optional<Artists> artistOptional = aristRepository.findById(id);
        if (artistOptional.isEmpty()) {
            throw new NotFoundException("Can't find any artist with id: " + id);
        }
        Artists artist = artistOptional.get();
        artist.setIsDeleted(true);
        aristRepository.save(artist);
        return true;
    }

    public NewOrUpdateArtist updateArtist(NewOrUpdateArtist request) {
        List<Map<String, String>> errors = new ArrayList<>();

        Optional<Artists> op = aristRepository.findByIdAndIsDeleted(request.getId(), false);

        if (op.isEmpty()) {
            throw new NotFoundException("Can't find any artist with id: " + request.getId());
        }
        Artists artist = op.get();

        if (request.getUserId() != null) {
            Optional<Users> userOp = userRepository.findByIdAndIsDeleted(request.getUserId(), false);

            if (userOp.isEmpty()) {
                errors.add(Map.of("userNotExistedError", "Can't find user"));
            } else {
                Users foundUser = userOp.get();

                // Kiểm tra xem artist với user ID đã tồn tại chưa
                Optional<Artists> foundArtistWithUID = aristRepository.findByUserId(request.getUserId(), false);
                if (foundArtistWithUID.isPresent()) {
                    errors.add(Map.of("accountAssignedError", "This artist account already has an owner"));
                } else {
                    artist.setUsersId(foundUser);
                }
            }
        }

        Optional<Artists> opName = aristRepository.findByArtistName(request.getArtistName());
        if (opName.isPresent() && opName.get().getArtistName() != op.get().getArtistName()) {
            errors.add(Map.of("artistNameError", "Already exist artist name"));
        }


        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        if (!StringUtils.isEmpty(request.getImage())) {
            //check xem có ảnh ko, có thì thay mới, ko thì thôi
            fileService.deleteImageFile(artist.getImage());
            artist.setImage(request.getImage());
        }
        artist.setArtistName(request.getArtistName());
        artist.setBio(request.getBio());
        artist.setModifiedAt(new Date());
        aristRepository.save(artist);

        return request;
    }



}
