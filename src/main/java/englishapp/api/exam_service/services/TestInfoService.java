package englishapp.api.exam_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import englishapp.api.exam_service.dto.apiGetNumberOfTest.OutputParamApiGetNumberOfTest;
import englishapp.api.exam_service.dto.apiGetTestInfo.OutputParamApiGetTestInfo;
import englishapp.api.exam_service.dto.apiGetYear.OutputParamApiGetYear;
import englishapp.api.exam_service.models.TestInfo;
import englishapp.api.exam_service.repositories.TestInfoRepository;
import reactor.core.publisher.Mono;

@Service
public class TestInfoService {

    @Autowired
    private TestInfoRepository testInfoRepository;

    public Mono<OutputParamApiGetTestInfo> getTestInfo(String year) {
        return testInfoRepository.findByYear(year)
                .collectList()
                .map(OutputParamApiGetTestInfo::new);
    }

    public Mono<OutputParamApiGetYear> getYear() {
        return testInfoRepository.findAllYears()
                .map(TestInfo::getYear)
                .distinct()
                .sort()
                .collectList()
                .map(years -> {
                    OutputParamApiGetYear output = new OutputParamApiGetYear();
                    output.setYears(years);
                    return output;
                });
    }

    public Mono<OutputParamApiGetNumberOfTest> getNumberOfTest() {
        return testInfoRepository.findAll()
                .collectList()
                .map(testInfos -> {
                    OutputParamApiGetNumberOfTest output = new OutputParamApiGetNumberOfTest();
                    output.setNumberOfTest(testInfos.size());
                    return output;
                });
    }
}
