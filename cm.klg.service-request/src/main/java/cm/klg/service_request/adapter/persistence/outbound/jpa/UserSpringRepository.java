package cm.klg.service_request.adapter.persistence.outbound.jpa;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserSpringRepository extends JpaRepository<UserJpa, UUID> {
  boolean existsByIdentityId(UUID identityId);

  java.util.Optional<UserJpa> findByIdentityId(UUID identityId);

  @Modifying
  @Query(
      value =
          "INSERT INTO t_user (c_id, c_identity_id, c_firstname, c_lastname, c_email_address,"
              + " c_phone_number, c_is_service_provider, c_created_at) VALUES (:id, :identityId,"
              + " :firstname, :lastname, :email, :phoneNumber, :isServiceProvider, :createdAt)"
              + " ON CONFLICT (c_identity_id) DO NOTHING",
      nativeQuery = true)
  int insertIfAbsent(
      @Param("id") UUID id,
      @Param("identityId") UUID identityId,
      @Param("firstname") String firstname,
      @Param("lastname") String lastname,
      @Param("email") String email,
      @Param("phoneNumber") String phoneNumber,
      @Param("isServiceProvider") boolean isServiceProvider,
      @Param("createdAt") LocalDateTime createdAt);
}
