package englishapp.api.exam_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import englishapp.api.exam_service.models.HistoryTest;
import englishapp.api.exam_service.repositories.HistoryTestRepository;
import reactor.core.publisher.Mono;

@Service
public class HistoryTestService {
    @Autowired
    private HistoryTestRepository historyTestRepository;

    public Mono<HistoryTest> saveHistoryTest(HistoryTest testHistory) {
        return historyTestRepository.save(
                testHistory)
                .map(historyTest -> {
                    return historyTest;
                });
    }
}
