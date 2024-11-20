package CertifyOS.Email_Integration.RequestDTO;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component

public class FacilityRequestDTO {
    private Integer facilityId;
    private String name;
    private String contactPerson;
    private String email;
    private String customerName;
    private String assigneeEmail;
    private String reason;
    private String body;
    private String DueDate;
}
