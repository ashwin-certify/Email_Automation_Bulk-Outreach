package CertifyOS.Email_Integration.Transformer;

import CertifyOS.Email_Integration.Model.Provider;
import CertifyOS.Email_Integration.RequestDTO.ProviderRequestDTO;


public class DTOToEntity {

    public static Provider convert(ProviderRequestDTO providerRequestDTO) {


        return Provider.builder()
                .providerId(providerRequestDTO.getProviderId())
                .email(providerRequestDTO.getEmail())
                .name(providerRequestDTO.getName())
                .type(providerRequestDTO.getType())
                .customerName(providerRequestDTO.getCustomerName())
                .build();
    }

}
