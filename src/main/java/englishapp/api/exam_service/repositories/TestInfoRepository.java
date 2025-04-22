package englishapp.api.exam_service.repositories;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import englishapp.api.exam_service.models.TestInfo;
import reactor.core.publisher.Flux;

@Repository
public interface TestInfoRepository extends ReactiveMongoRepository<TestInfo, String> {
    @Query("{ 'year': ?0 }")
    Flux<TestInfo> findByYear(String year);
}
