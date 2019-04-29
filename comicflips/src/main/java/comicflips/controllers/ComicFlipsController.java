package comicflips.controllers;

import comicflips.MongoUserDetailsService;
import comicflips.entities.Comic;
import comicflips.entities.Component;
import comicflips.entities.User;
import comicflips.repositories.ComicRepository;
import comicflips.repositories.ComponentRepository;
import comicflips.repositories.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;

import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
public class ComicFlipsController {

    @Autowired
    private MongoUserDetailsService userDetailsService;

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ComponentRepository componentRepository;

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
    ModelAndView indexPage(Authentication auth){
        ModelAndView mv = new ModelAndView("index");
        String username= "";
        List <Comic> comics = comicRepository.findByIsPublic(true, new Sort(Sort.Direction.DESC, "dateTime"));
        List<Boolean> likes = Arrays.asList(new Boolean[comics.size()]);
        Collections.fill(likes, Boolean.FALSE);
        System.out.println("before if");
        if(auth != null){
            username = auth.getName();
            User user = userDetailsService.getUser(username);
            List <String> userLikedComics = user.getLikedComics();
            likes = new ArrayList<Boolean>();

            for (Comic c: comics) {
                if(userLikedComics.contains(c.getId())){
                    likes.add(true);
                }else{
                    likes.add(false);
                }
            }

        }

        System.out.println("username:" + username);
        mv.addObject("user", username);
        mv.addObject("comics",comics);
        mv.addObject("likes", likes);
//        mv.addObject("comics",comicRepository.findAll(new Sort(Sort.Direction.DESC, "dateTime")));
        return mv;
    }

    @GetMapping("/create")
    ModelAndView createComicPage(){
        ModelAndView mv = new ModelAndView("create");
        mv.addObject("components",componentRepository.findAll());
        return mv;
    }
    @GetMapping("/component")
    ModelAndView createComponentPage(){
        return new ModelAndView("component");
    }

    @GetMapping("/profile")
    ModelAndView profilePage(Authentication auth){
        ModelAndView mv = new ModelAndView("profile");
        String username = auth.getName();
        System.out.println("server side user" +  username);
        User user = userDetailsService.getUser(username);
        mv.addObject("user", user);
        mv.addObject("comics",comicRepository.findByUsername(username));
        mv.addObject("components",componentRepository.findByUsername(username));
        return mv;
    }

    @PostMapping("/updateUser")
    String updateProfile(Authentication auth, @RequestParam String firstName, @RequestParam String lastName,
                         @RequestParam String password, @RequestParam String email) {
        String userName = auth.getName();
        User user = userDetailsService.getUser(userName);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUsername(userName);
        if(!password.equals("hidden")) {
            user.setPassword(password);
            userDetailsService.updateUser(user);
        }
        else{
            userRepository.save(user);
        }
        System.out.println("Username: " + userName);
        System.out.println("Password: " + password);
        //userDetailsService.updateUser(user);
        return "redirect:/profile";
    }


    @GetMapping("/comic")
    ModelAndView viewSingleComic(){
        return new ModelAndView("comic");
    }

    @GetMapping("/comic/{id}")
    ModelAndView getComic(@PathVariable String id,
                          Authentication auth){
        Comic c = comicRepository.findById(id).get();
        ModelAndView mv = new ModelAndView("comic");
        mv.addObject("comic", c);
        String isLiked;
        if(auth == null){
            isLiked = "";
        }
        else {
            String userName = auth.getName();
            User user = userDetailsService.getUser(userName);
            isLiked = Boolean.toString(user.getLikedComics().contains(id));
        }
        mv.addObject("liked", isLiked);
        return mv;
    }

    //Used to edit the liked comics arraylist in the user object. Takes a boolean
    //telling whether the user liked the comic and the comicID of the comic that was
    //liked/unliked.
    @PostMapping("/editLike")
    @ResponseBody
    String editLike(@RequestParam("liked") boolean liked,
                  @RequestParam("comicID") String comicID,
                  Authentication auth){
        System.out.println("edit");
        if(auth == null){
            System.out.println("Not logged in");
            return "You must be logged in";
        }
        System.out.println("Logged in");
        System.out.println("liked: "  + liked + " comicID: " + comicID);
        String userName = auth.getName();
        User user = userDetailsService.getUser(userName);
        boolean checkWithinComic = user.getLikedComics().contains(comicID);
        if(liked != checkWithinComic){
            Comic c = comicRepository.findById(comicID).get();
            if(liked){
                //System.out.println("liked");
                user.addToLikes(comicID);
                userDetailsService.updateUser(user);
                c.incLikesByOne();
                comicRepository.save(c);
            }
            else{
                //System.out.println("not liked");
                user.removeFromLikes(comicID);
                userDetailsService.updateUser(user);
                c.decLikesByOne();
                comicRepository.save(c);
            }
        }
        return "success";
    }

    @PostMapping("/addComic")
    @ResponseBody
    String addComic(@RequestParam("title") String title,
                    @RequestParam("about") String about,
                    @RequestParam("canvases[]") List<String> canvases,
                    @RequestParam("isPublic") boolean isPublic,
                    Authentication auth){
        System.out.println(canvases.size());
        if(canvases.get(1).equals("bug")){
            canvases.remove(1);
        }
        Comic c = comicRepository.findByName(title);
        String username = auth.getName();
        if (c != null && username == c.getUsername()){
            return "Comic with this name already exists!";
        }
        else{
            c = new Comic();
            c.setName(title);
            c.setDescription(about);
            c.setCanvases(canvases);
            c.setUsername(username);
            c.setPublic(isPublic);//for now, lets have this hardcoded as true

        }
        comicRepository.save(c);
        User author = userRepository.findByUsername(username);
        author.addComicId(c.getId());
        userRepository.save(author);
        return "Success";
    }

    @PostMapping("/deleteComic")
    String deleteComic(@RequestParam String title, Authentication auth){
        String userName = auth.getName();
        Comic c = comicRepository.findByNameAndUsername(title, userName);
        comicRepository.delete(c);
        return "Success";
    }

    @PostMapping("/deleteComic/{id}")
    String deleteComicById(@PathVariable String id){
        Comic c = comicRepository.findById(id).get();
        comicRepository.delete(c);
        return "redirect:/profile";
    }

    @PostMapping("/addComponent")
    @ResponseBody
    String addComponent(@RequestParam String title, @RequestParam String canvas, Authentication auth){
        Component c = componentRepository.findByName(title);
        String username = auth.getName();
        if (c != null && username == c.getUsername()){
            return "Component with this name already exists!";
        }
        else{
            c = new Component();
            c.setName(title);
            c.setCanvas(canvas);
            c.setUsername(username);
            c.setPublic(true);//for now, lets have this hardcoded as true
        }
        componentRepository.save(c);
        User author = userRepository.findByUsername(username);
        author.addComponentId(c.getId());
        userRepository.save(author);
        return "Success";
    }

    @PostMapping("/deleteComponent")
    String deleteComponent(@RequestParam String title, Authentication auth){
        String userName = auth.getName();
        Component c = componentRepository.findByNameAndUsername(title, userName);
        componentRepository.delete(c);
        return "Success";
    }

    @PostMapping("/deleteComponent/{id}")
    String deleteComponentById(@PathVariable String id){
        Component c = componentRepository.findById(id).get();
        componentRepository.delete(c);
        return "redirect:/profile";
    }

}
