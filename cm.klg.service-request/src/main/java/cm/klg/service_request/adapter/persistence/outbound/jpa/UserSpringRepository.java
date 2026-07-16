package cm.klg.service_request.adapter.persistence.outbound.jpa;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSpringRepository extends JpaRepository<UserJpa, UUID> {
  boolean existsByIdentityId(UUID identityId);
}
