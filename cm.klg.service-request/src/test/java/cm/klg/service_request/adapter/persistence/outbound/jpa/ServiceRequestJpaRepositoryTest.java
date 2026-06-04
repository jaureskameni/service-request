package cm.klg.service_request.adapter.persistence.outbound.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_request.application.views.ServiceRequestViews.ServiceRequestView1;
import cm.klg.service_request.domain.service_request.ServiceRequest;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.domain.service_request.ServiceRequestStatus;
import cm.klg.service_request.domain.user.UserId;
import cm.klg.service_request.utils.PageData;
import cm.klg.service_request.utils.PaginationFetchRequest;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class ServiceRequestJpaRepositoryTest {

  @Mock private ServiceRequestSpringRepository springRepository;
  @Mock private JpaMapper jpaMapper;
  @Mock private ServiceRequest serviceRequest;
  @Mock private ServiceRequestView1 serviceRequestView1;

  @InjectMocks private ServiceRequestJpaImpl repository;

  @Test
  void shouldInsertMappedServiceRequest() {
    ServiceRequestJpa serviceRequestJpa = new ServiceRequestJpa();
    when(jpaMapper.toJpa(serviceRequest)).thenReturn(serviceRequestJpa);

    repository.insert(serviceRequest);

    verify(springRepository).save(serviceRequestJpa);
  }

  @Test
  void shouldLoadAllMyRequestsWithPageable() {
    UserId userId = UserId.from(UUID.randomUUID());
    var pageable = PageRequest.of(1, 15);
    when(springRepository.findAllMyRequestByUserIdAsView1(userId.value(), pageable))
        .thenReturn(new PageImpl<>(List.of(serviceRequestView1), pageable, 16));

    PageData<ServiceRequestView1> result =
        repository.loadAllMyRequestsAsView1(userId, new PaginationFetchRequest(15, 1));

    assertThat(result.total()).isEqualTo(16);
    assertThat(result.elements()).containsExactly(serviceRequestView1);
    verify(springRepository)
        .findAllMyRequestByUserIdAsView1(
            eq(userId.value()),
            argThat(page -> page.getPageNumber() == 1 && page.getPageSize() == 15));
  }

  @Test
  void shouldLoadAllMyRequestsByStatusWithPageable() {
    UserId userId = UserId.from(UUID.randomUUID());
    var pageable = PageRequest.of(0, 10);
    when(springRepository.findAllMyRequestByUserIdAndStatusAsView1(
            userId.value(), ServiceRequestStatus.PENDING.name(), pageable))
        .thenReturn(new PageImpl<>(List.of(serviceRequestView1), pageable, 1));

    PageData<ServiceRequestView1> result =
        repository.loadAllMyRequestByStatusAsView1(
            userId, ServiceRequestStatus.PENDING, new PaginationFetchRequest(10, 0));

    assertThat(result.total()).isEqualTo(1);
    assertThat(result.elements()).containsExactly(serviceRequestView1);
    verify(springRepository)
        .findAllMyRequestByUserIdAndStatusAsView1(
            eq(userId.value()),
            eq(ServiceRequestStatus.PENDING.name()),
            argThat(page -> page.getPageNumber() == 0 && page.getPageSize() == 10));
  }

  @Test
  void shouldLoadAllRequestsByProviderWithPageable() {
    var providerId =
        cm.klg.service_request.domain.service_provider.ServiceProviderId.from(UUID.randomUUID());
    var pageable = PageRequest.of(2, 20);
    when(springRepository.findAllRequestByProviderIdAsView1(providerId.value(), pageable))
        .thenReturn(new PageImpl<>(List.of(serviceRequestView1), pageable, 41));

    PageData<ServiceRequestView1> result =
        repository.loadAllRequestsByProviderAsView1(providerId, new PaginationFetchRequest(20, 2));

    assertThat(result.total()).isEqualTo(41);
    assertThat(result.elements()).containsExactly(serviceRequestView1);
    verify(springRepository)
        .findAllRequestByProviderIdAsView1(
            eq(providerId.value()),
            argThat(page -> page.getPageNumber() == 2 && page.getPageSize() == 20));
  }

  @Test
  void shouldLoadAllRequestsByProviderAndStatusWithPageable() {
    var providerId =
        cm.klg.service_request.domain.service_provider.ServiceProviderId.from(UUID.randomUUID());
    var pageable = PageRequest.of(0, 10);
    when(springRepository.findAllRequestByProviderIdAndStatusAsView1(
            providerId.value(), ServiceRequestStatus.ACCEPTED.name(), pageable))
        .thenReturn(new PageImpl<>(List.of(serviceRequestView1), pageable, 1));

    PageData<ServiceRequestView1> result =
        repository.loadAllRequestByProviderAndStatusAsView1(
            providerId, ServiceRequestStatus.ACCEPTED, new PaginationFetchRequest(10, 0));

    assertThat(result.total()).isEqualTo(1);
    assertThat(result.elements()).containsExactly(serviceRequestView1);
    verify(springRepository)
        .findAllRequestByProviderIdAndStatusAsView1(
            eq(providerId.value()),
            eq(ServiceRequestStatus.ACCEPTED.name()),
            argThat(page -> page.getPageNumber() == 0 && page.getPageSize() == 10));
  }

  @Test
  void shouldLoadByIdAsView1() {
    ServiceRequestId id = new ServiceRequestId(UUID.randomUUID());
    when(springRepository.findByIdAsView1(id.value()))
        .thenReturn(java.util.Optional.of(serviceRequestView1));

    var result = repository.loadByIdAsView1(id);

    assertThat(result).isEqualTo(serviceRequestView1);
    verify(springRepository).findByIdAsView1(id.value());
  }
}
