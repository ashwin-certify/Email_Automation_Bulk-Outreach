package CertifyOS.Email_Integration.ProviderRepository;

import CertifyOS.Email_Integration.Model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepository extends JpaRepository<Provider, Integer> {
}
