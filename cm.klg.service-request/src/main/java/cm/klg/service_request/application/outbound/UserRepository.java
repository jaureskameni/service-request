package cm.klg.service_request.application.outbound;


import cm.klg.service_request.domain.user.User;
import cm.klg.service_request.domain.user.UserId;

public interface UserRepository {
  void insert(User newUser);

  User load(UserId userId);
}
