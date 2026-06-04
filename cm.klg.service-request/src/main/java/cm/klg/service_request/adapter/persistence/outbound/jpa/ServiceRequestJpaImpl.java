package cm.klg.service_request.adapter.persistence.outbound.jpa;

import cm.klg.service_request.application.outbound.ServiceRequestRepository;
import cm.klg.service_request.application.views.ServiceRequestViews;
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
    getById(serviceRequest.getId())
        .ifPresent(
            serviceRequestJpa -> {
              jpaMapper.toJpa(serviceRequestJpa, serviceRequest);
              springRepository.save(serviceRequestJpa);
            });
  }

  @Override
  public PageData<ServiceRequestViews.ServiceRequestView1> loadAllMyRequestsAsView1(
      @NonNull UserId userId, PaginationFetchRequest pagination) {
    Pageable pageable = PageRequest.of(pagination.pageIndex(), pagination.limit());
    var serviceRequestPage =
        springRepository.findAllMyRequestByUserIdAsView1(userId.value(), pageable);
    return new PageData<>(serviceRequestPage.getTotalElements(), serviceRequestPage.getContent());
  }

  @Override
  public PageData<ServiceRequestViews.ServiceRequestView1> loadAllMyRequestByStatusAsView1(
      @NonNull UserId userId,
      @NonNull ServiceRequestStatus status,
      PaginationFetchRequest pagination) {

    Pageable pageable = PageRequest.of(pagination.pageIndex(), pagination.limit());
    var serviceRequestPage =
        springRepository.findAllMyRequestByUserIdAndStatusAsView1(
            userId.value(), status.name(), pageable);
    return new PageData<>(serviceRequestPage.getTotalElements(), serviceRequestPage.getContent());
  }

  @Override
  public PageData<ServiceRequestViews.ServiceRequestView1> loadAllRequestsByProviderAsView1(
      @NonNull ServiceProviderId providerId, PaginationFetchRequest pagination) {
    Pageable pageable = PageRequest.of(pagination.pageIndex(), pagination.limit());
    var serviceRequestPage =
        springRepository.findAllRequestByProviderIdAsView1(providerId.value(), pageable);
    return new PageData<>(serviceRequestPage.getTotalElements(), serviceRequestPage.getContent());
  }

  @Override
  public PageData<ServiceRequestViews.ServiceRequestView1> loadAllRequestByProviderAndStatusAsView1(
      @NonNull ServiceProviderId providerId,
      @NonNull ServiceRequestStatus status,
      PaginationFetchRequest pagination) {
    Pageable pageable = PageRequest.of(pagination.pageIndex(), pagination.limit());
    var serviceRequestPage =
        springRepository.findAllRequestByProviderIdAndStatusAsView1(
            providerId.value(), status.name(), pageable);
    return new PageData<>(serviceRequestPage.getTotalElements(), serviceRequestPage.getContent());
  }

  @Override
  public ServiceRequestViews.ServiceRequestView1 loadByIdAsView1(@NonNull ServiceRequestId id)
      throws ServiceRequestNotFoundException {
    return springRepository
        .findByIdAsView1(id.value())
        .orElseThrow(ServiceRequestNotFoundException::new);
  }

  @Override
  public ServiceRequest load(@NonNull ServiceRequestId id) throws ServiceRequestNotFoundException {
    return this.getById(id)
        .map(jpaMapper::toDomain)
        .orElseThrow(ServiceRequestNotFoundException::new);
  }

  private Optional<ServiceRequestJpa> getById(ServiceRequestId id) {
    return springRepository.findById(id.value());
  }
}
