package cm.klg.service_request.adapter.rest.inbound;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestRegisterDTO;
import java.util.UUID;
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
}
