package cm.klg.service_request.adapter.persistence.outbound.jpa;

import cm.klg.service_request.application.outbound.UserRepository;
import cm.klg.service_request.domain.user.IdentityId;
import cm.klg.service_request.domain.user.User;
import cm.klg.service_request.domain.user.UserId;
import cm.klg.service_request.domain.user.UserNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;

@RequiredArgsConstructor
public class UserJpaRepository implements UserRepository {

  private final UserSpringRepository userSpringRepository;
  private final JpaMapper jpaMapper;

  @Override
  public void insert(@NonNull User user) {
    userSpringRepository.save(jpaMapper.toUserJpa(user));
  }

  @Override
  public boolean existsByUserId(@NonNull IdentityId userId) {
    return userSpringRepository.existsByIdentityId(userId.value());
  }

  @Override
  public Optional<User> loadById(@NonNull UserId userId) {
    return userSpringRepository.findById(userId.value()).map(jpaMapper::toDomain);
  }

  @Override
  public User load(@NonNull UserId userId) {
    return loadById(userId).orElseThrow(UserNotFoundException::new);
  }
}
