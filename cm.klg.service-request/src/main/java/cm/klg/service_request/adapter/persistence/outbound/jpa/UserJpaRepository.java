package cm.klg.service_request.adapter.persistence.outbound.jpa;

import cm.klg.service_request.application.outbound.UserRepository;
import cm.klg.service_request.domain.user.User;
import cm.klg.service_request.domain.user.UserId;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;

@RequiredArgsConstructor
public class UserJpaRepository implements UserRepository {

  private final UserSpringRepository userSpringRepository;
  private final JpaMapper jpaMapper;

  @Override
  public void insert(@NonNull User user) {
    userSpringRepository.save(jpaMapper.toJpa(user));
  }

  @Override
  public boolean existsById(@NonNull UserId userId) {
    return userSpringRepository.existsById(userId.value());
  }

  @Override
  public Optional<User> findById(@NonNull UserId userId) {
    return userSpringRepository.findById(userId.value()).map(jpaMapper::toDomain);
  }

  @Override
  public User load(@NonNull UserId userId) {
    return findById(userId).orElseThrow();
  }
}
