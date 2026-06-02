package cm.klg.service_request.domain.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ServiceRequestErrorCodeTest {

  @Test
  void shouldExposeErrorCodeValuesAndDescriptions() {
    assertThat(ServiceRequestErrorCode.SERVICE_REQUEST_404_001.value())
        .isEqualTo("SERVICE_REQUEST_404_001");
    assertThat(ServiceRequestErrorCode.SERVICE_REQUEST_404_001.getDescription())
        .isEqualTo("User Not Found");
    assertThat(ServiceRequestErrorCode.SERVICE_REQUEST_404_002.value())
        .isEqualTo("SERVICE_REQUEST_404_002");
    assertThat(ServiceRequestErrorCode.SERVICE_REQUEST_404_002.getDescription())
        .isEqualTo("Service Provider Not Found");
  }
}
