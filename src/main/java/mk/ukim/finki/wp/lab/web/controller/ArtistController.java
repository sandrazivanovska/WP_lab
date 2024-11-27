package mk.ukim.finki.wp.lab.web.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wp.lab.model.Artist;
import mk.ukim.finki.wp.lab.service.ArtistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/artist")
public class ArtistController {
    private final ArtistService artistService;
    private final SpringTemplateEngine springTemplateEngine;
    private final ServletContext servletContext;

    public ArtistController(ArtistService artistService, SpringTemplateEngine springTemplateEngine, ServletContext servletContext) {
        this.artistService = artistService;
        this.springTemplateEngine = springTemplateEngine;
        this.servletContext = servletContext;
    }

    @GetMapping
    public String getArtistPage(HttpServletRequest request, Model model){

        List<Artist> artists = artistService.listArtists();
        model.addAttribute("artists", artists);
        String trackId = (String) request.getSession().getAttribute("trackId");
        if (trackId != null) {
            model.addAttribute("trackId", trackId);
        }

        return "artistsList";
    }

    @PostMapping
    protected String addArtist(HttpServletRequest request, Model model){
        long id = Long.parseLong(request.getParameter("artistId"));
        Artist artist = artistService.findById(id);
        request.getSession().setAttribute("selectedArtist",artist);
        String trackId = (String) request.getSession().getAttribute("trackId");
        System.out.println("Track ID in session: " + trackId);
        return "redirect:/songDetails";
    }
}
