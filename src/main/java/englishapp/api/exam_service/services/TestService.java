package englishapp.api.exam_service.services;

import java.util.ArrayList;
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
import englishapp.api.exam_service.repositories.AnswerRepository;
import englishapp.api.exam_service.repositories.TestRepository;
import reactor.core.publisher.Mono;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private AnswerRepository answerRepository;

    public Mono<OutputParamApiGetTest> getTest(String idTest) {
        return testRepository.findById(idTest)
                .map(OutputParamApiGetTest::new);
    }

    public Mono<OutputParamApiTestAnswer> testAnswer(InputParamApiTestAnswer input) {
        return answerRepository.findById(
                input.getIdTest())
                .flatMap(answer -> {
                    // Process the answer and calculate the score
                    OutputParamApiTestAnswer output = new OutputParamApiTestAnswer();
                    calculateScore(answer, input, output);
                    output.setIdTest(answer.getIdTest());
                    return Mono.just(output);
                });
    }

    private void calculateScore(Answer answer, InputParamApiTestAnswer input, OutputParamApiTestAnswer output) {
        int score = 0;

        score += calculatePartScore(input.getAnswersPart1(), answer.getAnswersPart1());
        score += calculatePartScore(input.getAnswersPart2(), answer.getAnswersPart2());
        score += calculatePartScore(input.getAnswersPart3(), answer.getAnswersPart3());
        score += calculatePartScore(input.getAnswersPart4(), answer.getAnswersPart4());
        score += calculatePartScore(input.getAnswersPart5(), answer.getAnswersPart5());
        score += calculatePartScore(input.getAnswersPart6(), answer.getAnswersPart6());
        score += calculatePartScore(input.getAnswersPart7(), answer.getAnswersPart7());

        output.setAnswersPart1(answer.getAnswersPart1() != null ? answer.getAnswersPart1() : new ArrayList<>());
        output.setAnswersPart2(answer.getAnswersPart2() != null ? answer.getAnswersPart2() : new ArrayList<>());
        output.setAnswersPart3(answer.getAnswersPart3() != null ? answer.getAnswersPart3() : new ArrayList<>());
        output.setAnswersPart4(answer.getAnswersPart4() != null ? answer.getAnswersPart4() : new ArrayList<>());
        output.setAnswersPart5(answer.getAnswersPart5() != null ? answer.getAnswersPart5() : new ArrayList<>());
        output.setAnswersPart6(answer.getAnswersPart6() != null ? answer.getAnswersPart6() : new ArrayList<>());
        output.setAnswersPart7(answer.getAnswersPart7() != null ? answer.getAnswersPart7() : new ArrayList<>());

        output.setScore(String.valueOf(score));
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
