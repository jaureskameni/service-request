package cm.klg.service_request.adapter.persistence.outbound.jpa;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.common.base.domain.PhoneNumber;
import cm.klg.service_request.domain.service_provider.ServiceProvider;
import cm.klg.service_request.domain.user.EmailAddress;
import cm.klg.service_request.domain.user.Firstname;
import cm.klg.service_request.domain.user.Lastname;
import cm.klg.service_request.domain.user.User;
import cm.klg.service_request.domain.user.UserId;
import cm.klg.service_request.domain.user.UserProfile;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JpaMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "lastname", source = "lastname.value")
  @Mapping(target = "firstname", source = "firstname.value")
  @Mapping(target = "emailAddress", source = "email.value")
  @Mapping(target = "phoneNumber", source = "phoneNumber")
  @Mapping(target = "createdAt", source = "createdAt.value")
  UserJpa toJpa(User user);

  default User toDomain(UserJpa userJpa) {
    PhoneNumber phoneNumber =
        new PhoneNumber(
            userJpa.getPhoneNumber().getCountryCode(), userJpa.getPhoneNumber().getNumber());
    UserProfile userProfile =
        new UserProfile(
            Firstname.from(userJpa.getFirstname()),
            Lastname.from(userJpa.getLastname()),
            EmailAddress.from(userJpa.getEmailAddress()),
            phoneNumber);
    return User.reconstitute(
        new UserId(userJpa.getId()),
        userProfile,
        userJpa.isServiceProvider(),
        CreatedAt.from(userJpa.getCreatedAt()));
  }

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "userId", source = "userId.value")
  @Mapping(target = "status", source = "status")
  @Mapping(target = "approvedAt", source = "approvedAt.value")
  ServiceProviderJpa toJpa(ServiceProvider serviceProvider);
}
