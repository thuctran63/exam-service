package englishapp.api.exam_service.services;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import englishapp.api.exam_service.dto.apiGetTest.OutputParamApiGetTest;
import englishapp.api.exam_service.dto.apiTestAnswer.InputParamApiTestAnswer;
import englishapp.api.exam_service.dto.apiTestAnswer.OutputParamApiTestAnswer;
import englishapp.api.exam_service.models.Answer;
import englishapp.api.exam_service.models.AnswerPerQuestion;
import englishapp.api.exam_service.models.HistoryTest;
import englishapp.api.exam_service.repositories.AnswerRepository;
import englishapp.api.exam_service.repositories.TestRepository;
import reactor.core.publisher.Mono;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private HistoryTestService historyTestService;

    public Mono<OutputParamApiGetTest> getTest(String idTest) {
        return testRepository.findById(idTest)
                .map(OutputParamApiGetTest::new);
    }

    public Mono<OutputParamApiTestAnswer> testAnswer(InputParamApiTestAnswer input, String idUser) {
        return answerRepository.findById(input.getIdTest())
                .flatMap(answer -> {
                    OutputParamApiTestAnswer output = new OutputParamApiTestAnswer();
                    calculateScore(answer, input, output);
                    output.setIdTest(answer.getIdTest());

                    HistoryTest historyTest = new HistoryTest();
                    historyTest.setIdUser(idUser);
                    historyTest.setTestInfo(input);
                    historyTest.setScore(output.getScore());
                    historyTest.setTime(input.getTimeDoTest());
                    historyTest.setDateTest(input.getDateTest());

                    return historyTestService.saveHistoryTest(historyTest)
                            .thenReturn(output); // Chờ save xong mới trả output
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
}
