package englishapp.api.exam_service.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import englishapp.api.exam_service.dto.apiGetAllTest.OutputParamApiGetAllTest;
import englishapp.api.exam_service.dto.apiGetTest.OutputParamApiGetTest;
import englishapp.api.exam_service.dto.apiTestAnswer.InputParamApiTestAnswer;
import englishapp.api.exam_service.dto.apiTestAnswer.OutputParamApiTestAnswer;
import englishapp.api.exam_service.models.Answer;
import englishapp.api.exam_service.models.AnswerPerQuestion;
import englishapp.api.exam_service.models.HistoryTest;
import englishapp.api.exam_service.repositories.AnswerRepository;
import englishapp.api.exam_service.repositories.TestInfoRepository;
import englishapp.api.exam_service.repositories.TestRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private TestInfoRepository testInfoRepository;

    @Autowired
    private HistoryTestService historyTestService;

    public Mono<OutputParamApiGetTest> getTest(String idTest) {
        return testRepository.findById(idTest).flatMap(test -> {
            OutputParamApiGetTest output = new OutputParamApiGetTest();
            output.setIdTest(test.getIdTest());
            output.setQuestionPart1(test.getQuestionPart1());
            output.setQuestionPart2(test.getQuestionPart2());
            output.setQuestionPart3(test.getQuestionPart3());
            output.setQuestionPart4(test.getQuestionPart4());
            output.setQuestionPart5(test.getQuestionPart5());
            output.setQuestionPart6(test.getQuestionPart6());
            output.setQuestionPart7(test.getQuestionPart7());

            return testInfoRepository.findById(test.getIdTest())
                    .map(testInfo -> {
                        output.setNameTest(testInfo.getTestName());
                        return output;
                    });
        });
    }

    public Mono<OutputParamApiTestAnswer> testAnswer(InputParamApiTestAnswer input, String idUser, String email) {
        return answerRepository.findById(input.getIdTest())
                .flatMap(answer -> {
                    OutputParamApiTestAnswer output = new OutputParamApiTestAnswer();
                    calculateScore(answer, input, output);
                    output.setIdTest(answer.getIdTest());

                    HistoryTest historyTest = new HistoryTest();
                    historyTest.setIdUser(idUser);
                    historyTest.setEmailUser(email);
                    historyTest.setTestInfo(input);
                    historyTest.setScore(output.getScore());
                    historyTest.setScoreListening(output.getScoreListening());
                    historyTest.setScoreReading(output.getScoreReading());
                    historyTest.setTime(input.getTimeDoTest());
                    historyTest.setDateTest(input.getDateTest());

                    return testInfoRepository.findById(answer.getIdTest())
                            .flatMap(testInfo -> {
                                output.setNameTest(testInfo.getTestName());
                                return historyTestService.saveHistoryTest(historyTest)
                                        .thenReturn(output);
                            });
                });
    }

    private void calculateScore(Answer answer, InputParamApiTestAnswer input, OutputParamApiTestAnswer output) {
        int listeningScore = 0;
        int readingScore = 0;

        // Listening: Part 1–4
        listeningScore += calculatePartScore(input.getAnswersPart1(), answer.getAnswersPart1());
        listeningScore += calculatePartScore(input.getAnswersPart2(), answer.getAnswersPart2());
        listeningScore += calculatePartScore(input.getAnswersPart3(), answer.getAnswersPart3());
        listeningScore += calculatePartScore(input.getAnswersPart4(), answer.getAnswersPart4());

        // Reading: Part 5–7
        readingScore += calculatePartScore(input.getAnswersPart5(), answer.getAnswersPart5());
        readingScore += calculatePartScore(input.getAnswersPart6(), answer.getAnswersPart6());
        readingScore += calculatePartScore(input.getAnswersPart7(), answer.getAnswersPart7());

        int convertedListening = convertToToeicScore(listeningScore, "listening");
        int convertedReading = convertToToeicScore(readingScore, "reading");

        int totalScore = convertedListening + convertedReading;

        output.setScore(String.valueOf(totalScore));
        output.setAnswersPart1(answer.getAnswersPart1() != null ? answer.getAnswersPart1() : new ArrayList<>());
        output.setAnswersPart2(answer.getAnswersPart2() != null ? answer.getAnswersPart2() : new ArrayList<>());
        output.setAnswersPart3(answer.getAnswersPart3() != null ? answer.getAnswersPart3() : new ArrayList<>());
        output.setAnswersPart4(answer.getAnswersPart4() != null ? answer.getAnswersPart4() : new ArrayList<>());
        output.setAnswersPart5(answer.getAnswersPart5() != null ? answer.getAnswersPart5() : new ArrayList<>());
        output.setAnswersPart6(answer.getAnswersPart6() != null ? answer.getAnswersPart6() : new ArrayList<>());
        output.setAnswersPart7(answer.getAnswersPart7() != null ? answer.getAnswersPart7() : new ArrayList<>());
        output.setScoreListening(String.valueOf(convertedListening));
        output.setScoreReading(String.valueOf(convertedReading));
    }

    private int convertToToeicScore(int raw, String section) {
        if (raw < 0)
            return 0;
        if (raw > 100)
            raw = 100;
        return (int) (raw * 4.95);
    }

    private int calculatePartScore(List<AnswerPerQuestion> userAnswers, List<AnswerPerQuestion> correctAnswers) {
        if (userAnswers == null || correctAnswers == null)
            return 0;

        Map<String, String> correctMap = correctAnswers.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        AnswerPerQuestion::getIdQuestion,
                        AnswerPerQuestion::getKey));

        int partScore = 0;
        for (AnswerPerQuestion userAnswer : userAnswers) {
            if (userAnswer != null) {
                String id = userAnswer.getIdQuestion();
                String userKey = userAnswer.getKey();
                String correctKey = correctMap.get(id);

                if (correctKey != null && correctKey.equals(userKey)) {
                    partScore++;
                }
            }
        }

        return partScore;
    }

    public Flux<OutputParamApiGetAllTest> getAllTest() {
        return testRepository.findAll()
                .flatMap(testEntity -> {
                    OutputParamApiGetAllTest output = new OutputParamApiGetAllTest();

                    int numberOfQuestions = 0;
                    if (testEntity.getQuestionPart1() != null) {
                        numberOfQuestions += testEntity.getQuestionPart1().size();
                    }
                    if (testEntity.getQuestionPart2() != null) {
                        numberOfQuestions += testEntity.getQuestionPart2().size();
                    }
                    if (testEntity.getQuestionPart3() != null) {
                        numberOfQuestions += testEntity.getQuestionPart3().size();
                    }
                    if (testEntity.getQuestionPart4() != null) {
                        numberOfQuestions += testEntity.getQuestionPart4().size();
                    }
                    if (testEntity.getQuestionPart5() != null) {
                        numberOfQuestions += testEntity.getQuestionPart5().size();
                    }
                    if (testEntity.getQuestionPart6() != null) {
                        numberOfQuestions += testEntity.getQuestionPart6().size();
                    }
                    if (testEntity.getQuestionPart7() != null) {
                        numberOfQuestions += testEntity.getQuestionPart7().size();
                    }

                    output.setNumberOfQuestion(String.valueOf(numberOfQuestions));
                    output.setIdTest(testEntity.getIdTest());

                    return testInfoRepository.findById(testEntity.getIdTest())
                            .map(testInfo -> {
                                output.setTestName(testInfo.getTestName());
                                return output;
                            });
                });
    }

}
