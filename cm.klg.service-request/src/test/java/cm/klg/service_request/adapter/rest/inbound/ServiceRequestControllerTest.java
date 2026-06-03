package cm.klg.service_request.adapter.rest.inbound;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.CreationResponseDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestRegisterDTO;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@ExtendWith(MockitoExtension.class)
class ServiceRequestControllerTest {
  @Mock private UseCaseExecutor useCaseExecutor;

  @InjectMocks private ServiceRequestController objectUnderTest;

  @Test
  void createNewServiceRequestTest() {
    // Given
    var providerRegisterDTO =
        new ServiceRequestRegisterDTO()
            .providerId(UUID.randomUUID())
            .serviceTypeId(UUID.randomUUID())
            .title("title")
            .description("description")
            .location("location");
    var serviceRequestId = new ServiceRequestId(UUID.randomUUID());

    BDDMockito.given(useCaseExecutor.executeCommand(any())).willReturn(serviceRequestId);

    // When
    var result =
        // spotless:off
                given()
                        .standaloneSetup(objectUnderTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(providerRegisterDTO)
                        .when()
                        .post("/service-request")
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .as(CreationResponseDTO.class);
        // spotless:on
    // Then
    assertThat(result.getNewId()).isEqualTo(serviceRequestId.value());
  }
}
