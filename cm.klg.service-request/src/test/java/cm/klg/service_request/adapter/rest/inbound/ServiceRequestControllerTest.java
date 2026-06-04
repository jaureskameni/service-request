package cm.klg.service_request.adapter.rest.inbound;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.CreationResponseDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestPaginateDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestRegisterDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestStatusDTO;
import cm.klg.service_request.application.usecase.CreateNewServiceRequestUseCase;
import cm.klg.service_request.application.usecase.GetAllMyServiceRequestsUseCase;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import java.util.List;
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
  @Mock private RestMapper restMapper;
  @Mock private CreateNewServiceRequestUseCase createNewServiceRequestUseCase;
  @Mock private GetAllMyServiceRequestsUseCase getAllMyServiceRequestsUseCase;

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

  @Test
  void getAllMyServiceRequestTest() {
    // Given

    ServiceRequestStatusDTO statusDTO = ServiceRequestStatusDTO.ACCEPTED;
    var serviceProviderDTO = new ServiceRequestDTO().id(UUID.randomUUID());
    var useCaseResponse = new GetAllMyServiceRequestsUseCase.Response(List.of(), 1L);
    var paginateDTO =
        new ServiceRequestPaginateDTO().count(1L).serviceRequest(List.of(serviceProviderDTO));

    BDDMockito.given(useCaseExecutor.executeQuery(any())).willReturn(useCaseResponse);
    BDDMockito.given(restMapper.toServiceRequestPaginateDTO(useCaseResponse))
        .willReturn(paginateDTO);
    // When
    var result =
        // spotless:off
            given()
                    .standaloneSetup(objectUnderTest)
                    .contentType(MediaType.APPLICATION_JSON)
                    .queryParam("status", statusDTO)
                    .queryParam("page", "0")
                    .queryParam("limit", "10")
            .when()
                    .get("/my/service-request")
            .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ServiceRequestPaginateDTO.class);
      // spotless:on
    // Then
    assertThat(result.getCount()).isEqualTo(1);
    assertThat(result.getServiceRequest()).hasSize(1);
  }

  @Test
  void getAllServiceRequestsByProviderTest() {
    // Given
    UUID providerId = UUID.randomUUID();
    ServiceRequestStatusDTO statusDTO = ServiceRequestStatusDTO.ACCEPTED;
    var serviceProviderDTO = new ServiceRequestDTO().id(UUID.randomUUID());
    var useCaseResponse =
        new cm.klg.service_request.application.usecase.GetAllServiceRequestsByProviderUseCase
            .Response(List.of(), 1L);
    var paginateDTO =
        new ServiceRequestPaginateDTO().count(1L).serviceRequest(List.of(serviceProviderDTO));

    BDDMockito.given(useCaseExecutor.executeQuery(any())).willReturn(useCaseResponse);
    BDDMockito.given(restMapper.toServiceRequestPaginateDTO(useCaseResponse))
        .willReturn(paginateDTO);

    // When
    var result =
        // spotless:off
            given()
                    .standaloneSetup(objectUnderTest)
                    .contentType(MediaType.APPLICATION_JSON)
                    .queryParam("status", statusDTO)
                    .queryParam("page", "0")
                    .queryParam("limit", "10")
            .when()
                    .get("/service-provider/{serviceProviderId}/service-request", providerId)
            .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ServiceRequestPaginateDTO.class);
      // spotless:on

    // Then
    assertThat(result.getCount()).isEqualTo(1);
    assertThat(result.getServiceRequest()).hasSize(1);
  }
}
