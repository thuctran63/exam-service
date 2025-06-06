package englishapp.api.exam_service.controllers;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import api.common.englishapp.auth.RequiresAuth;
import api.common.englishapp.auth.UserData;
import api.common.englishapp.requests.CommonResponse;
import api.common.englishapp.requests.ResponseUtil;
import englishapp.api.exam_service.dto.apiTestAnswer.InputParamApiTestAnswer;
import englishapp.api.exam_service.services.HistoryTestService;
import englishapp.api.exam_service.services.TestInfoService;
import englishapp.api.exam_service.services.TestService;
import reactor.core.publisher.Mono;

@RestController
@SuppressWarnings("null")
public class ExamController {
    @Autowired
    private TestInfoService testInfoService;
    @Autowired
    private TestService testService;
    @Autowired
    private HistoryTestService historyTestService;

    private static final Logger logger = LogManager.getLogger(ExamController.class);

    @GetMapping("/getTestInfo")
    public Mono<ResponseEntity<CommonResponse<?>>> getTestInfo(@RequestParam String year) {
        return testInfoService.getTestInfo(year)
                .map(output -> {
                    if (CollectionUtils.isEmpty(output.getTestInfoList())) {
                        return ResponseUtil.noContent();
                    }
                    return ResponseUtil.ok(output.getTestInfoList());
                })
                .onErrorResume(e -> {
                    logger.error("Error occurred while getting test info: {}", e.getMessage(), e);
                    return Mono.just(ResponseUtil.serverError(e.getMessage()));
                });
    }

    @RequiresAuth(roles = { "ADMIN", "USER" })
    @GetMapping("/getTest")
    public Mono<ResponseEntity<CommonResponse<?>>> getTest(@RequestParam String idTest) {
        return testService.getTest(idTest)
                .map(output -> {
                    logger.info("Test found");
                    return ResponseUtil.ok(output);
                })
                .switchIfEmpty(Mono.fromCallable(() -> {
                    logger.warn("Test not found for id: {}", idTest);
                    return ResponseUtil.noContent();
                }))
                .onErrorResume(e -> {
                    logger.error("Error occurred while getting test: {}", e.getMessage(), e);
                    return Mono.just(ResponseUtil.serverError(e.getMessage()));
                });
    }

    @RequiresAuth(roles = { "ADMIN", "USER" })
    @PostMapping("/TestAnswer")
    public Mono<ResponseEntity<CommonResponse<?>>> testAnswer(ServerWebExchange exchange,
            @RequestBody InputParamApiTestAnswer input) {

        UserData userData = exchange.getAttribute("USER_DATA");

        if (Objects.isNull(userData)) {
            return Mono.just(ResponseUtil.unAuthorized("userId is null"));
        }
        return testService.testAnswer(
                input, userData.getUserId(), userData.getEmail())
                .map(output -> ResponseUtil.ok(output))
                .onErrorResume(e -> {
                    logger.error("Error occurred while submitting test answer: {}", e.getMessage(), e);
                    return Mono.just(ResponseUtil.serverError(e.getMessage()));
                });
    }

    @GetMapping("/getYears")
    public Mono<ResponseEntity<CommonResponse<?>>> getYears() {
        return testInfoService.getYear()
                .map(output -> {
                    if (CollectionUtils.isEmpty(output.getYears())) {
                        return ResponseUtil.noContent();
                    }
                    return ResponseUtil.ok(output.getYears());
                })
                .onErrorResume(e -> {
                    logger.error("Error occurred while getting years: {}", e.getMessage(), e);
                    return Mono.just(ResponseUtil.serverError(e.getMessage()));
                });
    }

    @GetMapping("/getHistoryTest")
    @RequiresAuth(roles = { "ADMIN", "USER" })
    public Mono<ResponseEntity<CommonResponse<?>>> getAllHistoryTest(ServerWebExchange exchange) {
        UserData userData = exchange.getAttribute("USER_DATA");

        if (Objects.isNull(userData)) {
            return Mono.just(ResponseUtil.unAuthorized("userId is null"));
        }
        return historyTestService.getAllHistoryTest(userData.getUserId())
                .map(output -> {
                    if (CollectionUtils.isEmpty(output.getHistoryTestList())) {
                        return ResponseUtil.noContent();
                    }
                    return ResponseUtil.ok(output);
                })
                .onErrorResume(e -> {
                    logger.error("Error occurred while getting all history test: {}",
                            e.getMessage(), e);
                    return Mono.just(ResponseUtil.serverError(e.getMessage()));
                });
    }

    @GetMapping("/getTestHistory")
    @RequiresAuth(roles = { "ADMIN", "USER" })
    public Mono<ResponseEntity<CommonResponse<?>>> getTestHistory(@RequestParam String idTestHistory) {
        return historyTestService.getTestHistory(idTestHistory)
                .map(output -> {
                    return ResponseUtil.ok(output);
                }).switchIfEmpty(Mono.fromCallable(() -> {
                    logger.warn("Test history not found for id: {}", idTestHistory);
                    return ResponseUtil.noContent();
                }))
                .onErrorResume(e -> {
                    logger.error("Error occurred while getting test history: {}", e.getMessage(), e);
                    return Mono.just(ResponseUtil.serverError(e.getMessage()));
                });
    }

    @GetMapping("/getNumberOfTest")
    @RequiresAuth(roles = { "ADMIN" })
    public Mono<ResponseEntity<CommonResponse<?>>> getNumberOfTest() {
        return testInfoService.getNumberOfTest()
                .map(output -> {
                    if (Objects.isNull(output.getNumberOfTest())) {
                        return ResponseUtil.noContent();
                    }
                    return ResponseUtil.ok(output.getNumberOfTest());
                })
                .onErrorResume(e -> {
                    logger.error("Error occurred while getting number of test: {}", e.getMessage(), e);
                    return Mono.just(ResponseUtil.serverError(e.getMessage()));
                });
    }

    @GetMapping("/getAllHistoryTest")
    @RequiresAuth(roles = { "ADMIN" })
    public Mono<ResponseEntity<CommonResponse<?>>> getAllHistoryTest() {
        return historyTestService.getAllHistoryTest()
                .collectList() // chuyển từ Flux -> Mono<List>
                .map(outputList -> {
                    if (CollectionUtils.isEmpty(outputList)) {
                        return ResponseUtil.noContent();
                    }
                    return ResponseUtil.ok(outputList);
                })
                .onErrorResume(e -> {
                    logger.error("Error occurred while getting all history test: {}", e.getMessage(), e);
                    return Mono.just(ResponseUtil.serverError(e.getMessage()));
                });
    }

    @GetMapping("/getAllTest")
    @RequiresAuth(roles = { "ADMIN" })
    public Mono<ResponseEntity<CommonResponse<?>>> getAllTest() {
        return testService.getAllTest()
                .collectList() // chuyển từ Flux -> Mono<List>
                .map(outputList -> {
                    if (CollectionUtils.isEmpty(outputList)) {
                        return ResponseUtil.noContent();
                    }
                    return ResponseUtil.ok(outputList);
                })
                .onErrorResume(e -> {
                    logger.error("Error occurred while getting all test: {}", e.getMessage(), e);
                    return Mono.just(ResponseUtil.serverError(e.getMessage()));
                });
    }

}
