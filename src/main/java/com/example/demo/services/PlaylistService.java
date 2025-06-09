package com.example.demo.services;

import com.example.demo.dto.request.NewOrUpdatePlaylist;
import com.example.demo.dto.response.common_response.PlaylistResponse;
import com.example.demo.dto.response.display_for_admin.PlaylistDisplayForAdmin;
import com.example.demo.entities.Playlists;
import com.example.demo.entities.Users;
import com.example.demo.ex.NotFoundException;
import com.example.demo.ex.ValidationException;
import com.example.demo.repositories.PlaylistRepository;
import com.example.demo.repositories.PlaylistSongRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private PlaylistSongRepository playlistSongRepository;

    @Autowired
    private UserRepository userRepository;

    public int getNumberOfPlaylist() {
        return playlistRepository.getNumberOfAllNotDeleted(false);
    }

    public List<PlaylistDisplayForAdmin> getAllPlaylistsDisplayForAdmin(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return playlistRepository.findAllNotDeletedPaging(false,pageable)
                .stream()
                .map(this::toPlaylistDisplayForAdmin)
                .collect(Collectors.toList());
    }

    public PlaylistDisplayForAdmin toPlaylistDisplayForAdmin(Playlists playlist) {
        PlaylistDisplayForAdmin res = new PlaylistDisplayForAdmin();
        res.setTotalSong(playlistSongRepository.findByPlaylistId(playlist.getId(), false).size());
        BeanUtils.copyProperties(playlist, res);
        res.setIsDeleted(playlist.getIsDeleted());
        res.setUsername(playlist.getUserId().getUsername());
        return res;
    }

    public NewOrUpdatePlaylist addNewPlaylist(NewOrUpdatePlaylist request) {
        List<Map<String, String>> errors = new ArrayList<>();
        Optional<Users> user = userRepository.findByIdAndIsDeleted(request.getUserId(), false);

        if (user.isEmpty()) {
            errors.add(Map.of("userError", "Can't find user"));
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        Playlists newPlaylist = new Playlists(request.getTitle(), false, new Date(),
                new Date(), user.get());
        playlistRepository.save(newPlaylist);
        return request;
    }

    public NewOrUpdatePlaylist updatePlaylist(NewOrUpdatePlaylist request) {
        List<Map<String, String>> errors = new ArrayList<>();
        Optional<Playlists> op = playlistRepository.findByIdAndIsDeleted(request.getId(), false);
        if (op.isEmpty()) {
            throw new NotFoundException("Can't find any playlist with id: " + request.getId());
        }

        Optional<Users> user = userRepository.findByIdAndIsDeleted(request.getUserId(), false);
        if (user.isEmpty()) {
            errors.add(Map.of("userError", "Can't find user"));
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        Playlists playlist = op.get();
        playlist.setTitle(request.getTitle());
        playlist.setUserId(user.get());
        playlist.setModifiedAt(new Date());
        playlistRepository.save(playlist);
        return request;
    }

    public List<PlaylistDisplayForAdmin> getSearchPlaylistsDisplayForAdmin(String searchTxt, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return playlistRepository.searchNotDeletedPaging(searchTxt, false, pageable)
                .stream()
                .map(this::toPlaylistDisplayForAdmin)
                .collect(Collectors.toList());
    }

    public PlaylistResponse findById(int id) {
        Optional<Playlists> op = playlistRepository.findByIdAndIsDeleted(id, false);
        if (op.isEmpty()) {
            throw new NotFoundException("Can't find any playlists with id: " + id);
        }
        return toPlaylistResponse(op.get());
    }

    public PlaylistResponse toPlaylistResponse(Playlists playlist) {
        PlaylistResponse res = new PlaylistResponse();
        BeanUtils.copyProperties(playlist, res);
        res.setIsDeleted(playlist.getIsDeleted());
        res.setUserId(playlist.getUserId().getId());
        return res;
    }

    public PlaylistDisplayForAdmin findDisplayForAdminById(int id) {
        Optional<Playlists> op = playlistRepository.findByIdAndIsDeleted(id, false);
        if (op.isEmpty()) {
            throw new NotFoundException("Can't find any playlists with id: " + id);
        }
        return toPlaylistDisplayForAdmin(op.get());
    }

    public boolean deleteById(int id) {
        Optional<Playlists> playlist = playlistRepository.findById(id);
        if (playlist.isEmpty()) {
            throw new NotFoundException("Can't find any playlist with id: " + id);
        }
        Playlists existing = playlist.get();
        existing.setIsDeleted(true);
        playlistRepository.save(existing);
        return true;
    }


}
