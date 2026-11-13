package cm.klg.service_request.adapter.persistence.outbound.jpa;

import cm.klg.service_request.application.outbound.ServiceRequestRepository;
import cm.klg.service_request.application.views.ServiceRequestViews.ServiceRequestView1;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_request.ServiceRequest;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.domain.service_request.ServiceRequestNotFoundException;
import cm.klg.service_request.domain.service_request.ServiceRequestStatus;
import cm.klg.service_request.domain.user.UserId;
import cm.klg.service_request.utils.PageData;
import cm.klg.service_request.utils.PaginationFetchRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ServiceRequestJpaImpl implements ServiceRequestRepository {
  private final ServiceRequestSpringRepository springRepository;
  private final JpaMapper jpaMapper;

  @Override
  public void insert(@NonNull ServiceRequest serviceRequest) {
    springRepository.save(jpaMapper.toJpa(serviceRequest));
  }

  @Override
  public void update(@NonNull ServiceRequest serviceRequest) {
    ServiceRequestJpa serviceRequestJpa =
        getByIdWithFollowUp(serviceRequest.getId())
            .orElseThrow(ServiceRequestNotFoundException::new);
    jpaMapper.toJpa(serviceRequestJpa, serviceRequest);
    springRepository.save(serviceRequestJpa);
  }

  @Override
  public PageData<@NonNull ServiceRequestView1> loadAllMyRequestsAsView1(
      @NonNull UserId userId, @NonNull PaginationFetchRequest pagination) {
    Pageable pageable = toPageable(pagination);
    var serviceRequestPage =
        springRepository.findAllMyRequestByUserIdAsView1(userId.value(), pageable);
    return new PageData<>(serviceRequestPage.getTotalElements(), serviceRequestPage.getContent());
  }

  @Override
  public PageData<@NonNull ServiceRequestView1> loadAllMyRequestByStatusAsView1(
      @NonNull UserId userId,
      @NonNull ServiceRequestStatus status,
      @NonNull PaginationFetchRequest pagination) {

    Pageable pageable = toPageable(pagination);
    var serviceRequestPage =
        springRepository.findAllMyRequestByUserIdAndStatusAsView1(
            userId.value(), status.name(), pageable);
    return new PageData<>(serviceRequestPage.getTotalElements(), serviceRequestPage.getContent());
  }

  @Override
  public PageData<@NonNull ServiceRequestView1> loadAllRequestsByProviderAsView1(
      @NonNull ServiceProviderId providerId, @NonNull PaginationFetchRequest pagination) {
    Pageable pageable = toPageable(pagination);
    var serviceRequestPage =
        springRepository.findAllRequestByProviderIdAsView1(providerId.value(), pageable);
    return new PageData<>(serviceRequestPage.getTotalElements(), serviceRequestPage.getContent());
  }

  @Override
  public PageData<@NonNull ServiceRequestView1> loadAllRequestByProviderAndStatusAsView1(
      @NonNull ServiceProviderId providerId,
      @NonNull ServiceRequestStatus status,
      @NonNull PaginationFetchRequest pagination) {
    Pageable pageable = toPageable(pagination);
    var serviceRequestPage =
        springRepository.findAllRequestByProviderIdAndStatusAsView1(
            providerId.value(), status.name(), pageable);
    return new PageData<>(serviceRequestPage.getTotalElements(), serviceRequestPage.getContent());
  }

  @Override
  public ServiceRequest load(@NonNull ServiceRequestId id) throws ServiceRequestNotFoundException {
    return this.getByIdWithFollowUp(id)
        .map(jpaMapper::toReviewSummary)
        .orElseThrow(ServiceRequestNotFoundException::new);
  }

  private Optional<@NonNull ServiceRequestJpa> getByIdWithFollowUp(@NonNull ServiceRequestId id) {
    return springRepository.findByIdWithFollowUp(id.value());
  }

  private static Pageable toPageable(@NonNull PaginationFetchRequest pagination) {
    return PageRequest.of(pagination.pageIndex(), pagination.limit());
  }
}
