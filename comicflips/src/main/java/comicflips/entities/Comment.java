package comicflips.entities;

import org.springframework.data.annotation.Id;

public class Comment {

    @Id
    private String user;
    private String comment;
    private String userImage;

    public Comment(String user, String comment){
        this.setUser(user);
        this.setComment(comment);
        this.userImage = "";
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
