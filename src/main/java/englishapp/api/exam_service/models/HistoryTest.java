package englishapp.api.exam_service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "HistoryTest")
@Data
public class HistoryTest {
    @Id
    private String idTestHistory;
    private String idUser;
    private String emailUser;
    private Answer TestInfo;
    private String score;
    private String scoreListening;
    private String scoreReading;
    private String time;
    private String dateTest;
}
