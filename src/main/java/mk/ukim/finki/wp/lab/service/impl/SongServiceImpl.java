package mk.ukim.finki.wp.lab.service.impl;

import mk.ukim.finki.wp.lab.model.Album;
import mk.ukim.finki.wp.lab.model.Artist;
import mk.ukim.finki.wp.lab.model.Song;
import mk.ukim.finki.wp.lab.model.exceptions.AlbumNotFoundException;
import mk.ukim.finki.wp.lab.model.exceptions.ArtistNotFoundException;
import mk.ukim.finki.wp.lab.model.exceptions.SongNotFoundException;
import mk.ukim.finki.wp.lab.repository.jpa.AlbumRepository;
import mk.ukim.finki.wp.lab.repository.jpa.ArtistRepository;
import mk.ukim.finki.wp.lab.repository.jpa.SongRepository;
import mk.ukim.finki.wp.lab.service.SongService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    public SongServiceImpl(SongRepository songRepository, AlbumRepository albumRepository, ArtistRepository artistRepository) {
        this.songRepository = songRepository;
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
    }

    @Override
    public List<Song> listSongs() {
        return songRepository.findAll();
    }

    public List<Song> findByAlbumId(Long albumId) {
        return this.songRepository.findAllByAlbum_Id(albumId);
    }


    @Override
    public Artist addArtistToSong(Artist a, Song s) {
        // Fetch the song and artist from the database
        Long songId = s.getId();
        Long artistId = a.getId();

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException(songId));
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new ArtistNotFoundException(artistId));

        // Add the artist to the song's performers list
        if (!song.getPerformers().contains(artist)) {
            song.getPerformers().add(artist);
        }

        // Add the song to the artist's songs list
        if (!artist.getSongs().contains(song)) {
            artist.getSongs().add(song);
        }

        // Save the changes
        songRepository.save(song);
        artistRepository.save(artist);

        return artist;
    }

    @Override
    public Song findByTrackId(String trackId) {
        return this.songRepository.findByTrackId(trackId);
    }

    @Override
    public List<Song> searchByTitle(String title) {

        return this.songRepository.findByTitleLike(title);
    }

    @Override
    public Optional<Song> findById(Long id) {
        return this.songRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        this.songRepository.deleteById(id);
    }

    public Optional<Song> save(String trackId, String title, String genre, Integer releaseYear, Long albumId) {

        Album album = this.albumRepository.findById(albumId)
                .orElseThrow(()-> new AlbumNotFoundException(albumId));

        Song song = songRepository.findByTrackId(trackId);

        if (song != null) {
            song.setTitle(title);
            song.setGenre(genre);
            song.setReleaseYear(releaseYear);
            song.setAlbum(album);
        } else {
            song = new Song(trackId, title, genre, releaseYear, new ArrayList<>(), album);
        }

        return Optional.of(this.songRepository.save(song));
    }

}