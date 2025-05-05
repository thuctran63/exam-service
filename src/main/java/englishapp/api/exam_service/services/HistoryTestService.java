package englishapp.api.exam_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import englishapp.api.exam_service.dto.apiGetHistory.HistoryTestDTO;
import englishapp.api.exam_service.dto.apiGetHistory.OutputParamApiGetHistory;
import englishapp.api.exam_service.models.HistoryTest;
import englishapp.api.exam_service.repositories.HistoryTestRepository;
import englishapp.api.exam_service.repositories.TestInfoRepository;
import reactor.core.publisher.Mono;

@Service
public class HistoryTestService {
    @Autowired
    private HistoryTestRepository historyTestRepository;

    @Autowired
    private TestInfoRepository testInfoRepository;

    public Mono<HistoryTest> saveHistoryTest(HistoryTest testHistory) {
        return historyTestRepository.save(
                testHistory)
                .map(historyTest -> {
                    return historyTest;
                });
    }

    public Mono<OutputParamApiGetHistory> getAllHistoryTest(String idUser) {
        return historyTestRepository.findByIdUser(idUser)
                .flatMap(historyTest -> {
                    HistoryTestDTO dto = new HistoryTestDTO();
                    dto.setIdTest(historyTest.getTestInfo().getIdTest());
                    dto.setScoreTest(historyTest.getScore());
                    dto.setDateTest(historyTest.getDateTest());

                    return testInfoRepository.findById(historyTest.getTestInfo().getIdTest())
                            .map(testInfo -> {
                                dto.setNameTest(testInfo.getTestName());
                                return dto;
                            });
                })
                .collectList()
                .map(historyTestDTOs -> {
                    OutputParamApiGetHistory output = new OutputParamApiGetHistory();
                    output.setHistoryTestList(historyTestDTOs);
                    output.setNumberOfTest(historyTestDTOs.size());

                    double average = historyTestDTOs.stream()
                            .mapToInt(test -> Integer.parseInt(test.getScoreTest()))
                            .average()
                            .orElse(0.0);
                    output.setScoreAverage((int) Math.round(average));

                    return output;
                });
    }
}
