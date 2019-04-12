package comicflips.entities;

import org.springframework.data.annotation.Id;

import java.sql.Blob;
import java.util.ArrayList;

public class User {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Blob avatar;
    private String email;
    private ArrayList<String> createdComics;
    private ArrayList<String> likedComics;
    private ArrayList<String> following;
    private ArrayList<String> subscriptions;
    private ArrayList<String> createdComponents;

    public User(){
        this.avatar = null;
        this.createdComics = new ArrayList<String>();
        this.likedComics = new ArrayList<String>();
        this.following = new ArrayList<String>();
        this.subscriptions = new ArrayList<String>();
        this.createdComponents = new ArrayList<String>();
    }

    public User(String firstName, String lastName,String username, String password, Blob avatar,
                String email, ArrayList<String> createdComics, ArrayList<String> likedComics,
                ArrayList<String> following, ArrayList<String> subscriptions, ArrayList<String> createdComponents) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.email = email;
        this.createdComics = createdComics;
        this.likedComics = likedComics;
        this.following = following;
        this.subscriptions = subscriptions;
        this.createdComponents = createdComponents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Blob getAvatar() {
        return avatar;
    }

    public void setAvatar(Blob avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getCreatedComics() {
        return createdComics;
    }

    public void setCreatedComics(ArrayList<String> createdComics) {
        this.createdComics = createdComics;
    }

    public ArrayList<String> getLikedComics() {
        return likedComics;
    }

    public void setLikedComics(ArrayList<String> likedComics) {
        this.likedComics = likedComics;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<String> following) {
        this.following = following;
    }

    public ArrayList<String> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(ArrayList<String> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public ArrayList<String> getCreatedComponents() {
        return createdComponents;
    }

    public void setCreatedComponents(ArrayList<String> createdComponents) {
        this.createdComponents = createdComponents;
    }

    public void addComicId(String id){
        createdComics.add(id);
    }

    public void deleteComicId(String id){
        createdComics.remove(id);
    }

    public void addComponentId(String id){
        createdComponents.add(id);
    }

    public void deleteComponentId(String id){
        createdComponents.remove(id);
    }
}
