package mk.ukim.finki.wp.lab.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import mk.ukim.finki.wp.lab.model.Artist;
import mk.ukim.finki.wp.lab.model.Song;
import mk.ukim.finki.wp.lab.service.SongService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/songDetails")
public class SongDetailsController {
    private final SongService songService;

    public SongDetailsController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping
    public String getSongDetailsPage(HttpServletRequest req, Model model) {
        String trackId = (String) req.getSession().getAttribute("trackId");

        Song song = songService.findByTrackId(trackId);
        Artist selectedArtist = (Artist) req.getSession().getAttribute("selectedArtist");

        if (selectedArtist != null && !song.getPerformers().contains(selectedArtist)) {
            songService.addArtistToSong(selectedArtist, song);
        }

       Integer counter = songService.songViews(song.getId());

        model.addAttribute("trackId", trackId);
        model.addAttribute("songTitle", song.getTitle());
        model.addAttribute("songGenre", song.getGenre());
        model.addAttribute("songRelease", song.getReleaseYear());
        model.addAttribute("songPerformers", song.getPerformers());
        model.addAttribute("viewCount", counter);

        // Return the view name to render the template
        return "songDetails";
    }
}
