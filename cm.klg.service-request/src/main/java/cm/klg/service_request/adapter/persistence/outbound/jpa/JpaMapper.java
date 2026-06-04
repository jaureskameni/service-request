package cm.klg.service_request.adapter.persistence.outbound.jpa;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.common.base.entity.PhoneNumberJpa;
import cm.klg.service_request.domain.service_provider.ServiceProvider;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_request.domain.service_request.ServiceRequest;
import cm.klg.service_request.domain.service_request.ServiceRequestDescription;
import cm.klg.service_request.domain.service_request.ServiceRequestDetails;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.domain.service_request.ServiceRequestLifecycle;
import cm.klg.service_request.domain.service_request.ServiceRequestLocation;
import cm.klg.service_request.domain.service_request.ServiceRequestParties;
import cm.klg.service_request.domain.service_request.ServiceRequestStatus;
import cm.klg.service_request.domain.service_request.ServiceRequestTitle;
import cm.klg.service_request.domain.service_request.ServiceTypeId;
import cm.klg.service_request.domain.user.EmailAddress;
import cm.klg.service_request.domain.user.Firstname;
import cm.klg.service_request.domain.user.Lastname;
import cm.klg.service_request.domain.user.PhoneNumber;
import cm.klg.service_request.domain.user.User;
import cm.klg.service_request.domain.user.UserId;
import cm.klg.service_request.domain.user.UserProfile;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
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

  default PhoneNumberJpa toJpa(PhoneNumber phoneNumber) {
    if (phoneNumber == null) {
      return null;
    }
    return new PhoneNumberJpa(phoneNumber.countryCode(), phoneNumber.number());
  }

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "userId", source = "userId.value")
  @Mapping(target = "serviceTypeId", source = "serviceTypeId.value")
  @Mapping(target = "providerId", source = "serviceProviderId.value")
  @Mapping(target = "title", source = "title")
  @Mapping(target = "description", source = "description")
  @Mapping(target = "location", source = "location")
  @Mapping(target = "status", source = "status")
  @Mapping(target = "acceptedAt", source = "acceptAt.value")
  @Mapping(target = "createdAt", source = "createdAt.value")
  ServiceRequestJpa toJpa(@NonNull ServiceRequest serviceRequest);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "userId", source = "userId.value")
  @Mapping(target = "serviceTypeId", source = "serviceTypeId.value")
  @Mapping(target = "providerId", source = "serviceProviderId.value")
  @Mapping(target = "title", source = "title")
  @Mapping(target = "description", source = "description")
  @Mapping(target = "location", source = "location")
  @Mapping(target = "status", source = "status")
  @Mapping(target = "acceptedAt", source = "acceptAt.value")
  @Mapping(target = "createdAt", source = "createdAt.value")
  void toJpa(@MappingTarget ServiceRequestJpa serviceRequestJpa, ServiceRequest serviceRequest);

  default @Nullable String toJpaValue(@Nullable ServiceRequestTitle title) {
    return title == null ? null : title.value();
  }

  default @Nullable String toJpaValue(@Nullable ServiceRequestDescription description) {
    return description == null ? null : description.value();
  }

  default @Nullable String toJpaValue(@Nullable ServiceRequestLocation location) {
    return location == null ? null : location.value();
  }

  default ServiceProvider toDomain(ServiceProviderJpa jpa) {
    return ServiceProvider.reconstitute(
        ServiceProviderId.from(jpa.getId()),
        UserId.from(jpa.getUserId()),
        ServiceProviderStatus.valueOf(jpa.getStatus()),
        CreatedAt.from(jpa.getApprovedAt()));
  }

  default ServiceRequest toDomain(ServiceRequestJpa jpa) {
    return ServiceRequest.reconstitute(
        ServiceRequestId.from(jpa.getId()),
        new ServiceRequestParties(
            UserId.from(jpa.getUserId()),
            ServiceProviderId.from(jpa.getProviderId()),
            ServiceTypeId.from(jpa.getServiceTypeId())),
        new ServiceRequestDetails(
            jpa.getTitle() == null ? null : ServiceRequestTitle.from(jpa.getTitle()),
            jpa.getDescription() == null
                ? null
                : ServiceRequestDescription.from(jpa.getDescription()),
            jpa.getLocation() == null ? null : ServiceRequestLocation.from(jpa.getLocation())),
        new ServiceRequestLifecycle(
            ServiceRequestStatus.valueOf(jpa.getStatus()),
            jpa.getAcceptedAt() == null ? null : CreatedAt.from(jpa.getAcceptedAt()),
            CreatedAt.from(jpa.getCreatedAt())));
  }
}
