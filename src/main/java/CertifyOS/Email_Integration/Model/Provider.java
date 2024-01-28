package CertifyOS.Email_Integration.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "provider")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Provider {

    @Id
    private Integer providerId;

    private String name;

    private String type;

    private String email;

    private String customerName;

    private Integer noOfOutreach = 0;
}
