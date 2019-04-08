package comicflips.repositories;

import comicflips.entities.Comic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ComicRepository extends MongoRepository<Comic, String>{

    public Comic findByGroup(String group);

    public Comic findByTag(String[] tags);

    public Comic findByName(String name);
}
