package cm.klg.service_request.domain.review;

import cm.klg.service_request.domain.UpdatedAt;
import cm.klg.service_request.domain.user.UserId;

public record Moderation(ReviewComment reason, UserId moderatorId, UpdatedAt moderatedAt) {
  public static final Moderation NONE =
      new Moderation(ReviewComment.NULL, UserId.NULL, UpdatedAt.NULL);
}
