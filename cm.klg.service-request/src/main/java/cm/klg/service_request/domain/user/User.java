package cm.klg.service_request.domain.user;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.common.base.domain.PhoneNumber;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

@Getter
public class User {
  private final UserId id;
  @Nullable private final Firstname firstname;
  private final Lastname lastname;
  @Nullable private final EmailAddress email;
  private final PhoneNumber phoneNumber;
  private final boolean isServiceProvider;
  private final CreatedAt createdAt;

  public User(UserId id, UserProfile userProfile, boolean isServiceProvider, CreatedAt createdAt) {
    this.id = id;
    this.firstname = userProfile.firstname();
    this.lastname = userProfile.lastname();
    this.email = userProfile.email();
    this.phoneNumber = userProfile.phoneNumber();
    this.isServiceProvider = isServiceProvider;
    this.createdAt = createdAt;
  }

  public static User reconstitute(
      UserId id, UserProfile userProfile, boolean isServiceProvider, CreatedAt createdAt) {
    return new User(id, userProfile, isServiceProvider, createdAt);
  }
}
