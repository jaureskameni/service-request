package cm.klg.service_request.domain.exception;

import cm.klg.common.base.utils.ErrorCode;
import lombok.Getter;

public enum ServiceRequestErrorCode implements ErrorCode {
  // ERROR-403
  SERVICE_REQUEST_403_001(
      "SERVICE_REQUEST_403_001", "Request Does Not Belong To Provider Exception"),
  SERVICE_REQUEST_403_002("SERVICE_REQUEST_403_002", "Request Does Not Belong To User Exception"),
  SERVICE_REQUEST_403_003("SERVICE_REQUEST_403_003", "Unauthorized Provider Exception"),

  // ERROR-404
  SERVICE_REQUEST_404_001("SERVICE_REQUEST_404_001", "User Not Found"),
  SERVICE_REQUEST_404_002("SERVICE_REQUEST_404_002", "Service Provider Not Found"),
  SERVICE_REQUEST_404_003("SERVICE_REQUEST_404_003", "Service Request Not Found"),

  // ERROR-409
  SERVICE_REQUEST_409_001("SERVICE_REQUEST_409_001", "Invalid Service Request Status"),
  ;

  private final String value;
  @Getter private final String description;

  ServiceRequestErrorCode(String value, String description) {
    this.value = value;
    this.description = description;
  }

  public String value() {
    return value;
  }
}
