package comicflips.entities;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.springframework.data.annotation.Id;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

public class Comic {

    @Id
    private String id;
    private String group;
    private String name;
    private List<String> canvases;
    private int likes;
    private ArrayList<Comment>  comments;
    private ArrayList<Component> components;
    private boolean isPublic;
    private ArrayList<String> tags;
    private String description;
    private String username;
    private String dateTime;
    private User author;
    private boolean isExternal;
    private ArrayList<String> uploadLinks;

    public Comic(){
        likes = 0;
        comments = new ArrayList<Comment>();
        components = new ArrayList<Component>();
        tags = new ArrayList<String>();
        dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss"));
        isExternal = false;
    }

    public Comic(String group, String name, boolean isPublic, String description,
                 List<String> canvases, int likes, ArrayList<Comment> comments,
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean getIsPublic() {
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

    public List<String> getCanvases() {
        return canvases;
    }

    public void setCanvases(List<String> canvases) {
        this.canvases = canvases;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
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

    public void incLikesByOne(){
        likes += 1;
    }
    public void decLikesByOne(){
        likes -= 1;
    }

    public void addComment(Comment c){
        comments.add(c);
    }

    public void deleteComment(Comment c){
        String user = c.getUser();
        String comment = c.getComment();
        for(Comment com: comments){
            if(com.getUser().equals(user) && com.getComment().equals(comment)){
                comments.remove(com);
                break;
            }
        }
//        comments.remove(c);
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public boolean getIsExternal() {
        return isExternal;
    }

    public void setExternal(boolean external) {
        isExternal = external;
    }

    public ArrayList<String> getUploadLinks() {
        return uploadLinks;
    }

    public void setUploadLinks(ArrayList<String> uploadLinks) {
        this.uploadLinks = uploadLinks;
    }
}
