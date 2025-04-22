package englishapp.api.exam_service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import englishapp.api.exam_service.models.Answer;

@Repository
public interface AnswerRepository extends ReactiveMongoRepository<Answer, String> {
}
