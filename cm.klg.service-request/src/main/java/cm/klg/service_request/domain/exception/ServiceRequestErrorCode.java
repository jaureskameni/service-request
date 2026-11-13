package cm.klg.service_request.domain.exception;

import cm.klg.common.base.utils.ErrorCode;
import lombok.Getter;

public enum ServiceRequestErrorCode implements ErrorCode {

  // 400
  SERVICE_REQUEST_400_001("SERVICE_REQUEST_400_001", "Invalid Pagination Data"),
  SERVICE_REQUEST_400_002("SERVICE_REQUEST_400_002", "Invalid Review Data"),
  SERVICE_REQUEST_400_003("SERVICE_REQUEST_400_003", "Invalid Review Moderation Data"),

  // ERROR-403
  SERVICE_REQUEST_403_001(
      "SERVICE_REQUEST_403_001", "Request Does Not Belong To Provider Exception"),
  SERVICE_REQUEST_403_002("SERVICE_REQUEST_403_002", "Request Does Not Belong To User Exception"),
  SERVICE_REQUEST_403_003("SERVICE_REQUEST_403_003", "Unauthorized Provider Exception"),
  SERVICE_REQUEST_403_004(
      "SERVICE_REQUEST_403_004", "User Is Not Involved In Service Request Exception"),
  SERVICE_REQUEST_403_005(
      "SERVICE_REQUEST_403_005", "Provider Is Not Involved In Service Request Exception"),

  // ERROR-404
  SERVICE_REQUEST_404_001("SERVICE_REQUEST_404_001", "User Not Found"),
  SERVICE_REQUEST_404_002("SERVICE_REQUEST_404_002", "Service Provider Not Found"),
  SERVICE_REQUEST_404_003("SERVICE_REQUEST_404_003", "Service Request Not Found"),
  SERVICE_REQUEST_404_004("SERVICE_REQUEST_404_004", "Service Request Follow-up Not Found"),
  SERVICE_REQUEST_404_005("SERVICE_REQUEST_404_005", "Review Not Found"),

  // ERROR-409
  SERVICE_REQUEST_409_001("SERVICE_REQUEST_409_001", "Invalid Service Request Status"),
  SERVICE_REQUEST_409_002("SERVICE_REQUEST_409_002", "Invalid Follow-up Transition"),
  SERVICE_REQUEST_409_003(
      "SERVICE_REQUEST_409_003", "Completion Confirmation Is Not Yet Available"),
  SERVICE_REQUEST_409_004("SERVICE_REQUEST_409_004", "Review Is Not Eligible"),
  SERVICE_REQUEST_409_005("SERVICE_REQUEST_409_005", "Review Already Exists"),
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
