package comicflips.controllers;

import comicflips.MongoUserDetailsService;
import comicflips.entities.Comic;
import comicflips.entities.Comment;
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

    @GetMapping("/forgot")
    String forgotPass(Principal user){

        if(user != null){
            return "redirect:/";
        }

        return "forgotpass";
    }

    /**
     * When a user registers, this endpoint will get called. This method will create a new user object and set all
     * of the provided information from the register page. This method will then redirect the user to the login
     * page. However, if the username already exists, the end user will not be able to create the account and the
     * page will not redirect.
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     * @param username The username that the user wants to have for the account
     * @param password The password for the account
     * @param email The users email
     * @param question The users security question
     * @param answer The users security answer
     * @return The model and view that will redirect the user either to the register page or the login page
     */
    @PostMapping("/addUser")
    ModelAndView addUser(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String username,
                         @RequestParam String password, @RequestParam String email, @RequestParam String question,
                         @RequestParam String answer){
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
        newUser.setQuestion(question);
        newUser.setAnswer(answer);
        userDetailsService.saveUser(newUser);
        mv.setViewName("login");
        return mv;
    }

    /**
     * When logging in, if the user info exists, the user will be redirected to the home page. Otherwise, they will
     * be sent back to the login page
     * @param user The user info for the person trying to login
     * @return The login page or the index page
     */
    @GetMapping("/login")
    String login(Principal user){

        if(user != null){
            return "redirect:/";
        }

        return "login";
    }

    /**
     * Loads all of the comics to the index page, regardless of whether the user is logged in or not. This gets the
     * comics from the database, puts in the the modelandview object, and returns it.
     * @param auth The Spring Authentication object
     * @return THe ModelAndView object with all the necessary info to display the comics and to create the index page.
     */
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
        mv.addObject("title", "All Comics");
        return mv;
    }

    /**
     * Takes logged in user to the create comic page. Also sends components from the database and the users
     * previously made groups to help ease the comic creating process.
     * @param auth The Spring Authentication object
     * @return The ModelAndView with the components, groups, and buttons. Directs to the create html.
     */
    @GetMapping("/create")
    ModelAndView createComicPage(Authentication auth){
        ModelAndView mv = new ModelAndView("create");
        User currentUser = userDetailsService.getUser(auth.getName());
        mv.addObject("components",componentRepository.findAll());
        mv.addObject("groups", currentUser.getCreatedGroups());
        mv.addObject("genres", "");
        mv.addObject("button", "Publish");
        return mv;
    }

    /**
     * Takes logged in user to create a comic component
     * @return ModelAndView that takes user to the create component page
     */
    @GetMapping("/component")
    ModelAndView createComponentPage(){
        return new ModelAndView("component");
    }

    /**
     * Endpoint for displaying user profile page. This page will show user info, comics made by user, and
     * comic components made by user.
     * @param auth  The Spring Authentication object
     * @return ModelAndView
     */
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

    /**
     * When the user makes edits to their profile,this method will update the database with the corresponding edits.
     * It is important to note that the user cannot change the username.
     * @param auth The Spring Authentication object
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     * @param password The password for the account
     * @param email The users email
     * @return The string that will redirect user back to the user profile with all the changes they made.
     */
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

    /**
     * When user clicks a comic, this endpoint will direct the user to the comic page that will contain all the
     * info for that comic.
     * @param id The id for the comic
     * @param auth The Spring Authentication object
     * @return ModelAndView will all of the comic information.
     */
    @GetMapping("/comic/{id}")
    ModelAndView getComic(@PathVariable String id,
                          Authentication auth){
        Comic c = comicRepository.findById(id).get();
        ModelAndView mv = new ModelAndView("comic");
        mv.addObject("comic", c);
        String isLiked;
        String userName= "";
        String group = c.getGroup();
        String subscribeButton = "Subscribe";
        if(auth == null){
            isLiked = "";
        }
        else {
            userName = auth.getName();
            User user = userDetailsService.getUser(userName);
            isLiked = Boolean.toString(user.getLikedComics().contains(id));
            if(user.getSubscriptions().contains(group)){
                subscribeButton = "Unsubscribe";
            }
        }
        mv.addObject("liked", isLiked);
        mv.addObject("user", userName);
        mv.addObject("group", group);
        mv.addObject("subButton", subscribeButton);
        return mv;
    }

    /**
     * Used to edit the liked comics arraylist in the user object. Takes a boolean
     * telling whether the user liked the comic and the comicID of the comic that was
     * liked/unliked.
     * @param liked Boolean for whether the user liked or disliked the comic
     * @param comicID The id for the comic
     * @param auth The Spring Authentication object
     * @return Success string if POST request was successful, and error message if
     * user is not logged in.
     */
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

    /**
     * When user makes the comic, a post request will be performed that will add the comic and all of its other
     * info into the database
     * @param title Title of the comic
     * @param about Description of the comic
     * @param canvases All of the canvases for the comic
     * @param isPublic Boolean for whether the comic was made public or not
     * @param group The comic's group
     * @param auth The Spring Authentication object
     * @return Success string if POST request was successful, and error message if user is not logged in.
     */
    @PostMapping("/addComic")
    @ResponseBody
    String addComic(@RequestParam("title") String title,
                    @RequestParam("about") String about,
                    @RequestParam("canvases[]") List<String> canvases,
                    @RequestParam("isPublic") boolean isPublic,
                    @RequestParam("group") String group,
                    @RequestParam("tags[]") ArrayList<String> tags,
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
            c.setGroup(group);
            c.setPublic(isPublic);//for now, lets have this hardcoded as true
            c.setTags(tags);

        }
        comicRepository.save(c);
        User author = userRepository.findByUsername(username);
        author.addComicId(c.getId());
        author.addToCreatedGroups(group);
        userRepository.save(author);
        return "Success";
    }

    /**
     * Updates a previously made comic. User supplies any new information, and a POST request will be sent to the
     * database and will update the comic accordingly.
     * @param oldTitle The old title for the comic
     * @param title The new title for the comic
     * @param about The description for the comic
     * @param canvases The comic's canvases
     * @param isPublic Boolean for whether the comic is public or not
     * @param auth The Spring Authentication object
     * @return Update Complete message
     */
    @PostMapping("/updateComic")
    @ResponseBody
    String updateComic(@RequestParam("oldTitle") String oldTitle,
                       @RequestParam("title") String title,
                       @RequestParam("about") String about,
                       @RequestParam("canvases[]") List<String> canvases,
                       @RequestParam("isPublic") boolean isPublic,
                       @RequestParam("group") String group,
                       @RequestParam("tags[]") ArrayList<String> tags,
                       Authentication auth){

        if(canvases.get(1).equals("bug")){
            canvases.remove(1);
        }

        String username = auth.getName();
        Comic c = comicRepository.findByNameAndUsername(oldTitle, username);
        c.setName(title);
        c.setDescription(about);
        c.setCanvases(canvases);
        c.setPublic(isPublic);
        c.setGroup(group);
        c.setTags(tags);
        comicRepository.save(c);
        return "update complete";
    }

    /**
     * Deletes comic from the database using the title of the comic
     * @param title Title of the comic
     * @param auth The Spring Authentication object
     * @return Success message
     */
    @PostMapping("/deleteComic")
    String deleteComic(@RequestParam String title, Authentication auth){
        String userName = auth.getName();
        Comic c = comicRepository.findByNameAndUsername(title, userName);
        comicRepository.delete(c);
        return "Success";
    }

    /**
     * Deletes comic from the database using the id of the comic.
     * @param id Id of the comic
     * @return String that redirects user to the profile page.
     */
    @PostMapping("/deleteComic/{id}")
    String deleteComicById(@PathVariable String id){
        Comic c = comicRepository.findById(id).get();
        comicRepository.delete(c);
        return "redirect:/profile";
    }

    /**
     * Adds component to the database. This component can be in the comics themselves.
     * @param title Title of the Component
     * @param canvas The canvas for the component
     * @param auth The Spring Authentication object
     * @return Success message
     */
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

    /**
     * Deletes component from the database using the component title
     * @param title Title of the component
     * @param auth The Spring Authentication object
     * @return Success message
     */
    @PostMapping("/deleteComponent")
    String deleteComponent(@RequestParam String title, Authentication auth){
        String userName = auth.getName();
        Component c = componentRepository.findByNameAndUsername(title, userName);
        componentRepository.delete(c);
        return "Success";
    }

    /**
     * Deletes component from the database using the component id
     * @param id The id of the component
     * @return String to redirect to profile page
     */
    @PostMapping("/deleteComponent/{id}")
    String deleteComponentById(@PathVariable String id){
        Component c = componentRepository.findById(id).get();
        componentRepository.delete(c);
        return "redirect:/profile";
    }

    /**
     * Adds comment to the comic object in the database.
     * @param comment Comment message
     * @param comicID Id of the comic
     * @param auth The Spring Authentication object
     * @return Success message
     */
    @PostMapping("/addComment")
    @ResponseBody
    String addComment(@RequestParam("comment") String comment,@RequestParam("id") String comicID ,Authentication auth){
        Comment c = new Comment(auth.getName(),comment);
        Comic comic = comicRepository.findById(comicID).get();
        comic.addComment(c);
        comicRepository.save(comic);
        return "Success!";
    }

    /**
     * Deletes comment from the comic object in the database.
     * @param comment Comment message
     * @param comicID Id of the comic
     * @param auth The Spring Authentication object
     * @return Success message
     */
    @PostMapping("/deleteComment")
    String deleteComponent(@RequestParam("comment") String comment,@RequestParam("id") String comicID ,Authentication auth) {
        Comic comic = comicRepository.findById(comicID).get();
        comic.deleteComment(new Comment(auth.getName(), comment));
        return "Success!";
    }

    /**
     * Takes user to the create page, where they can edit a previously created comic.
     * @param id Id for the comic
     * @return ModelAndView that will redirect to the create page, with all of the info for comic the user
     * wants to edit.
     */
    @GetMapping("/editComic/{id}")
    String editComicPage(@PathVariable String id, Authentication auth, Model mv){
        Comic comicToEdit = comicRepository.findById(id).get();
        User currentUser = userDetailsService.getUser(auth.getName());
        if(!currentUser.getUsername().equals(comicToEdit.getUsername())){
            return "redirect:/";
        }
//        ModelAndView mv = new ModelAndView("create");
        mv.addAttribute("title", comicToEdit.getName());
        mv.addAttribute("about",comicToEdit.getDescription());
        mv.addAttribute("canvases", comicToEdit.getCanvases());
        mv.addAttribute("isPublic", comicToEdit.getIsPublic());
        mv.addAttribute("button", "Update");
        mv.addAttribute("groups", currentUser.getCreatedGroups());
        mv.addAttribute("selectedGroup", comicToEdit.getGroup());
        mv.addAttribute("components",componentRepository.findAll());
        mv.addAttribute("genres", comicToEdit.getTags());
        System.out.println("****selectedGroup : " + comicToEdit.getGroup());
        return "create";
    }

    /**
     * Subscribes user to the group
     * @param group Group that the user wants to subscribe to.
     * @param auth The Spring Authentication object
     * @return Subscription complete message
     */
    @PostMapping("/subscribe")
    @ResponseBody
    String subscribeToGroup(@RequestParam String group, Authentication auth){
        String username = auth.getName();
        User currentUser = userDetailsService.getUser(username);
        currentUser.addToSubscribed(group);
        userDetailsService.updateUser(currentUser);
        return "subscription success";
    }

    /**
     * Unsubscribe user from the group
     * @param group The comic group the user wants to unsubscribe to.
     * @param auth The Spring Authentication object
     * @return Unsubscription complete message
     */
    @PostMapping("/unsubscribe")
    @ResponseBody
    String unsubscribeFromGroup(@RequestParam String group, Authentication auth){
        String username = auth.getName();
        User currentUser = userDetailsService.getUser(username);
        currentUser.removeFromSubscribed(group);
        userDetailsService.updateUser(currentUser);
        return "unsubscribe success";
    }

    /**
     * Shows the subscribed comics for the particular group to the user.
     * @param group The comic group
     * @param auth The Spring Authentication object
     * @param mv Index page ModelAndView
     * @return String that will take user to the index page
     */
    @GetMapping("/subscribed/{group}")
    String getSubscribedPage(@PathVariable String group, Authentication auth, Model mv){
        String username = auth.getName();
        User user = userDetailsService.getUser(username);
        if(!user.getSubscriptions().contains(group)){
            return "redirect:/";
        }
        List <Comic> comics = comicRepository.findByGroup(group, new Sort(Sort.Direction.ASC, "dateTime"));
        List<Boolean> likes = Arrays.asList(new Boolean[comics.size()]);
        Collections.fill(likes, Boolean.FALSE);
        List <String> userLikedComics = user.getLikedComics();
        likes = new ArrayList<Boolean>();

        for (Comic c: comics) {
            if(userLikedComics.contains(c.getId())){
                likes.add(true);
            }else{
                likes.add(false);
            }
        }
        
        System.out.println("username:" + username);
        mv.addAttribute("user", username);
        mv.addAttribute("comics",comics);
        mv.addAttribute("likes", likes);
        mv.addAttribute("title", group);
        return "index";
    }

    /**
     * Gets the security question that the user put in upon registering
     * @param username The user's username
     * @return The user's security question
     */
    @GetMapping("/securityQuestion")
    @ResponseBody
    String getSecurityQuestion(@RequestParam String username){
        //System.out.println("entering here");
        User user = userDetailsService.getUser(username);
        if(user == null){
            //System.out.println("Entering if statement");
            return "Invalid Username";
        }
        String question = user.getQuestion();
        return question;
    }

    /**
     * Checks that the answer to the security question is correct.
     * @param username The user's username
     * @param answer The answer that the user put in
     * @return String that tells if the user put the right answer or not.
     */
    @GetMapping("/securityAnswer")
    @ResponseBody
    String checkSecurityQuestion(@RequestParam String username, @RequestParam String answer){
        //System.out.println("entering answer");
        User user = userDetailsService.getUser(username);
        String ans = user.getAnswer();
        if(ans.equals(answer)){
            return "Success";
        }
        else{
            return "Incorrect Answer";
        }
    }

    /**
     * Changes the password of the user account
     * @param username The user's username
     * @param password The user's new password
     * @return Success String
     */
    @PostMapping("/changePassword")
    @ResponseBody
    String changePassword(@RequestParam String username, @RequestParam String password){
        User user = userDetailsService.getUser(username);
        user.setPassword(password);
        userDetailsService.saveUser(user);
        return "Success";
    }
}
