package CertifyOS.Email_Integration.ProviderService;

import CertifyOS.Email_Integration.Model.Provider;
import CertifyOS.Email_Integration.ProviderRepository.ProviderRepository;
import CertifyOS.Email_Integration.ProviderRequestDTO;
import Transformer.DTOToEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

                if (noOfOutreaches < 2) {
                    // Send email only if the number of outreaches is less than 2
                    sendEmailForProvider(existingProvider);
                    resultMessage.append("Emails have been sent successfully for provider with ID: ").append(providerDTO.getProviderId()).append("\n");
                } else {
                    resultMessage.append("Email for provider: ")
                            .append(existingProvider.getName())
                            .append(" :")
                            .append(existingProvider.getProviderId())
                            .append(" with max no. Of Outreach: ")
                            .append(noOfOutreaches)
                            .append(" was already sent twice\n");
                }
            } else {
                Provider newProvider = DTOToEntity.convert(providerDTO);
                newProvider.setNoOfOutreach(1);  // Set outreach count to 1 for a new provider
                sendEmailForProvider(newProvider);
                resultMessage.append("Emails have been sent successfully for new provider with ID: ").append(providerDTO.getProviderId()).append("\n");
            }
        }
        return resultMessage.toString();
    }

    private void sendEmailForProvider(Provider provider) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        String providerName = provider.getName();
        String customerName = provider.getCustomerName();
        Integer providerNpi = provider.getProviderId();

        simpleMailMessage.setSubject("OUTREACH REASON: Expired attestation :: " +
                provider.getName() + " " + provider.getType() + " " + providerNpi);

        String body = "Dear " + providerName + "\n" +
                "\n" +
                "My name is Ashwin Das and " + customerName + " uses my company’s services to complete provider credentialing. I’m reaching out to you today because your CAQH attestation has expired. This expired attestation is currently blocking you from being credentialed with " + customerName + "." + "\n" +
                "\n" +
                "Please log into your CAQH at your earliest convenience and update your attestation directly. As soon as this is complete, we can complete your credentialing with " + customerName + "." + "\n" +
                "\n" +
                "Please let me know if you have any questions. Thank you!\n" +
                "\n" +
                "Regards,\n" +
                "Ashwin Das\n";

        simpleMailMessage.setFrom("ashwind@certifyos.com");
        simpleMailMessage.setTo(provider.getEmail());
        simpleMailMessage.setCc("credentialing@certifyos.com");
        simpleMailMessage.setText(body);

        javaMailSender.send(simpleMailMessage);

        // Increment outreach count for existing provider
        provider.setNoOfOutreach(provider.getNoOfOutreach() + 1);
        providerRepository.save(provider);
    }
}
