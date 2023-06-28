package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Profile;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.ProfileService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.Date;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class MainController {
    private Profile loggedProfile;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private ProfileService profileService;

    @RequestMapping
    public String index(Model model) {
        fillModel(model);
        return "index";
    }

    @RequestMapping("/login")
    public String login(String login, String password) {
        Profile databaseProfile = profileService.getProfile("PegSolitaire", login);
        if (databaseProfile != null && databaseProfile.getPassword().equals(password)) {
            loggedProfile = databaseProfile;
        }else{
            loggedProfile = new Profile(login, password, "PegSolitaire");
            profileService.addProfile(loggedProfile);
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAA");
        }
        System.out.println(loggedProfile.getLogin());
        return "redirect:/";
    }

    private void fillModel(Model model) {
        model.addAttribute("message", "This is message");
        model.addAttribute("scores", scoreService.getTopScores("PegSolitaire"));
        //model.addAttribute("htmlField", getHtmlField());
    }

    @RequestMapping("/logout")
    public String logout() {
        loggedProfile = null;
        return "redirect:/";
    }

    public Profile getLoggedProfile() {
        return loggedProfile;
    }

    public boolean isLogged() {
        return loggedProfile != null;
    }

    @RequestMapping("/add-comment")
    public String writeComment(String comment) {
        if (isLogged() && comment != null && !comment.isEmpty())
            commentService.addComment(new Comment(getLoggedProfile().getLogin(), "PegSolitaire", comment, new Date()));
        return "redirect:/";
    }

    @RequestMapping("/add-rating")
    public String writeRating(int rating) {
        if (isLogged())
            ratingService.addRating(new Rating(getLoggedProfile().getLogin(), "PegSolitaire", rating, new Date()));
        return "redirect:/";
    }
}
