package mk.ukim.finki.wp.lab.service;

import mk.ukim.finki.wp.lab.model.Artist;
import mk.ukim.finki.wp.lab.model.Song;

import java.util.List;
import java.util.Optional;

public interface SongService {
    List<Song> listSongs();
    Artist addArtistToSong(Artist artist, Song song);
    public Song findByTrackId(String trackId);
    Optional<Song> save(String trackId, String title, String genre, Integer releaseYear, Long albumId);
    public List<Song> searchByTitle(String title);
    public Optional<Song> findById(Long id);
    void deleteById(Long id);
    public Integer songViews(Long id);
}
