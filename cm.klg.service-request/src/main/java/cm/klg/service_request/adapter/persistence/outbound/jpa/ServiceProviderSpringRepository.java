package cm.klg.service_request.adapter.persistence.outbound.jpa;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ServiceProviderSpringRepository extends JpaRepository<ServiceProviderJpa, UUID> {
  Optional<ServiceProviderJpa> findByUserId(UUID userId);

  default Optional<ServiceProviderJpa> findAggregateById(UUID id) {
    return findById(id);
  }

  boolean existsByIdAndStatus(UUID id, String status);

  boolean existsByUserIdAndStatus(UUID userId, String status);

  @Transactional
  @Modifying
  @Query(
      value =
          "INSERT INTO t_service_provider (c_id, c_user_id, c_status, c_approved_at) "
              + "VALUES (:id, :userId, :status, :approvedAt) "
              + "ON CONFLICT (c_id) DO NOTHING",
      nativeQuery = true)
  int insertIfAbsent(
      @Param("id") UUID id,
      @Param("userId") UUID userId,
      @Param("status") String status,
      @Param("approvedAt") java.time.LocalDateTime approvedAt);
}
