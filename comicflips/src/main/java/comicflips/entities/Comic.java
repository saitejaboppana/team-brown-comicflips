package comicflips.entities;

import org.springframework.data.annotation.Id;
import java.sql.Blob;
import java.util.ArrayList;

public class Comic {

    @Id
    private String id;
    private String group;
    private String name;
    private String[] canvases;
    private ArrayList<String> likes;
    private ArrayList<Comment>  comments;
    private ArrayList<Component> components;
    private boolean isPublic;
    private ArrayList<String> tags;
    private String description;
    private String username;

    public Comic(){
        likes = new ArrayList<String>();
        comments = new ArrayList<Comment>();
        components = new ArrayList<Component>();
        tags = new ArrayList<String>();
    }

    public Comic(String group, String name, boolean isPublic, String description,
                 String[] canvases, ArrayList<String> likes, ArrayList<Comment> comments,
                 ArrayList<Component> components, ArrayList<String> tags){
        this.group = group;
        this.name = name;
        this.isPublic = isPublic;
        this.description = description;
        this.canvases = canvases;
        this.likes = likes;
        this.comments = comments;
        this.components = components;
        this.tags = tags;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
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

    public String[] getCanvases() {
        return canvases;
    }

    public void setCanvases(String[] canvases) {
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
