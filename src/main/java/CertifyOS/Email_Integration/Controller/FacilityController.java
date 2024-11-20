package CertifyOS.Email_Integration.Controller;


import CertifyOS.Email_Integration.Service.FacilityService;
import CertifyOS.Email_Integration.RequestDTO.FacilityRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/facility")
public class FacilityController {


    @Autowired
    private FacilityService facilityService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody List<FacilityRequestDTO> facilityRequestDTOList) {
        try {
            if (facilityRequestDTOList == null || facilityRequestDTOList.isEmpty()) {
                return ResponseEntity.badRequest().body("Facility request list is empty or null.");
            }
            String result = facilityService.sendEmail(facilityRequestDTOList);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending emails: " + e.getMessage());
        }
    }
}
