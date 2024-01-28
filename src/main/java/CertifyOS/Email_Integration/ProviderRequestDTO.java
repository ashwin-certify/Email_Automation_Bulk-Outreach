package CertifyOS.Email_Integration;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component

public class ProviderRequestDTO {
    private Integer providerId;

    private String name;

    private String type;

    private String email;

    private String customerName;
}
