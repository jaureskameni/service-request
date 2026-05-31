package cm.klg.service_request.adapter.persistence.outbound.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserSpringRepository extends JpaRepository<UserJpa, UUID> {}
