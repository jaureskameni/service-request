package cm.klg.service_request.adapter.rest.inbound;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestRegisterDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestStatusDTO;
import cm.klg.service_request.application.usecase.GetAllMyServiceRequestsUseCase;
import cm.klg.service_request.application.views.ServiceRequestViews.ServiceRequestView1;
import cm.klg.service_request.domain.service_request.ServiceRequestStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

class RestMapperTest {

  private final RestMapper mapper = new RestMapperImpl();

  @Test
  void shouldMapRegisterDtoToCreateCommandWithProvidedOptionalFields() {
    UUID userId = UUID.randomUUID();
    UUID providerId = UUID.randomUUID();
    UUID serviceTypeId = UUID.randomUUID();
    ServiceRequestRegisterDTO dto =
        new ServiceRequestRegisterDTO()
            .providerId(providerId)
            .serviceTypeId(serviceTypeId)
            .title("title")
            .description("description")
            .location("location");

    var resultUnderTest = mapper.toCreateNewServiceRequestCommand(dto, userId);

    assertThat(resultUnderTest)
        .satisfies(
            command -> {
              assertThat(command.userId().value()).isEqualTo(userId);
              assertThat(command.serviceProviderId().value()).isEqualTo(providerId);
              assertThat(command.serviceTypeId().value()).isEqualTo(serviceTypeId);
              assertThat(command.title()).isNotNull();
              assertThat(command.title().value()).isEqualTo("title");
              assertThat(command.description()).isNotNull();
              assertThat(command.description().value()).isEqualTo("description");
              assertThat(command.location()).isNotNull();
              assertThat(command.location().value()).isEqualTo("location");
            });
  }

  @Test
  void shouldMapRegisterDtoToCreateCommandWithAbsentOptionalFields() {
    ServiceRequestRegisterDTO dto =
        new ServiceRequestRegisterDTO()
            .providerId(UUID.randomUUID())
            .serviceTypeId(UUID.randomUUID());

    var resultUnderTest = mapper.toCreateNewServiceRequestCommand(dto, UUID.randomUUID());

    assertThat(resultUnderTest)
        .satisfies(
            command -> {
              assertThat(command.title()).isNull();
              assertThat(command.description()).isNull();
              assertThat(command.location()).isNull();
            });
  }

  @Test
  void shouldMapNullDtoWithCurrentUser() {
    UUID userId = UUID.randomUUID();

    var resultUnderTest = mapper.toCreateNewServiceRequestCommand(null, userId);

    assertThat(resultUnderTest)
        .satisfies(
            command -> {
              assertThat(command.userId().value()).isEqualTo(userId);
              assertThat(command.serviceProviderId()).isNull();
              assertThat(command.serviceTypeId()).isNull();
              assertThat(command.title()).isNull();
              assertThat(command.description()).isNull();
              assertThat(command.location()).isNull();
            });
  }

  @Test
  void shouldMapGetAllMyServiceRequestsCommandWithDefaults() {
    UUID userId = UUID.randomUUID();

    var resultUnderTest = mapper.toGetAllMyServiceRequestsCommand(null, null, null, userId);

    assertThat(resultUnderTest.userId().value()).isEqualTo(userId);
    assertThat(resultUnderTest.status()).isNull();
    assertThat(resultUnderTest.limit()).isEqualTo(10);
    assertThat(resultUnderTest.page()).isZero();
  }

  @Test
  void shouldMapGetAllMyServiceRequestsCommandWithStatus() {
    UUID userId = UUID.randomUUID();

    var resultUnderTest =
        mapper.toGetAllMyServiceRequestsCommand(20, ServiceRequestStatusDTO.ACCEPTED, 2, userId);

    assertThat(resultUnderTest.userId().value()).isEqualTo(userId);
    assertThat(resultUnderTest.status()).isEqualTo(ServiceRequestStatus.ACCEPTED);
    assertThat(resultUnderTest.limit()).isEqualTo(20);
    assertThat(resultUnderTest.page()).isEqualTo(2);
  }

  @Test
  void shouldMapGetAllServiceRequestsByProviderCommandWithDefaults() {
    UUID providerId = UUID.randomUUID();

    var resultUnderTest =
        mapper.toGetAllServiceRequestsByProviderCommand(providerId, null, null, null);

    assertThat(resultUnderTest.providerId().value()).isEqualTo(providerId);
    assertThat(resultUnderTest.status()).isNull();
    assertThat(resultUnderTest.limit()).isEqualTo(10);
    assertThat(resultUnderTest.page()).isZero();
  }

