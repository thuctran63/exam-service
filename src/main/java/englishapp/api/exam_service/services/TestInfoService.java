package englishapp.api.exam_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import englishapp.api.exam_service.dto.apiGetTestInfo.OutputParamApiGetTestInfo;
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

}
