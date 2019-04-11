package comicflips.repositories;

import comicflips.entities.Component;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ComponentRepository extends MongoRepository<Component, String>{

    public Component findByName(String name);

    //?0 -> first parameter, ?1 -> second parameter
    @Query("{'name' : ?0, 'username' : ?1 }")
    public Component findByNameAndUsername(String name, String username);
}