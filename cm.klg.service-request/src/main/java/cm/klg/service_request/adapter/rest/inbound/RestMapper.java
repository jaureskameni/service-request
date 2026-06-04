package cm.klg.service_request.adapter.rest.inbound;

import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestRegisterDTO;
import cm.klg.service_request.application.usecase.CreateNewServiceRequestUseCase;
import cm.klg.service_request.domain.service_request.ServiceRequestDescription;
import cm.klg.service_request.domain.service_request.ServiceRequestLocation;
import cm.klg.service_request.domain.service_request.ServiceRequestTitle;
import java.util.UUID;
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
public interface RestMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "userId.value", source = "currentUserId")
  @Mapping(target = "description", source = "serviceRequestRegisterDTO.description")
  @Mapping(target = "title", source = "serviceRequestRegisterDTO.title")
  @Mapping(target = "location", source = "serviceRequestRegisterDTO.location")
  @Mapping(target = "serviceProviderId.value", source = "serviceRequestRegisterDTO.providerId")
  @Mapping(target = "serviceTypeId.value", source = "serviceRequestRegisterDTO.serviceTypeId")
  CreateNewServiceRequestUseCase.Command toCreateNewServiceRequestCommand(
      ServiceRequestRegisterDTO serviceRequestRegisterDTO, UUID currentUserId);

  default @Nullable ServiceRequestDescription toServiceRequestDescription(@Nullable String value) {
    return value == null ? null : ServiceRequestDescription.from(value);
  }

  default @Nullable ServiceRequestTitle toServiceRequestTitle(@Nullable String value) {
    return value == null ? null : ServiceRequestTitle.from(value);
  }

  default @Nullable ServiceRequestLocation toServiceRequestLocation(@Nullable String value) {
    return value == null ? null : ServiceRequestLocation.from(value);
  }
}
