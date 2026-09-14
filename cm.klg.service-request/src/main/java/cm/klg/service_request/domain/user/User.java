package cm.klg.service_request.domain.user;

import cm.klg.common.base.domain.CreatedAt;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

@Getter
public class User {
  private final UserId id;
  @Nullable private Firstname firstname;
  private Lastname lastname;
  @Nullable private EmailAddress email;
  private PhoneNumber phoneNumber;
  private boolean isServiceProvider;
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

  public void updateProfile(UserProfile userProfile) {
    this.firstname = userProfile.firstname();
    this.lastname = userProfile.lastname();
    this.email = userProfile.email();
    this.phoneNumber = userProfile.phoneNumber();
  }
}
