package cm.klg.service_request.adapter.persistence.outbound.jpa;

import cm.klg.common.base.entity.PhoneNumberJpaConverter;
import cm.klg.service_request.application.outbound.UserRepository;
import cm.klg.service_request.domain.user.IdentityId;
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
              userJpa.getIdentityId(),
              userJpa.getFirstname(),
              userJpa.getLastname(),
              userJpa.getEmailAddress(),
              PHONE_NUMBER_CONVERTER.convertToDatabaseColumn(userJpa.getPhoneNumber()),
              userJpa.isServiceProvider(),
              userJpa.getCreatedAt());
      if (insertedRows == 0) {
        log.debug(
            "User with identityId {} already exists, skipping insertion.",
            user.getIdentityId().value());
      }
    } catch (DataIntegrityViolationException e) {
      log.warn(
          "Unexpected constraint violation while creating user with identityId {}",
          user.getIdentityId().value(),
          e);
    }
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

  @Override
  public User loadByIdentityId(@NonNull IdentityId identityId) {
    return userSpringRepository
        .findByIdentityId(identityId.value())
        .map(jpaMapper::toDomain)
        .orElseThrow(UserNotFoundException::new);
  }

  @Override
  public void update(@NonNull User user) {
    userSpringRepository.save(jpaMapper.toUserJpa(user));
  }
}
