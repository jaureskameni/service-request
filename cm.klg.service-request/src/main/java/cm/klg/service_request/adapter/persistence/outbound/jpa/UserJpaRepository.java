package cm.klg.service_request.adapter.persistence.outbound.jpa;

import cm.klg.common.base.entity.PhoneNumberJpaConverter;
import cm.klg.service_request.application.outbound.UserRepository;
import cm.klg.service_request.domain.user.User;
import cm.klg.service_request.domain.user.UserId;
import cm.klg.service_request.domain.user.UserNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.DataIntegrityViolationException;

@Slf4j
@RequiredArgsConstructor
public class UserJpaRepository implements UserRepository {

  private static final PhoneNumberJpaConverter PHONE_NUMBER_CONVERTER =
      new PhoneNumberJpaConverter();

  private final UserSpringRepository userSpringRepository;
  private final JpaMapper jpaMapper;

  @Override
  public void insertIfAbsent(@NonNull User user) {
    UserJpa userJpa = jpaMapper.toUserJpa(user);
    try {
      int insertedRows =
          userSpringRepository.insertIfAbsent(
              userJpa.getId(),
              userJpa.getFirstname(),
              userJpa.getLastname(),
              userJpa.getEmailAddress(),
              PHONE_NUMBER_CONVERTER.convertToDatabaseColumn(userJpa.getPhoneNumber()),
              userJpa.isServiceProvider(),
              userJpa.getCreatedAt());
      if (insertedRows == 0) {
        log.debug("User with id {} already exists, skipping insertion.", user.getId().value());
      }
    } catch (DataIntegrityViolationException e) {
      log.warn(
          "Unexpected constraint violation while creating user with id {}",
          user.getId().value(),
          e);
    }
  }

  @Override
  public boolean existsByUserId(@NonNull UserId userId) {
    return userSpringRepository.existsById(userId.value());
  }

  @Override
  public Optional<User> loadById(@NonNull UserId userId) {
    return userSpringRepository.findById(userId.value()).map(jpaMapper::toDomain);
  }

  @Override
  public User load(@NonNull UserId userId) {
    return loadById(userId).orElseThrow(UserNotFoundException::new);
  }

  @Override
  public void update(@NonNull User user) {
    userSpringRepository.save(jpaMapper.toUserJpa(user));
  }

  @Override
  public void delete(@NonNull UserId userId) {
    userSpringRepository.deleteById(userId.value());
  }
}
