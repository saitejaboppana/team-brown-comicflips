package comicflips.repositories;

import comicflips.entities.Component;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ComponentRepository extends MongoRepository<Component, String>{

    public Component findByName(String name);
}
