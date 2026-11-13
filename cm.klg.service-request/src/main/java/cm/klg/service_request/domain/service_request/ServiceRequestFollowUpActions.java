package cm.klg.service_request.domain.service_request;

public record ServiceRequestFollowUpActions(
    boolean canAnswerAgreement,
    boolean canAnswerCompletion,
    boolean canSubmitReview,
    FollowUpDateTime completionAvailableAt) {

  public static ServiceRequestFollowUpActions none() {
    return new ServiceRequestFollowUpActions(false, false, false, FollowUpDateTime.NULL);
  }
}
