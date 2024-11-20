package CertifyOS.Email_Integration.Service;

import CertifyOS.Email_Integration.Model.Facility;
import CertifyOS.Email_Integration.Repository.FacilityRepository;
import CertifyOS.Email_Integration.RequestDTO.FacilityRequestDTO;
import CertifyOS.Email_Integration.Transformer.FacilityDTOtoEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;



@Service
public class FacilityService {

    @Autowired
    private FacilityRepository facilityRepository;
    @Autowired
    private JavaMailSender javaMailSender;

    public String sendEmail(List<FacilityRequestDTO> facilities) {
        StringBuilder resultMessage = new StringBuilder();

        for (FacilityRequestDTO facilityDTO : facilities) {
            Optional<Facility> optionalFacility = facilityRepository.findById(facilityDTO.getFacilityId());


            if (optionalFacility.isPresent()) {
                Facility existingFacility = optionalFacility.get();
                Integer noOfOutreaches = existingFacility.getNoOfOutreach() != null ? existingFacility.getNoOfOutreach() : 0;

                if (noOfOutreaches < 16) {
                    // Send email only if the number of outreaches is less than 6
                    sendEmailForFacility(existingFacility, facilityDTO.getAssigneeEmail(), facilityDTO.getReason(), facilityDTO.getContactPerson(), facilityDTO.getBody());
                    resultMessage.append("Emails have been sent successfully for facility with ID: ").append(facilityDTO.getFacilityId()).append("\n");
                } else {
                    resultMessage.append("Email for facility: ")
                            .append(existingFacility.getName())
                            .append(" :")
                            .append(existingFacility.getFacilityId())
                            .append(" with max no. Of Outreach: ")
                            .append(noOfOutreaches)
                            .append(" was already sent six times\n");
                }
            } else {
                Facility newFacility = FacilityDTOtoEntity.convert(facilityDTO);
                newFacility.setNoOfOutreach(1);  // Set outreach count to 1 for a new facility
                sendEmailForFacility(newFacility, facilityDTO.getAssigneeEmail(), facilityDTO.getReason(), facilityDTO.getContactPerson(), facilityDTO.getBody());
                resultMessage.append("Emails have been sent successfully for new facility with ID: ").append(facilityDTO.getFacilityId()).append("\n");
            }
        }
        return resultMessage.toString();
    }


    private void sendEmailForFacility(Facility facility, String assigneeEmail, String reason, String contactPerson, String body) {
        String facilityName = facility.getName();
        Integer facilityId = facility.getFacilityId();

        String subject = "Re: Lumeris Health Facility Credentialing:  - " + reason + " :: " + facilityName + " " + facilityId;
//        String body = "<p>Dear " + contactPerson + ",</p>" +
//                "<p style=\"color:blue;\">Greetings from Certify OS! Lumeris Health has partnered with us to finalize your facility credentialing. We are reaching out to your practice today because we need a copy of your current/active state license and certificate of insurance, as the one we received from Lumeris Health is expired and not current. Additionally, we require the following documents, where applicable, to complete the credentialing for your facility.</p>" +
//                "<ul>" +
//                "<li>Current State License or a letter explaining non-regulation in your state</li>" +
//                "<li>Updated & Signed W9</li>" +
//                "<li>Accreditation Certificate (if applicable)</li>" +
//                "<li>Certificate Of Insurance (General & Professional)</li>" +
//                "<li>Malpractice case details (if applicable)</li>" +
//                "<li>CLIA Certificate or Waiver (if your facility provides lab services)</li>" +
//                "<li>Medicare or Medicaid Approval Letter(if enrolled)</li>" +
//                "</ul>" +
//                "<p>As soon as this is complete, please send us an email, and we can complete your credentialing.</p>" +
//                "<p>If you have any questions or are no longer a part of the Lumeris Health network, please <b><i>reply all</i></b> or email us at credentialing@certifyos.com.</p>" +
//                "<p>Thank you!</p>" +
//                "<p><i><b style=\"color:green;\">Please ignore this email if you have already responded. Thank you!</b></i></p>";
//        String isoDateString = formatDate(dateInput);
        String body1 = "<p>Dear " + contactPerson + ",</p>\n" +
                "\n" +
                "<p>We hope this message finds you well.</p>\n" +
                "\n" +
                "<p>We wish to inform you that the email you received from us on <b>08/02/2024</b> was sent in error. Please disregard the previous communication. The correct re-cred due date for your file is " +  body +".</p>\n" +
                "\n" +
                "<p>We apologize for any confusion this may have caused and appreciate your understanding.</p>\n" +
                "\n" +
                "<p>If you have any questions or need further assistance, please feel free to contact us.</p>\n" +
                "\n" +
                "<p>Thank you for your attention to this matter.</p>";
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("ashwind@certifyos.com");
            helper.setTo(facility.getEmail());
            helper.setCc(new String[]{assigneeEmail, "credentialing@certifyos.com", "chicagocred@essencehealthcare.com"});
//            helper.setCc(new String[]{assigneeEmail, "credentialing@certifyos.com"});
            helper.setSubject(subject);
            helper.setText(body1, true);  // Set to true to send HTML content

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        // Increment outreach count for existing facility
        facility.setNoOfOutreach(facility.getNoOfOutreach() + 1);
        facilityRepository.save(facility);
    }
}
