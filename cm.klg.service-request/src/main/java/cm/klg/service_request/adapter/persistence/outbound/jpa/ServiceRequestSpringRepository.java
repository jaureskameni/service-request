package cm.klg.service_request.adapter.persistence.outbound.jpa;

import cm.klg.service_request.application.views.ServiceRequestViews;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServiceRequestSpringRepository extends JpaRepository<ServiceRequestJpa, UUID> {

  @Query(
      value =
          """
          SELECT
              s.id                        AS id,
              s.userId                    AS userId,
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
          WHERE s.userId = :userId AND s.status = :status
          ORDER BY s.createdAt DESC\
          """,
      countQuery =
          "SELECT COUNT(DISTINCT s) FROM ServiceRequestJpa s WHERE"
              + " s.userId = :userId AND s.status = :status")
  Page<ServiceRequestViews.ServiceRequestView1> findAllMyRequestByUserIdAndStatusAsView1(
      @Param("userId") UUID userId, @Param("status") String status, Pageable pageable);

  @Query(
      value =
          """
          SELECT
              s.id                        AS id,
              s.userId                    AS userId,
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
          WHERE s.userId = :userId
          ORDER BY s.createdAt DESC\
          """,
      countQuery =
          "SELECT COUNT(DISTINCT s) FROM ServiceRequestJpa s WHERE" + " s.userId = :userId")
  Page<ServiceRequestViews.ServiceRequestView1> findAllMyRequestByUserIdAsView1(
      @Param("userId") UUID userId, Pageable pageable);

  @Query(
      value =
          """
          SELECT
              s.id                        AS id,
              s.userId                    AS userId,
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
          ORDER BY s.createdAt DESC\
          """,
      countQuery =
          "SELECT COUNT(DISTINCT s) FROM ServiceRequestJpa s WHERE"
              + " s.providerId = :providerId AND s.status = :status")
  Page<ServiceRequestViews.ServiceRequestView1> findAllRequestByProviderIdAndStatusAsView1(
      @Param("providerId") UUID providerId, @Param("status") String status, Pageable pageable);

  @Query(
      value =
          """
          SELECT
              s.id                        AS id,
              s.userId                    AS userId,
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
          ORDER BY s.createdAt DESC\
          """,
      countQuery =
          "SELECT COUNT(DISTINCT s) FROM ServiceRequestJpa s WHERE" + " s.providerId = :providerId")
  Page<ServiceRequestViews.ServiceRequestView1> findAllRequestByProviderIdAsView1(
      @Param("providerId") UUID providerId, Pageable pageable);

  @Query(
      value =
          """
          SELECT
              s.id                        AS id,
              s.userId                    AS userId,
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
          WHERE s.id = :id\
          """)
  Optional<ServiceRequestViews.ServiceRequestView1> findByIdAsView1(@Param("id") UUID id);
}
