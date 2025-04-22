package englishapp.api.exam_service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import englishapp.api.exam_service.models.Test;

@Repository
public interface TestRepository extends ReactiveMongoRepository<Test, String> {
}
