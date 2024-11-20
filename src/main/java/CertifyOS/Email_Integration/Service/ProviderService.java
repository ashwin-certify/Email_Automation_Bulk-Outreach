package CertifyOS.Email_Integration.Service;

import CertifyOS.Email_Integration.Model.Provider;
import CertifyOS.Email_Integration.Repository.ProviderRepository;
import CertifyOS.Email_Integration.RequestDTO.ProviderRequestDTO;
import CertifyOS.Email_Integration.Transformer.DTOToEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderService {
    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    public String sendEmail(List<ProviderRequestDTO> providers) {
        StringBuilder resultMessage = new StringBuilder();

        for (ProviderRequestDTO providerDTO : providers) {
            Optional<Provider> optionalProvider = providerRepository.findById(providerDTO.getProviderId());

            if (optionalProvider.isPresent()) {
                Provider existingProvider = optionalProvider.get();
                Integer noOfOutreaches = existingProvider.getNoOfOutreach() != null ? existingProvider.getNoOfOutreach() : 0;

                if (noOfOutreaches < 30) {
                    // Send email only if the number of outreaches is less than 4
                    sendEmailForProvider(existingProvider, providerDTO.getAssigneeEmail(), providerDTO.getReason(), providerDTO.getBody());
                    resultMessage.append("Emails have been sent successfully for provider with ID: ").append(providerDTO.getProviderId()).append("\n");
                } else {
                    resultMessage.append("Email for provider: ")
                            .append(existingProvider.getName())
                            .append(" :")
                            .append(existingProvider.getProviderId())
                            .append(" with max no. Of Outreach: ")
                            .append(noOfOutreaches)
                            .append(" was already sent with max attempt\n");
                }
            } else {
                Provider newProvider = DTOToEntity.convert(providerDTO);
                newProvider.setNoOfOutreach(1);  // Set outreach count to 1 for a new provider
                sendEmailForProvider(newProvider, providerDTO.getAssigneeEmail(), providerDTO.getReason(), providerDTO.getBody());
                resultMessage.append("Emails have been sent successfully for new provider with ID: ").append(providerDTO.getProviderId()).append("\n");
            }
        }
        return resultMessage.toString();
    }
    private void sendEmailForProvider(Provider provider, String assigneeEmail, String reason, String emailBody) {
        String providerName = provider.getName();
        Integer providerNpi = provider.getProviderId();

    //single reason
//        String subject = "Re: Centene Re-credentialing: Outreach Reason: "+ reason+ " :" + provider.getName() + " " + provider.getType() + " " + providerNpi + "_Certify Outreach";
//        String body = "<p>Dear " + providerName + ",</p>" +
//                "<p><b style=\"color:blue;\">" + emailBody + "</b></p>" +
//                "<p>Also, Please log in to CAQH at your earliest convenience and update your attestation. During your attestation review, please ensure the information and documents below are also updated, where applicable.</p>" +
//                "<ul>" +
//                "<li>Active Malpractice Insurance</li>" +
//                "<li>Disclosure questions with explanations (if applicable)</li>" +
//                "<li>Hospital admitting privileges or detailed admitting arrangements</li>" +
//                "<li>Malpractice case details (if applicable)</li>" +
//                "<li>CLIA Certificate or Waiver (if your primary location provides blood-drawn services)</li>" +
//                "<li><a href=\"https://drive.google.com/file/d/15BH9y88J-Mi3k2nxBiQL5rtkpUDTxH6Q/view?usp=drive_link\">Collaborative Agreement (if applicable)</li>" +
//                "<li><a href=\"https://drive.google.com/file/d/15BNgd6RC-2VWE8e1TUOY4IM36ZKwwJTh/view?usp=sharing\">Disclosure of Ownership Control</a> (for CA, FL, GA, IL, ID, KS, LA, MA, MI, MO, MS, NJ, NM, NV, OH, OR, SD, TN, TX and WA)</li>" +
//                "</ul>" +
//                "<p>As soon as this is complete, please <b>reply all</b> or send us an email at credentialing@certifyos.com, and we can complete your credentialing with Centene Health Plan.</p>" +
//                "<p>If you have any questions or are no longer a part of the Centene Health Plan network, please email us at credentialing@certifyos.com.</p>" +
//                "<p>Thank you!</p>" +
//                "<p><i><b style=\"color:green;\">Please disregard this email if you have already responded. Thank you!</b></i></p>";


////         -- Multiple Reasons - Centene
          String formattedEmailBody = emailBody.replaceAll("(\\d+\\.\\s*)", "$1");
          String subject = "Re: Centene Health Re-Credentialing: Outreach Reason :"  + provider.getName() + " " + provider.getType() + " " + providerNpi + " : Certify Outreach";
          String body = "<p>Dear " + providerName + ",</p>" +
                "<p>My name is Ashwin Das and Centene Health has collaborated with us to complete provider credentialing. We hope this letter finds you well. As part of our re-credentialing process, we have conducted a review of your credentialing file and found that some required documentation is missing or incomplete.</p>" +
//                "<p>To finalize your credentialing, Your credentialing due date is 11/30/2024 and we have still not heard back from you. Please submit the information requested below as soon as possible to avoid network termination from Oscar</p>" +
                "<p>To complete your re-credentialing, please provide or update the following documents or attest CAQH at your earliest convenience.</p>" +
                "<p>" + formattedEmailBody + "</p>" +
                "<p>Please submit these documents as soon as possible to avoid delays in your credentialing process. Your prompt attention to this matter will help us ensure that your participation and status within our network remains active and in compliance.</p>" +
//                "<p>As soon as this is complete, please <b>reply all</b> or send us an email at <a href='mailto:credentialing@certifyos.com'>credentialing@certifyos.com</a>, and we can complete your credentialing with Oscar Health Plan.</p>" +
//                "<p>If you have already submitted these documents or if you require any assistance or clarification, please do not hesitate to contact us at <a href='mailto:credentialing@certifyos.com'>credentialing@certifyos.com</a>.</p>" +
//                "<p>We greatly appreciate your cooperation in this process and look forward to continuing our partnership in providing high-quality care to our patients.</p>" +
//                  "<p>Thank you for your prompt attention to this matter.</p>";
                "<li><a href=\"https://drive.google.com/file/d/15BNgd6RC-2VWE8e1TUOY4IM36ZKwwJTh/view?usp=sharing\">Disclosure of Ownership Control</a> (for CA, FL, GA, ID, KS, LA, MI, MO, MS, NJ, NM, NV, OH, SD, TN, TX and WA)</li>" +
                "<li><a href=\"https://drive.google.com/file/d/15BH9y88J-Mi3k2nxBiQL5rtkpUDTxH6Q/view?usp=drive_link\">Collaborative Agreement (if applicable)</li>" +
                "<p>As soon as this is complete, please <b>reply all</b> or send us an email at credentialing@certifyos.com, and we can complete your re-credentialing with Centene Health Network.</p>" +
                "<p>If you have any questions or are no longer a part of the Centene Health Network, please email us at credentialing@certifyos.com.</p>" +
                "<p>Thank you!</p>";

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("ashwind@certifyos.com");
            helper.setTo(provider.getEmail());
            helper.setCc(new String[]{assigneeEmail,"credentialing@certifyos.com","recred-corporate@centene.com"});
//            helper.setCc(new String[]{assigneeEmail,"credentialing@certifyos.com"});
            helper.setSubject(subject);
            helper.setText(body, true);  // Set to true to send HTML content

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        // Increment outreach count for existing provider
        provider.setNoOfOutreach(provider.getNoOfOutreach() + 1);
        providerRepository.save(provider);
    }
}































