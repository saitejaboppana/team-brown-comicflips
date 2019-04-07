package comicflips.controllers;

import comicflips.MongoUserDetailsService;
import comicflips.entities.User;
import comicflips.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;

import javax.jws.WebParam;
import java.security.Principal;

@Controller
public class ComicFlipsController {

    @Autowired
    private MongoUserDetailsService userDetailsService;

    @GetMapping("/register")
    String register(Principal user){

        if(user != null){
            return "redirect:/";
        }

        return "register";
    }

    @PostMapping("/addUser")
    ModelAndView addUser(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String username,
                         @RequestParam String password, @RequestParam String email){
        System.out.println("Entering registering1");
        ModelAndView mv = new ModelAndView();
        if(userDetailsService.loadUserByUsername(username) != null){
            mv.addObject("error", "User Already Exists");
            mv.setViewName("register");
            return mv;
        }
        System.out.println("Entering registering2");
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        userDetailsService.saveUser(newUser);
        mv.setViewName("login");
        return mv;
    }

    @GetMapping("/login")
    String login(Principal user){

        if(user != null){
            return "redirect:/";
        }

        return "login";
    }


    @GetMapping("/")
    ModelAndView indexPage(){
        return new ModelAndView("index");
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
    ModelAndView profilePage(Authentication auth){
        ModelAndView mv = new ModelAndView("profile");
        String username = auth.getName();
        User user = userDetailsService.getUser(username);
        mv.addObject("user", user);
        return mv;
    }

    @PostMapping("/updateUser")
    String updateProfile(Authentication auth, @RequestParam String firstName, @RequestParam String lastName,
                         @RequestParam String username, @RequestParam String password, @RequestParam String email) {
        String userName = auth.getName();
        User user = userDetailsService.getUser(userName);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUsername(username);
        if(!password.equals("hidden")) {
            user.setPassword(password);
        }
        userDetailsService.updateUser(user);
        return "redirect:/profile";
    }



    @GetMapping("/comic")
    ModelAndView viewSingleComic(){
        return new ModelAndView("comic");
    }



}
