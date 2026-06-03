package cm.klg.service_request.adapter.persistence.outbound.jpa;

import cm.klg.service_request.application.outbound.ServiceRequestRepository;
import cm.klg.service_request.domain.service_request.ServiceRequest;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;

@RequiredArgsConstructor
public class ServiceRequestJpaImpl implements ServiceRequestRepository {
  private final ServiceRequestSpringRepository springRepository;
  private final JpaMapper jpaMapper;

  @Override
  public void insert(@NonNull ServiceRequest serviceRequest) {
    springRepository.save(jpaMapper.toJpa(serviceRequest));
  }
}
