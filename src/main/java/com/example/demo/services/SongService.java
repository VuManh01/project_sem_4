package com.example.demo.services;

import com.example.demo.dto.request.NewOrUpdateGenreSong;
import com.example.demo.dto.request.NewOrUpdateSong;
import com.example.demo.dto.response.common_response.SongResponse;
import com.example.demo.entities.Albums;
import com.example.demo.entities.Artists;
import com.example.demo.entities.Songs;
import com.example.demo.ex.NotFoundException;
import com.example.demo.ex.ValidationException;
import com.example.demo.repositories.AlbumRepository;
import com.example.demo.repositories.ArtistRepository;
import com.example.demo.repositories.GenreSongRepository;
import com.example.demo.repositories.SongRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private GenreSongRepository genreSongRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private GenreSongService genreSongService;

    @Autowired
    private FileService fileService;

    @Autowired
    private AlbumRepository albumRepository;

    public List<SongResponse> getAllSongs(){
        return songRepository.findAllNotDeleted(false)
                .stream()
                .map(this::toSongResponse)
                .collect(Collectors.toList());
    }

    public SongResponse toSongResponse(Songs song){
        SongResponse res = new SongResponse();
        BeanUtils.copyProperties(song, res);
        res.setIsDeleted(song.getIsDeleted());
        res.setIsPending(song.getIsPending());
        if(song.getAlbumId() != null){
            res.setAlbumId(song.getAlbumId().getId());
        }
        res.setArtistId(song.getArtistId().getId());
        List<Integer> genreIds = genreSongRepository.findBySongId(song.getId(), false)
                .stream()
                .map(it -> it.getGenreId().getId())
                .toList();
        res.setGenreIds(genreIds);
        return res;

    }


    public NewOrUpdateSong addNewSong(NewOrUpdateSong request){
        try {
            List<Map<String, String>> errors = new ArrayList<>();

            Optional<Songs> op = songRepository.findByTitle(request.getTitle());
            if(op.isPresent()){
                errors.add(Map.of("titleError", "Already exist title"));
            }

            Optional<Artists> artists = artistRepository.findByIdAndIsDeleted(request.getArtistId(), false);
            if(artists.isEmpty()){
                errors.add(Map.of("artistError", "Can't find artist"));
            }

            if(!errors.isEmpty()){
                throw new ValidationException(errors);
            }

            Songs newSong = new Songs(request.getTitle(), request.getAudioPath(),
                    0, request.getFeatureArtist(), request.getLyricFilePath(), false, false,
                    new Date(), new Date(), artists.get());
            songRepository.save(newSong);

            request.getGenreIds()
                    .stream()
                    .map(it -> new NewOrUpdateGenreSong(null, it, newSong.getId()))
                    .forEach(newOrUpdateGenreSong -> genreSongService.addNewGenreSong(newOrUpdateGenreSong));

            return request;

        } catch (RuntimeException e){
            //Xóa file nếu insert database thất bại
            fileService.deleteLRCFile(request.getLyricFilePath());
            fileService.deleteAudioFile(request.getAudioPath());
            throw e;
        }
    }

    public NewOrUpdateSong updateSong(NewOrUpdateSong request){

        List<Map<String, String>> errors = new ArrayList<>();

        Optional<Songs> optional = songRepository.findById(request.getId());

        // Check sự tồn tại
        if(optional.isEmpty()){ // <=> optional == null
            throw new NotFoundException("Can't find any song with id: "+request.getId());
        }

        Songs song = optional.get();

        Optional<Songs> optionaTitle = songRepository.findByTitle(request.getTitle());
        if(optionaTitle.isPresent() && optionaTitle.get().getTitle() != optional.get().getTitle()){
            errors.add(Map.of("titleError", "Already exist title"));
        }

        Optional<Artists> artists = artistRepository.findByIdAndIsDeleted(request.getArtistId(), false);
        if(artists.isEmpty()){
            errors.add(Map.of("artistError", "Can't find artist"));
        }

        if(request.getAlbumId() != null){
            Optional<Albums> albums = albumRepository.findByIdAndIsDeleted(request.getAlbumId(), false);
            if(albums.isPresent()){ // <=> albums != null
                song.setAlbumId(albums.get());
            } else {
                errors.add(Map.of("albumError", "Can't find album"));
            }
        }

        if(!errors.isEmpty()){
            throw new ValidationException(errors);
        }

        if(!StringUtils.isEmpty(request.getAudioPath())){
            //check xem co anh khong, co thi thay moi, khong thi thoi
            fileService.deleteAudioFile(song.getAudioPath());
            song.setAudioPath(request.getAudioPath());
        }

        if(!StringUtils.isEmpty(request.getLyricFilePath())){
            //check xem co anh khong, co thi thay moi, khong thi thoi
            fileService.deleteLRCFile(song.getLyricFilePath());
            song.setLyricFilePath(request.getLyricFilePath());
        }

        song.setTitle(request.getTitle());
        song.setListenAmount(request.getListenAmount());
        song.setFeatureArtist(request.getFeatureArtist());
        song.setModifiedAt(new Date());
        song.setArtistId(artists.get());
        songRepository.save(song);

        genreSongService.updateGenresForSong(request.getId(), request.getGenreIds());
        return request;
    }

}
