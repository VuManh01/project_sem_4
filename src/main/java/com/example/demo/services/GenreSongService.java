package com.example.demo.services;

import com.example.demo.dto.request.NewOrUpdateGenreSong;
import com.example.demo.entities.GenreSong;
import com.example.demo.entities.Genres;
import com.example.demo.entities.Songs;
import com.example.demo.ex.AlreadyExistedException;
import com.example.demo.ex.NotFoundException;
import com.example.demo.repositories.GenreSongRepository;
import com.example.demo.repositories.GenresRepository;
import com.example.demo.repositories.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreSongService {
    @Autowired
    private GenreSongRepository genreSongRepository;

    @Autowired
    private GenresRepository genresRepository;

    @Autowired
    private SongRepository songRepository;

    public GenreSongService(GenreSongRepository genreSongRepository) {
        this.genreSongRepository = genreSongRepository;
    }

    public NewOrUpdateGenreSong addNewGenreSong(NewOrUpdateGenreSong request) {
        Optional<GenreSong> existingGenreSong = genreSongRepository.findByGenreIdAndSongId(request.getGenreId(), request.getSongId());
        if (existingGenreSong.isPresent()){
            throw new AlreadyExistedException("A GenreSong already exists");
        }

        Optional<Genres> genres = genresRepository.findByIdAndIsDeleted(request.getGenreId(), false);
        if(genres.isEmpty()){
            throw new NotFoundException("Can't find any genre with id: " +request.getGenreId());
        }

        Optional<Songs> songs = songRepository.findByIdAndIsDeleted(request.getSongId(), false);
        if(songs.isEmpty()){
            throw new NotFoundException("Can't find any song with id: " +request.getSongId());
        }

        GenreSong newGenreSong = new GenreSong(
                genres.get(),
                songs.get()
        );

        genreSongRepository.save(newGenreSong);
        return request;
    }


    public void deleteByGenreIdAndSongId(int genreId, int songId){
        Optional<GenreSong> genreSong = genreSongRepository.findByGenreIdAndSongId(genreId, songId);
        if(genreSong.isEmpty()){
            throw new NotFoundException("Can't find any genre-song");
        }
        GenreSong existing = genreSong.get();
        genreSongRepository.delete(existing);
    }

    // viet method ben tren truoc roi moi den cai nay
    public void updateGenresForSong(Integer songId, List<Integer> newGenreIds) {
    List<Integer> oldList = genreSongRepository.findBySongId(songId, false)
            .stream()
            .map(it -> it.getGenreId().getId())
            .toList();
    if(newGenreIds.isEmpty()){
        oldList.forEach(it -> deleteByGenreIdAndSongId(it, songId));
        return;
    }

    List<Integer> removeIds = oldList.stream()
            .filter(item -> !newGenreIds.contains(item))
            .toList();

    List<Integer> toAddIds = newGenreIds.stream()
            .filter(item -> !oldList.contains(item))
            .toList();

    toAddIds.stream()
            .map( it -> new NewOrUpdateGenreSong(null, it, songId))
            .forEach(this::addNewGenreSong);

    removeIds.forEach(it -> deleteByGenreIdAndSongId(it, songId));
    }
}


