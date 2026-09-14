package cm.klg.service_request.adapter.persistence.outbound.jpa;

import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.domain.service_provider.ServiceProvider;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_provider.ServiceProviderNotFoundException;
import cm.klg.service_request.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_request.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.DataIntegrityViolationException;

@Slf4j
@RequiredArgsConstructor
public class ServiceProviderJpaRepository implements ServiceProviderRepository {
  private final ServiceProviderSpringRepository springRepository;
  private final JpaMapper jpaMapper;

  @Override
  public void insertIfAbsent(@NonNull ServiceProvider serviceProvider) {
    ServiceProviderJpa jpa = jpaMapper.toJpa(serviceProvider);
    try {
      int insertedRows =
          springRepository.insertIfAbsent(
              jpa.getId(), jpa.getUserId(), jpa.getStatus(), jpa.getApprovedAt());
      if (insertedRows == 0) {
        log.debug("Service provider {} already exists, skipping insertion.", jpa.getId());
      }
    } catch (DataIntegrityViolationException e) {
      log.warn(
          "Unexpected constraint violation while creating service provider {}", jpa.getId(), e);
    }
  }

  @Override
  public void update(@NonNull ServiceProvider serviceProvider) {
    springRepository
        .findAggregateById(serviceProvider.getId().value())
        .ifPresentOrElse(
            serviceProviderJpa -> {
              jpaMapper.fromServiceProvider(serviceProviderJpa, serviceProvider);
              springRepository.save(serviceProviderJpa);
            },
            () -> {
              throw new ServiceProviderNotFoundException();
            });
  }

  @Override
  public boolean existsById(@NonNull ServiceProviderId serviceProviderId) {
    return springRepository.existsById(serviceProviderId.value());
  }

  @Override
  public ServiceProvider load(@NonNull ServiceProviderId serviceProviderId)
      throws ServiceProviderNotFoundException {
    return springRepository
        .findAggregateById(serviceProviderId.value())
        .map(jpaMapper::toDomain)
        .orElseThrow(ServiceProviderNotFoundException::new);
  }

  @Override
  public boolean isApprovedById(@NonNull ServiceProviderId serviceProviderId) {
    return springRepository.existsByIdAndStatus(
        serviceProviderId.value(), ServiceProviderStatus.APPROVED.name());
  }

  @Override
  public boolean isApprovedByUserId(@NonNull UserId userId) {
    return springRepository.existsByUserIdAndStatus(
        userId.value(), ServiceProviderStatus.APPROVED.name());
  }

  @Override
  public ServiceProvider loadByUserId(@NonNull UserId userId)
      throws ServiceProviderNotFoundException {
    return springRepository
        .findByUserId(userId.value())
        .map(jpaMapper::toDomain)
        .orElseThrow(ServiceProviderNotFoundException::new);
  }
}
