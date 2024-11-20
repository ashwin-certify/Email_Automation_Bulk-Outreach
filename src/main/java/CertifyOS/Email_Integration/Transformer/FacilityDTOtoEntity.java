package CertifyOS.Email_Integration.Transformer;

import CertifyOS.Email_Integration.Model.Facility;
import CertifyOS.Email_Integration.RequestDTO.FacilityRequestDTO;

public class FacilityDTOtoEntity {

    public static Facility convert(FacilityRequestDTO facilityRequestDTO) {

        return Facility.builder()
                .facilityId(facilityRequestDTO.getFacilityId())
                .name(facilityRequestDTO.getName())
                .contactPerson(facilityRequestDTO.getContactPerson())
                .assigneeEmail(facilityRequestDTO.getAssigneeEmail())
                .body(facilityRequestDTO.getBody())
                .reason(facilityRequestDTO.getReason())
                .email(facilityRequestDTO.getEmail())
                .customerName(facilityRequestDTO.getCustomerName())
                .DueDate(facilityRequestDTO.getDueDate())
                .build();
    }
}
