package cm.klg.service_request.adapter.persistence.outbound.jpa;

import static cm.klg.service_request.application.views.ServiceRequestViews.*;

import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServiceRequestSpringRepository
    extends JpaRepository<@NonNull ServiceRequestJpa, @NonNull UUID> {

  @Query(
      "select request from ServiceRequestJpa request "
          + "left join fetch request.followUp where request.id = :id")
  Optional<@NonNull ServiceRequestJpa> findByIdWithFollowUp(@Param("id") UUID id);

  @Query(
      value =
          """
          SELECT
              s.id                        AS id,
              s.clientId                  AS userId,
              s.providerId                AS serviceProviderId,
              s.serviceTypeId             AS serviceTypeId,
              s.title                     AS title,
              s.description               AS description,
              s.location                  AS location,
              s.status                    AS status,
              s.reason                    AS reason,
              s.createdAt                 AS createdAt,
              s.updatedAt                 AS updatedAt
          FROM ServiceRequestJpa s
          WHERE s.clientId = :userId AND s.status = :status
          ORDER BY s.createdAt DESC, s.id DESC\
          """,
      countQuery =
          "SELECT COUNT(DISTINCT s) FROM ServiceRequestJpa s WHERE"
              + " s.clientId = :userId AND s.status = :status")
  Page<@NonNull ServiceRequestView1> findAllMyRequestByUserIdAndStatusAsView1(
      @Param("userId") UUID userId, @Param("status") String status, Pageable pageable);

  @Query(
      value =
          """
          SELECT
              s.id                        AS id,
              s.clientId                  AS userId,
              s.providerId                AS serviceProviderId,
              s.serviceTypeId             AS serviceTypeId,
              s.title                     AS title,
              s.description               AS description,
              s.location                  AS location,
              s.status                    AS status,
              s.reason                    AS reason,
              s.createdAt                 AS createdAt,
              s.updatedAt                 AS updatedAt
          FROM ServiceRequestJpa s
          WHERE s.clientId = :userId
          ORDER BY s.createdAt DESC, s.id DESC\
          """,
      countQuery =
          "SELECT COUNT(DISTINCT s) FROM ServiceRequestJpa s WHERE" + " s.clientId = :userId")
  Page<@NonNull ServiceRequestView1> findAllMyRequestByUserIdAsView1(
      @Param("userId") UUID userId, Pageable pageable);

  @Query(
      value =
          """
          SELECT
              s.id                        AS id,
              s.clientId                  AS userId,
              s.providerId                AS serviceProviderId,
              s.serviceTypeId             AS serviceTypeId,
              s.title                     AS title,
              s.description               AS description,
              s.location                  AS location,
              s.status                    AS status,
              s.reason                    AS reason,
              s.createdAt                 AS createdAt,
              s.updatedAt                 AS updatedAt
          FROM ServiceRequestJpa s
          WHERE s.providerId = :providerId AND s.status = :status
          ORDER BY s.createdAt DESC, s.id DESC\
          """,
      countQuery =
          "SELECT COUNT(DISTINCT s) FROM ServiceRequestJpa s WHERE"
              + " s.providerId = :providerId AND s.status = :status")
  Page<@NonNull ServiceRequestView1> findAllRequestByProviderIdAndStatusAsView1(
      @Param("providerId") UUID providerId, @Param("status") String status, Pageable pageable);

  @Query(
      value =
          """
          SELECT
              s.id                        AS id,
              s.clientId                  AS userId,
              s.providerId                AS serviceProviderId,
              s.serviceTypeId             AS serviceTypeId,
              s.title                     AS title,
              s.description               AS description,
              s.location                  AS location,
              s.status                    AS status,
              s.reason                    AS reason,
              s.createdAt                 AS createdAt,
              s.updatedAt                 AS updatedAt
          FROM ServiceRequestJpa s
          WHERE s.providerId = :providerId
          ORDER BY s.createdAt DESC, s.id DESC\
          """,
      countQuery =
          "SELECT COUNT(DISTINCT s) FROM ServiceRequestJpa s WHERE" + " s.providerId = :providerId")
  Page<@NonNull ServiceRequestView1> findAllRequestByProviderIdAsView1(
      @Param("providerId") UUID providerId, Pageable pageable);
}
