package cm.klg.service_request.application.views;

public record ReviewSummary(
    double averageRating,
    long reviewCount,
    long oneStarCount,
    long twoStarCount,
    long threeStarCount,
    long fourStarCount,
    long fiveStarCount) {}
