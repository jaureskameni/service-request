package cm.klg.service_request.domain.service_request;

import java.time.Duration;
import java.time.LocalDateTime;

/** Bilateral agreement and completion state owned by a service request. */
public class ServiceRequestFollowUp {
  public static final ServiceRequestFollowUp NULL = empty(ServiceRequestFollowUpId.NULL);

  private final ServiceRequestFollowUpId id;
  private BilateralAnswers agreement;
  private BilateralAnswers completion;
  private FollowUpDateTime scheduledAt;
  private FollowUpDateTime agreementConfirmedAt;

  private ServiceRequestFollowUp(
      ServiceRequestFollowUpId id,
      BilateralAnswers agreement,
      BilateralAnswers completion,
      FollowUpDateTime scheduledAt,
      FollowUpDateTime agreementConfirmedAt) {
    this.id = id;
    this.agreement = agreement;
    this.completion = completion;
    this.scheduledAt = scheduledAt;
    this.agreementConfirmedAt = agreementConfirmedAt;
  }

  private static ServiceRequestFollowUp empty(ServiceRequestFollowUpId id) {
    return new ServiceRequestFollowUp(
        id,
        BilateralAnswers.EMPTY,
        BilateralAnswers.EMPTY,
        FollowUpDateTime.NULL,
        FollowUpDateTime.NULL);
  }

  public static ServiceRequestFollowUp start() {
    return empty(ServiceRequestFollowUpId.generate());
  }

  public static ServiceRequestFollowUp reconstitute(
      ServiceRequestFollowUpId id,
      FollowUpAnswer clientAgreement,
      FollowUpDateTime clientAgreementAt,
      FollowUpAnswer providerAgreement,
      FollowUpDateTime providerAgreementAt,
      FollowUpDateTime scheduledAt,
      FollowUpDateTime agreementConfirmedAt,
      FollowUpAnswer clientCompletion,
      FollowUpDateTime clientCompletionAt,
      FollowUpAnswer providerCompletion,
      FollowUpDateTime providerCompletionAt) {
    return new ServiceRequestFollowUp(
        id,
        new BilateralAnswers(
            clientAgreement, clientAgreementAt, providerAgreement, providerAgreementAt),
        new BilateralAnswers(
            clientCompletion, clientCompletionAt, providerCompletion, providerCompletionAt),
        scheduledAt,
        agreementConfirmedAt);
  }

  void answerAgreement(FollowUpParty party, FollowUpAnswer answer) {
    requireOpenStage(answer, agreement);
    FollowUpDateTime answeredAt = FollowUpDateTime.from(LocalDateTime.now());
    agreement = agreement.withAnswer(party, answer, answeredAt);
    if (agreement.bothYes()) {
      agreementConfirmedAt = answeredAt;
    }
  }

  void answerCompletion(
      FollowUpParty party, FollowUpAnswer completionAnswer, Duration completionDelay) {
    requireOpenStage(completionAnswer, completion);
    if (!agreement.bothYes()) {
      throw new InvalidFollowUpTransitionException();
    }
    LocalDateTime answeredAt = LocalDateTime.now();
    if (answeredAt.isBefore(completionAvailableAt(completionDelay))) {
      throw new CompletionNotYetAvailableException();
    }
    completion = completion.withAnswer(party, completionAnswer, FollowUpDateTime.from(answeredAt));
  }

  public LocalDateTime completionAvailableAt(Duration completionDelay) {
    if (isNull() || !agreement.bothYes()) {
      throw new InvalidFollowUpTransitionException();
    }
    return scheduledAt.isNull()
        ? agreementConfirmedAt.valueOrThrow().plus(completionDelay)
        : scheduledAt.valueOrThrow();
  }

  public boolean isReviewEligible() {
    return completion.client() == FollowUpAnswer.YES;
  }

  ServiceRequestFollowUpActions actionsFor(
      FollowUpParty party, Duration completionDelay, boolean reviewAlreadyExists) {
    if (isNull()) {
      return ServiceRequestFollowUpActions.none();
    }
    return new ServiceRequestFollowUpActions(
        canAnswerAgreement(),
        canAnswerCompletion(completionDelay),
        canSubmitReview(party, reviewAlreadyExists),
        completionWindowStart(completionDelay));
  }

