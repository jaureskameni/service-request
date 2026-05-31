package cm.klg.service_request.adapter.persistence.outbound.jpa;

import cm.klg.service_request.application.outbound.UserRepository;
import cm.klg.service_request.domain.user.User;
import cm.klg.service_request.domain.user.UserId;
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
  public User load(@NonNull UserId userId) {
    return userSpringRepository.findById(userId.value()).map(jpaMapper::toUserDomain).orElseThrow();
  }
}
