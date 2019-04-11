package comicflips.entities;

import org.springframework.data.annotation.Id;

import java.sql.Blob;

public class Component {

    @Id
    private String id;
    private String name;
    private String canvas;
    private boolean isPublic;
    private String username;

    public Component(){
    }

    public Component(String name, String canvas, boolean isPublic){
        this.setName(name);
        this.setCanvas(canvas);
        this.setPublic(isPublic);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCanvas() {
        return canvas;
    }

    public void setCanvas(String canvas) {
        this.canvas = canvas;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
