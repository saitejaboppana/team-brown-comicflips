package comicflips.entities;

import org.springframework.data.annotation.Id;

public class Comment {

    @Id
    private String user;
    private String comment;

    public Comment(String user, String comment){
        this.setUser(user);
        this.setComment(comment);
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

}
