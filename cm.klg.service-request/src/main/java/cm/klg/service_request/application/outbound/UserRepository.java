package cm.klg.service_request.application.outbound;

import cm.klg.service_request.domain.user.User;
import cm.klg.service_request.domain.user.UserId;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

public interface UserRepository {
  void insertIfAbsent(@NonNull User user);

  boolean existsByUserId(@NonNull UserId userId);

  Optional<User> loadById(@NonNull UserId userId);

  User load(@NonNull UserId userId);

  void update(@NonNull User user);

  void delete(@NonNull UserId userId);
}
