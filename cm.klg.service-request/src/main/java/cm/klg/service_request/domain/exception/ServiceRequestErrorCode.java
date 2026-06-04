package cm.klg.service_request.domain.exception;

import cm.klg.common.base.utils.ErrorCode;
import lombok.Getter;

public enum ServiceRequestErrorCode implements ErrorCode {
  // ERROR-403
  SERVICE_REQUEST_403_001(
      "SERVICE_REQUEST_403_001", "Request Does Not Belong To Provider Exception"),

  // ERROR-404
  SERVICE_REQUEST_404_001("SERVICE_REQUEST_404_001", "User Not Found"),
  SERVICE_REQUEST_404_002("SERVICE_REQUEST_404_002", "Service Provider Not Found"),
  SERVICE_REQUEST_404_003("SERVICE_REQUEST_404_003", "Service Request Not Found");

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
