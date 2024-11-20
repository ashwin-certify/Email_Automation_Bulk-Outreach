package CertifyOS.Email_Integration.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "facility")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Facility {
    @Id
    private Integer facilityId;

    private String name;

    private String contactPerson;

    private String assigneeEmail;

    private String DueDate;

    private String body;

    private String reason;

    private String email;

    private String customerName;

    private Integer noOfOutreach = 0;

}
