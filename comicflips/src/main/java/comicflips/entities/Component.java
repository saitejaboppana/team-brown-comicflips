package comicflips.entities;

import org.springframework.data.annotation.Id;

import java.sql.Blob;

public class Component {

    @Id
    private String id;
    private String name;
    private String canvas;
    private String visibility;
    private String username;

    public Component(String name, Blob canvas, String visibility){
        this.setName(name);
        this.setCanvas(canvas);
        this.setVisibility(visibility);
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

    public void setCanvas(Blob canvas) {
        this.canvas = canvas;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
