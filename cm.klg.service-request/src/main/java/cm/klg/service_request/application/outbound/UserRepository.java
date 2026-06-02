package cm.klg.service_request.application.outbound;

import cm.klg.service_request.domain.user.User;
import cm.klg.service_request.domain.user.UserId;
import java.util.Optional;

public interface UserRepository {
  void insert(User newUser);

  boolean existsById(UserId userId);

  Optional<User> findById(UserId userId);

  User load(UserId userId);
}
