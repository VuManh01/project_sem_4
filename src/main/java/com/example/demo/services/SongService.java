package com.example.demo.services;

import com.example.demo.dto.request.NewOrUpdateGenreSong;
import com.example.demo.dto.request.NewOrUpdateSong;
import com.example.demo.dto.response.common_response.SongResponse;
import com.example.demo.dto.response.display_for_admin.SongDisplayForAdmin;
import com.example.demo.dto.response.mix_response.SongWithViewInMonth;
import com.example.demo.entities.Albums;
import com.example.demo.entities.Artists;
import com.example.demo.entities.Songs;
import com.example.demo.entities.ViewInMonth;
import com.example.demo.ex.NotFoundException;
import com.example.demo.ex.ValidationException;
import com.example.demo.repositories.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.time.LocalDate;
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

    @Autowired
    private FavouriteSongRepository favouriteSongRepository;

    @Autowired
    private ViewInMonthRepository likeAndViewRepository;

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

    public List<SongDisplayForAdmin> getAllSongsForAdmin(int page){
        Pageable pageable = PageRequest.of(page, 10);

        return songRepository.findAllNotDeletedPaging(false, pageable)
                .stream()
                .map(this::toSongDisplayAdmin)
                .collect(Collectors.toList());
    }

    public SongDisplayForAdmin toSongDisplayAdmin(Songs song) {
        SongDisplayForAdmin res = new SongDisplayForAdmin();
        int favCount = favouriteSongRepository.findFSBySongId(song.getId(), false).size();
        res.setTotalFavourite(favCount);

        BeanUtils.copyProperties(song, res);
        res.setIsDeleted(song.getIsDeleted());
        res.setIsPending(song.getIsPending());
        if (song.getAlbumId() != null) {
            res.setAlbumTitle(song.getAlbumId().getTitle());
            res.setAlbumImage(song.getAlbumId().getImage());
        }
        res.setArtistName(song.getArtistId().getArtistName());
        List<String> genreNames = genreSongRepository.findBySongId(song.getId(), false)
                .stream()
                .map(it -> it.getGenreId().getTitle())
                .toList();
        res.setGenreNames(genreNames);
        return res;
    }

    public SongWithViewInMonth getMostListenedSongInMonth() {
        LocalDate cuDate = LocalDate.now();
        Pageable pageable = PageRequest.of(0, 1);
        ViewInMonth mostListenedSong = likeAndViewRepository.findSongsWithMaxListenAmount(cuDate.getMonthValue(), pageable)
                .stream().findFirst().orElseThrow(() -> new NotFoundException("Can't find most listened song"));
        return toSongWithLikeAndViewAmount(mostListenedSong);
    }
    private SongWithViewInMonth toSongWithLikeAndViewAmount(ViewInMonth mostListenedSong) {
        SongWithViewInMonth res = new SongWithViewInMonth();
        res.setSongId(mostListenedSong.getSongId().getId());
        res.setSongName(mostListenedSong.getSongId().getTitle());
        res.setArtistName(mostListenedSong.getSongId().getArtistId().getArtistName());
        res.setAlbumName(mostListenedSong.getSongId().getAlbumId().getTitle());
        res.setListenInMonth(mostListenedSong.getListenAmount());
        return res;
    }

    public int getNumberOfSong() {
        return songRepository.getNumberOfAllNotDeleted(false);
    }

}
