package cm.klg.service_request.adapter.messaging.outbound;

import cm.klg.generated.service.request.adapter.messaging.outbound.dto.ServiceRequestAcceptedEventDTO;
import cm.klg.generated.service.request.adapter.messaging.outbound.dto.ServiceRequestCancelledEventDTO;
import cm.klg.generated.service.request.adapter.messaging.outbound.dto.ServiceRequestCreatedEventDTO;
import cm.klg.generated.service.request.adapter.messaging.outbound.dto.ServiceRequestRejectedEventDTO;
import cm.klg.service_request.domain.event.ServiceRequestAcceptedEvent;
import cm.klg.service_request.domain.event.ServiceRequestCancelledEvent;
import cm.klg.service_request.domain.event.ServiceRequestCreatedEvent;
import cm.klg.service_request.domain.event.ServiceRequestRejectedEvent;
import cm.klg.service_request.domain.service_request.ServiceRequestDescription;
import cm.klg.service_request.domain.service_request.ServiceRequestLocation;
import cm.klg.service_request.domain.service_request.ServiceRequestTitle;
import org.jspecify.annotations.Nullable;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OutboxWriterMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "userId", source = "userId.value")
  @Mapping(target = "providerId", source = "providerId.value")
  @Mapping(target = "updatedAt", source = "updatedAt.value")
  ServiceRequestAcceptedEventDTO toServiceRequestAcceptedEventDTO(
      ServiceRequestAcceptedEvent event);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "userId", source = "userId.value")
  @Mapping(target = "providerId", source = "providerId.value")
  @Mapping(target = "rejectedAt", source = "updatedAt.value")
  @Mapping(target = "reason", source = "reason.value")
  ServiceRequestRejectedEventDTO toServiceRequestRejectedEventDTO(
      ServiceRequestRejectedEvent event);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "userId", source = "userId.value")
  @Mapping(target = "providerId", source = "providerId.value")
  @Mapping(target = "cancelledAt", source = "updatedAt.value")
  ServiceRequestCancelledEventDTO toServiceRequestCancelledEventDTO(
      ServiceRequestCancelledEvent event);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "providerId", source = "parties.serviceProviderId.value")
  @Mapping(target = "userId", source = "parties.userId.value")
  @Mapping(target = "serviceTypeId", source = "parties.serviceTypeId.value")
  @Mapping(target = "description", source = "details.description")
  @Mapping(target = "title", source = "details.title")
  @Mapping(target = "location", source = "details.location")
  @Mapping(target = "status", source = "lifecycle.status")
  @Mapping(target = "createdAt", source = "lifecycle.createdAt.value")
  ServiceRequestCreatedEventDTO toServiceRequestCreatedEventDTO(ServiceRequestCreatedEvent event);

  default @Nullable String map(@Nullable ServiceRequestTitle title) {
    return title == null ? null : title.value();
  }

  default @Nullable String map(@Nullable ServiceRequestDescription description) {
    return description == null ? null : description.value();
  }

  default @Nullable String map(@Nullable ServiceRequestLocation location) {
    return location == null ? null : location.value();
  }
}
