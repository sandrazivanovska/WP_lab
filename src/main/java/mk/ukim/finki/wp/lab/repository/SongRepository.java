package mk.ukim.finki.wp.lab.repository;

import mk.ukim.finki.wp.lab.model.Album;
import mk.ukim.finki.wp.lab.model.Artist;
import mk.ukim.finki.wp.lab.model.Song;
import mk.ukim.finki.wp.lab.model.exceptions.SongNotFound;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository

public class SongRepository {
    private List<Song> songs;
    private final AlbumRepository albumRepository;

    public SongRepository(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
        List<Album> albums = albumRepository.findAll();

        songs = new ArrayList<>();

        songs.add(new Song("track6", "Dreams", "Rock", 1977, new ArrayList<>(), albums.get(1)));
        songs.add(new Song("track7", "Smells Like Teen Spirit", "Grunge", 1991, new ArrayList<>(), albums.get(0)));
        songs.add(new Song("track8", "Shake It Off", "Pop", 2014, new ArrayList<>(), albums.get(2)));
        songs.add(new Song("track9", "So What", "Jazz", 1959, new ArrayList<>(), albums.get(3)));
        songs.add(new Song("track10", "Get Lucky", "Electronic", 2013, new ArrayList<>(), albums.get(4)));
    }

    public List<Song> findAll(){
        return songs;
    }

    public Song findByTrackId(String trackId){
        return songs.stream()
                .filter(song -> song.getTrackId().equals(trackId))
                .findFirst()
                .orElse(null);
    }

    public Artist addArtistToSong(Artist artist, Song song){
        song.getPerformers().add(artist);
        return artist;
    }

    public List<Song> findByTitle(String title){
        return songs.stream()
                .filter(song -> song.getTitle() != null &&
                        song.getTitle().toLowerCase().contains(title.toLowerCase()))
                .toList();
    }
    public Optional<Song> findById(Long id){
        return songs.stream()
                .filter(song -> song.getId().equals(id))
                .findFirst();
    }

    public Optional<Song> save(String trackId, String title, String genre,
                               Integer releaseYear, Album album){
        songs.removeIf(i-> i.getTrackId().equals(trackId));
        Song song = new Song(trackId, title, genre, releaseYear, new ArrayList<>(), album);
        songs.add(song);
        return Optional.of(song);
    }

    public void deleteById(Long id){
        songs.removeIf(i-> i.getId().equals(id));
    }

    public Integer songCounts(Long id)
    {
        Song song = songs.stream().filter(i->i.getId().equals(id)).findFirst().orElse(null);
        if(song!=null)
        {
            Integer views = song.getCounter();
            song.setCounter(++views);
            return song.getCounter();
        }
        else{
            throw new SongNotFound(id);
        }
    }
}
