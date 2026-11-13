package cm.klg.service_request.application.views;

public interface ReviewRatingSummary {
  Double getAverageRating();

  Long getReviewCount();

  Long getOneStarCount();

  Long getTwoStarCount();

  Long getThreeStarCount();

  Long getFourStarCount();

  Long getFiveStarCount();
}
