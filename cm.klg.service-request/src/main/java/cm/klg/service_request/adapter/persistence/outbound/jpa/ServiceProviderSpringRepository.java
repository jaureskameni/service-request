package cm.klg.service_request.adapter.persistence.outbound.jpa;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceProviderSpringRepository extends JpaRepository<ServiceProviderJpa, UUID> {
  Optional<ServiceProviderJpa> findByUserId(UUID userId);
}
