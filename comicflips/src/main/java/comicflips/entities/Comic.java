package comicflips.entities;

import org.springframework.data.annotation.Id;
import java.sql.Blob;
import java.util.ArrayList;

public class Comic {

    @Id
    private String id;
    private String group;
    private String name;
    private ArrayList<Blob> canvases;
    private ArrayList<String> likes;
    private ArrayList<Comment>  comments;
    private ArrayList<Component> components;
    private String visibility;
    private ArrayList<String> tags;
    private String description;

    public Comic(){
        canvases = new ArrayList<Blob>();
        likes = new ArrayList<String>();
        comments = new ArrayList<Comment>();
        components = new ArrayList<Component>();
        tags = new ArrayList<String>();
    }

    public Comic(String group, String name, String visibility, String description,
                 ArrayList<Blob> canvases, ArrayList<String> likes, ArrayList<Comment> comments,
                 ArrayList<Component> components, ArrayList<String> tags){
        this.group = group;
        this.name = name;
        this.visibility = visibility;
        this.description = description;
        this.canvases = canvases;
        this.likes = likes;
        this.comments = comments;
        this.components = components;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Blob> getCanvases() {
        return canvases;
    }

    public void setCanvases(ArrayList<Blob> canvases) {
        this.canvases = canvases;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<Component> components) {
        this.components = components;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
