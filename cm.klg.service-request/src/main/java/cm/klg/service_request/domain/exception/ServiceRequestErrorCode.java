package cm.klg.service_request.domain.exception;

import cm.klg.common.base.utils.ErrorCode;
import lombok.Getter;

public enum ServiceRequestErrorCode implements ErrorCode {
  // ERROR-404
  SERVICE_REQUEST_404_001("SERVICE_REQUEST_404_001", "User Not Found"),
  SERVICE_REQUEST_404_002("SERVICE_REQUEST_404_002", "Service Provider Not Found");

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
