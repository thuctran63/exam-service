package englishapp.api.exam_service.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import api.common.englishapp.requests.CommonResponse;
import api.common.englishapp.requests.ResponseUtil;
import englishapp.api.exam_service.dto.apiTestAnswer.InputParamApiTestAnswer;
import englishapp.api.exam_service.services.TestInfoService;
import englishapp.api.exam_service.services.TestService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/exam")
public class ExamController {
    @Autowired
    private TestInfoService testInfoService;
    @Autowired
    private TestService testService;

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

    @PostMapping("/TestAnswer")
    public Mono<ResponseEntity<CommonResponse<?>>> testAnswer(@RequestBody InputParamApiTestAnswer input) {
        return testService.testAnswer(
                input)
                .map(output -> ResponseUtil.ok(output))
                .onErrorResume(e -> {
                    logger.error("Error occurred while submitting test answer: {}", e.getMessage(), e);
                    return Mono.just(ResponseUtil.serverError(e.getMessage()));
                });
    }

}
