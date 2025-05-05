package englishapp.api.exam_service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import englishapp.api.exam_service.models.HistoryTest;
import reactor.core.publisher.Flux;

@Repository
public interface HistoryTestRepository extends ReactiveMongoRepository<HistoryTest, String> {
    // Custom query method to find history tests by user ID
    Flux<HistoryTest> findByIdUser(String idUser); // Assuming idUser is a field in HistoryTest model
}
