package comicflips.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;

@Controller
public class ComicFlipsController {

    @GetMapping("/")
    ModelAndView indexPage(){
        return new ModelAndView("index");
    }

    @GetMapping("/login")
    ModelAndView loginPage(){
        return new ModelAndView("login");
    }

    @GetMapping("/register")
    ModelAndView registerPage(){
        return new ModelAndView("register");
    }

    @GetMapping("/create")
    ModelAndView createComicPage(){
        return new ModelAndView("create");
    }

    @GetMapping("/component")
    ModelAndView createComponentPage(){
        return new ModelAndView("component");
    }

    @GetMapping("/profile")
    ModelAndView profilePage(){
        return new ModelAndView("profile");
    }

    @GetMapping("/comic")
    ModelAndView viewSingleComic(){
        return new ModelAndView("comic");
    }


}