  @Test
  void shouldMapGetAllServiceRequestsByProviderCommandWithStatus() {
    UUID providerId = UUID.randomUUID();

    var resultUnderTest =
        mapper.toGetAllServiceRequestsByProviderCommand(
            providerId, 20, ServiceRequestStatusDTO.ACCEPTED, 2);

    assertThat(resultUnderTest.providerId().value()).isEqualTo(providerId);
    assertThat(resultUnderTest.status()).isEqualTo(ServiceRequestStatus.ACCEPTED);
    assertThat(resultUnderTest.limit()).isEqualTo(20);
    assertThat(resultUnderTest.page()).isEqualTo(2);
  }

  @Test
  void shouldMapServiceRequestPaginateDtoFromProviderResponse() {
    UUID id = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    UUID providerId = UUID.randomUUID();
    UUID serviceTypeId = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.of(2026, 6, 4, 11, 0);
    var response =
        new cm.klg.service_request.application.usecase.GetAllServiceRequestsByProviderUseCase
            .Response(
            List.of(
                new FakeServiceRequestView1(
                    id,
                    userId,
                    providerId,
                    serviceTypeId,
                    "title",
                    "description",
                    "location",
                    ServiceRequestStatus.PENDING.name(),
                    createdAt)),
            1);

    var resultUnderTest = mapper.toServiceRequestPaginateDTO(response);

    assertThat(resultUnderTest.getCount()).isEqualTo(1);
    assertThat(resultUnderTest.getServiceRequest()).hasSize(1);
  }

  @Test
  void shouldMapServiceRequestPaginateDto() {
    UUID id = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    UUID providerId = UUID.randomUUID();
    UUID serviceTypeId = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.of(2026, 6, 4, 10, 0);
    var response =
        new GetAllMyServiceRequestsUseCase.Response(
            List.of(
                new FakeServiceRequestView1(
                    id,
                    userId,
                    providerId,
                    serviceTypeId,
                    "title",
                    "description",
                    "location",
                    ServiceRequestStatus.PENDING.name(),
                    createdAt)),
            1);

    var resultUnderTest = mapper.toServiceRequestPaginateDTO(response);

    assertThat(resultUnderTest.getCount()).isEqualTo(1);
    assertThat(resultUnderTest.getServiceRequest()).hasSize(1);
    assertThat(resultUnderTest.getServiceRequest().getFirst())
        .satisfies(
            serviceRequest -> {
              assertThat(serviceRequest.getId()).isEqualTo(id);
              assertThat(serviceRequest.getUserId()).isEqualTo(userId);
              assertThat(serviceRequest.getProviderId()).isEqualTo(providerId);
              assertThat(serviceRequest.getServiceTypeId()).isEqualTo(serviceTypeId);
              assertThat(serviceRequest.getTitle()).isEqualTo("title");
              assertThat(serviceRequest.getDescription()).isEqualTo("description");
              assertThat(serviceRequest.getLocation()).isEqualTo("location");
              assertThat(serviceRequest.getStatus()).isEqualTo(ServiceRequestStatusDTO.PENDING);
              assertThat(serviceRequest.getCreatedAt()).isEqualTo(createdAt);
            });
  }

  private record FakeServiceRequestView1(
      UUID id,
      UUID userId,
      UUID serviceProviderId,
      UUID serviceTypeId,
      @Nullable String title,
      @Nullable String description,
      @Nullable String location,
      String status,
      LocalDateTime createdAt)
      implements ServiceRequestView1 {

    @Override
    public UUID getId() {
      return id;
    }

    @Override
    public UUID getUserId() {
      return userId;
    }

    @Override
    public UUID getServiceProviderId() {
      return serviceProviderId;
    }

    @Override
    public UUID getServiceTypeId() {
      return serviceTypeId;
    }

    @Override
    public @Nullable String getTitle() {
      return title;
    }

    @Override
    public @Nullable String getDescription() {
      return description;
    }

    @Override
    public @Nullable String getLocation() {
      return location;
    }

    @Override
    public String getStatus() {
      return status;
    }

    @Override
    public LocalDateTime getCreatedAt() {
      return createdAt;
    }
  }
}
