package cm.klg.service_request.domain.review;

public record ReviewRating(int value) {
  public static ReviewRating from(int value) {
    if (value < 1 || value > 5) throw new InvalidReviewException();
    return new ReviewRating(value);
  }
}
