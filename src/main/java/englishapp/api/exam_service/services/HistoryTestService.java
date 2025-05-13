package englishapp.api.exam_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import api.common.englishapp.utils.DateUtils;
import englishapp.api.exam_service.dto.apiGetAllHistoryTest.OutputParamGetAllHistoryTest;
import englishapp.api.exam_service.dto.apiGetHistory.HistoryTestDTO;
import englishapp.api.exam_service.dto.apiGetHistory.OutputParamApiGetHistory;
import englishapp.api.exam_service.dto.apiGetTestHistory.OutputParamApiGetTestHistory;
import englishapp.api.exam_service.dto.apiGetTestHistory.OutputParamApiGetTestHistory.UserAnswer;
import englishapp.api.exam_service.models.HistoryTest;
import englishapp.api.exam_service.repositories.AnswerRepository;
import englishapp.api.exam_service.repositories.HistoryTestRepository;
import englishapp.api.exam_service.repositories.TestInfoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class HistoryTestService {
    @Autowired
    private HistoryTestRepository historyTestRepository;

    @Autowired
    private TestInfoRepository testInfoRepository;

    @Autowired
    private AnswerRepository answerRepository;

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
                    dto.setIdTestHistory(historyTest.getIdTestHistory());
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

    public Mono<OutputParamApiGetTestHistory> getTestHistory(String idTestHistory) {
        return historyTestRepository.findById(idTestHistory)
                .flatMap(historyTest -> {
                    OutputParamApiGetTestHistory output = new OutputParamApiGetTestHistory();
                    output.setUserAnswer(new UserAnswer(historyTest.getTestInfo().getAnswersPart1(),
                            historyTest.getTestInfo().getAnswersPart2(),
                            historyTest.getTestInfo().getAnswersPart3(),
                            historyTest.getTestInfo().getAnswersPart4(),
                            historyTest.getTestInfo().getAnswersPart5(),
                            historyTest.getTestInfo().getAnswersPart6(),
                            historyTest.getTestInfo().getAnswersPart7()));
                    output.setDateTest(historyTest.getDateTest());
                    output.setTimeTest(DateUtils.convertToDateFormat(historyTest.getTime()));
                    output.setScore(historyTest.getScore());
                    output.setScoreListening(historyTest.getScoreListening());
                    output.setScoreReading(historyTest.getScoreReading());

                    return testInfoRepository.findById(historyTest.getTestInfo().getIdTest())
                            .flatMap(testInfo -> {
                                output.setNameTest(testInfo.getTestName());
                                return answerRepository.findById(testInfo.getIdTest())
                                        .map(answer -> {
                                            output.setCorrectAnswer(new UserAnswer(answer.getAnswersPart1(),
                                                    answer.getAnswersPart2(),
                                                    answer.getAnswersPart3(),
                                                    answer.getAnswersPart4(),
                                                    answer.getAnswersPart5(),
                                                    answer.getAnswersPart6(),
                                                    answer.getAnswersPart7()));
                                            return output;
                                        });
                            });
                }).switchIfEmpty(Mono.empty());
    }

    public Flux<OutputParamGetAllHistoryTest> getAllHistoryTest() {
        return historyTestRepository.findAll()
                .flatMap(historyTest -> {
                    OutputParamGetAllHistoryTest output = new OutputParamGetAllHistoryTest();
                    output.setIdTestHistory(historyTest.getIdTestHistory());
                    output.setEmail(historyTest.getEmailUser());
                    output.setScore(historyTest.getScore());
                    output.setDateTest(historyTest.getDateTest());

                    return testInfoRepository.findById(historyTest.getTestInfo().getIdTest())
                            .map(testInfo -> {
                                output.setTestName(testInfo.getTestName());
                                return output;
                            });
                });
    }
}
