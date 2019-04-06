package comicflips;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories("comicflips.repositories")
public class ComicFlipsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComicFlipsApplication.class, args);
    }

}
