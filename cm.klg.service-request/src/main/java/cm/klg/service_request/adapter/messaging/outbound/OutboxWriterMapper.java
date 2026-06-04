package cm.klg.service_request.adapter.messaging.outbound;

import cm.klg.generated.service.request.adapter.messaging.outbound.dto.ServiceRequestAcceptedEventDTO;
import cm.klg.service_request.domain.event.ServiceRequestAcceptedEvent;
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
  @Mapping(target = "serviceProviderId", source = "parties.serviceProviderId.value")
  @Mapping(target = "userId", source = "parties.userId.value")
  @Mapping(target = "serviceTypeId", source = "parties.serviceTypeId.value")
  @Mapping(target = "description", source = "details.description.value")
  @Mapping(target = "title", source = "details.title.value")
  @Mapping(target = "status", source = "lifecycle.status")
  @Mapping(target = "acceptedAt", source = "lifecycle.acceptAt.value")
  @Mapping(target = "createdAt", source = "lifecycle.createdAt.value")
  ServiceRequestAcceptedEventDTO toServiceRequestAcceptedEventDTO(
      ServiceRequestAcceptedEvent event);
}
