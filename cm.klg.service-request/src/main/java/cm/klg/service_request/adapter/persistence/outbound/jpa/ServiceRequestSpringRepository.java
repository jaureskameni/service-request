package cm.klg.service_request.adapter.persistence.outbound.jpa;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRequestSpringRepository extends JpaRepository<ServiceRequestJpa, UUID> {}