  public FollowUpStatus status() {
    if (isNull()) {
      return FollowUpStatus.WAITING_FOR_AGREEMENT;
    }
    if (agreementConfirmedAt.isNull()) {
      return agreementStatus();
    }
    return completionStatus();
  }

  public boolean isNull() {
    return this == NULL;
  }

  public ServiceRequestFollowUpId getId() {
    return id;
  }

  public FollowUpAnswer getClientAgreement() {
    return agreement.client();
  }

  public FollowUpDateTime getClientAgreementAt() {
    return agreement.clientAt();
  }

  public FollowUpAnswer getProviderAgreement() {
    return agreement.provider();
  }

  public FollowUpDateTime getProviderAgreementAt() {
    return agreement.providerAt();
  }

  public FollowUpDateTime getScheduledAt() {
    return scheduledAt;
  }

  public FollowUpDateTime getAgreementConfirmedAt() {
    return agreementConfirmedAt;
  }

  public FollowUpAnswer getClientCompletion() {
    return completion.client();
  }

  public FollowUpDateTime getClientCompletionAt() {
    return completion.clientAt();
  }

  public FollowUpAnswer getProviderCompletion() {
    return completion.provider();
  }

  public FollowUpDateTime getProviderCompletionAt() {
    return completion.providerAt();
  }

  private boolean canAnswerAgreement() {
    return !agreement.isClosed();
  }

  private boolean canAnswerCompletion(Duration completionDelay) {
    if (!agreement.bothYes() || completion.isClosed()) {
      return false;
    }
    return !LocalDateTime.now().isBefore(completionAvailableAt(completionDelay));
  }

  private boolean canSubmitReview(FollowUpParty party, boolean reviewAlreadyExists) {
    return party == FollowUpParty.CLIENT && isReviewEligible() && !reviewAlreadyExists;
  }

  private FollowUpDateTime completionWindowStart(Duration completionDelay) {
    if (!agreement.bothYes()) {
      return FollowUpDateTime.NULL;
    }
    return FollowUpDateTime.from(completionAvailableAt(completionDelay));
  }

  private FollowUpStatus agreementStatus() {
    if (!agreement.isClosed()) {
      return FollowUpStatus.WAITING_FOR_AGREEMENT;
    }
    return agreement.bothNo() ? FollowUpStatus.NO_AGREEMENT : FollowUpStatus.AGREEMENT_DISPUTED;
  }

  private FollowUpStatus completionStatus() {
    if (completion.client() == FollowUpAnswer.YES) {
      return switch (completion.provider()) {
        case YES -> FollowUpStatus.COMPLETION_CONFIRMED;
        case NO -> FollowUpStatus.COMPLETION_DISPUTED;
        case NULL -> FollowUpStatus.COMPLETION_REPORTED_BY_CLIENT;
      };
    }
    return completion.bothNo()
        ? FollowUpStatus.NOT_COMPLETED
        : FollowUpStatus.WAITING_FOR_COMPLETION;
  }

  private void requireOpenStage(FollowUpAnswer answer, BilateralAnswers stage) {
    if (isNull() || answer == FollowUpAnswer.NULL || stage.isClosed()) {
      throw new InvalidFollowUpTransitionException();
    }
  }

  private record BilateralAnswers(
      FollowUpAnswer client,
      FollowUpDateTime clientAt,
      FollowUpAnswer provider,
      FollowUpDateTime providerAt) {

    static final BilateralAnswers EMPTY =
        new BilateralAnswers(
            FollowUpAnswer.NULL, FollowUpDateTime.NULL, FollowUpAnswer.NULL, FollowUpDateTime.NULL);

    boolean isClosed() {
      return client != FollowUpAnswer.NULL && provider != FollowUpAnswer.NULL;
    }

    boolean isEmpty() {
      return client == FollowUpAnswer.NULL && provider == FollowUpAnswer.NULL;
    }

    boolean bothYes() {
      return client == FollowUpAnswer.YES && provider == FollowUpAnswer.YES;
    }

    boolean bothNo() {
      return client == FollowUpAnswer.NO && provider == FollowUpAnswer.NO;
    }

    BilateralAnswers withAnswer(FollowUpParty party, FollowUpAnswer answer, FollowUpDateTime at) {
      return party == FollowUpParty.CLIENT
          ? new BilateralAnswers(answer, at, provider, providerAt)
          : new BilateralAnswers(client, clientAt, answer, at);
    }
  }
}
