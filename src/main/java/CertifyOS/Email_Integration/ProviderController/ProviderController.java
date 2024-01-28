package CertifyOS.Email_Integration.ProviderController;

import CertifyOS.Email_Integration.Model.Provider;
import CertifyOS.Email_Integration.ProviderRequestDTO;
import CertifyOS.Email_Integration.ProviderService.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/provider")

public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody List<ProviderRequestDTO> providerRequestDTOList) {
        try {
            if (providerRequestDTOList == null || providerRequestDTOList.isEmpty()) {
                return ResponseEntity.badRequest().body("Provider request list is empty or null.");
            }
            String result = providerService.sendEmail(providerRequestDTOList);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending emails: " + e.getMessage());
        }
    }
}
