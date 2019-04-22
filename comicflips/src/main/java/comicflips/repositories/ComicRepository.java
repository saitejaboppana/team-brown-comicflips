package comicflips.repositories;

import comicflips.entities.Comic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ComicRepository extends MongoRepository<Comic, String>{

    public Comic findByGroup(String group);

    public Comic findByName(String name);

    @Query("{'name' : ?0, 'username' : ?1 }")
    public Comic findByNameAndUsername(String name, String username);

    public List<Comic> findByUsername(String username);
}