// 1. Plain Mail -----------------------
//    private void sendEmailForProvider(Provider provider, String assigneeEmail, String reason) {
//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        String providerName = provider.getName();
//        String customerName = provider.getCustomerName();
//        Integer providerNpi = provider.getProviderId();
//
//        simpleMailMessage.setSubject("Re: Centene Re-credentialing: Outreach Reason - " + reason + " :: " + provider.getName() + " " + provider.getType() + " " + providerNpi);
//
//        String body = "Dear " + providerName + ",\n" +
//                "\n" +
//                "Centene uses CertifyOS to complete provider credentialing. We are contacting you because " + reason + ".\n" +
//                "\n" +
//                "Please log in to CAQH at your earliest convenience and update your attestation. During your attestation review, please ensure the information and documents below are also updated, where applicable:\n" +
//                "\n" +
//                "• Active Malpractice Insurance\n" +
//                "• Disclosure questions with explanations (if applicable)\n" +
//                "• Hospital admitting privileges or detailed admitting arrangements\n" +
//                "• Malpractice case details (if applicable)\n" +
//                "• CLIA Certificate or Waiver (if your primary location provides blood-drawn services)\n" +
//                "• Collaborative Agreement (if applicable)\n" +
//                "• Disclosure of Ownership Control (for CA, WA, MO, LA, MS, GA, NV, KS, FL, TN, OR, and NM)\n" +
//                "\n" +
//                "As soon as this is complete, please send us an email at credentialing@certifyos.com, and we can complete your re-credentialing with Centene.\n" +
//                "\n" +
//                "If you have any questions or are no longer a part of the Centene network, please email us at credentialing@certifyos.com.\n" +
//                "\n" +
//                "Thank you!";
//
//        simpleMailMessage.setFrom("ashwind@certifyos.com");
//        simpleMailMessage.setTo(provider.getEmail());
//        simpleMailMessage.setCc(assigneeEmail, "credentialing@certifyos.com");
//        simpleMailMessage.setText(body);
//
//        javaMailSender.send(simpleMailMessage);
//
//        // Increment outreach count for existing provider
//        provider.setNoOfOutreach(provider.getNoOfOutreach() + 1);
//        providerRepository.save(provider);
//    }
//}
