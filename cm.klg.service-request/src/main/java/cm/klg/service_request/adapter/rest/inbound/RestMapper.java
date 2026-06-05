package cm.klg.service_request.adapter.rest.inbound;

import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestPaginateDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestRegisterDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestRejectDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestStatusDTO;
import cm.klg.service_request.application.usecase.AcceptServiceRequestUseCase;
import cm.klg.service_request.application.usecase.CancelServiceRequestUseCase;
import cm.klg.service_request.application.usecase.CreateNewServiceRequestUseCase;
import cm.klg.service_request.application.usecase.GetAllMyServiceRequestsUseCase;
import cm.klg.service_request.application.usecase.GetAllServiceRequestsByProviderUseCase;
import cm.klg.service_request.application.usecase.RejectServiceRequestUseCase;
import cm.klg.service_request.application.views.ServiceRequestViews;
import cm.klg.service_request.domain.service_request.ServiceRequestDescription;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.domain.service_request.ServiceRequestLocation;
import cm.klg.service_request.domain.service_request.ServiceRequestReason;
import cm.klg.service_request.domain.service_request.ServiceRequestStatus;
import cm.klg.service_request.domain.service_request.ServiceRequestTitle;
import cm.klg.service_request.domain.user.UserId;
import java.util.Optional;
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

  default @Nullable ServiceRequestStatus toServiceRequestStatus(
      @Nullable ServiceRequestStatusDTO status) {
    return status == null ? null : ServiceRequestStatus.valueOf(status.name());
  }

  default GetAllMyServiceRequestsUseCase.Command toGetAllMyServiceRequestsCommand(
      Integer limit, @Nullable ServiceRequestStatusDTO status, Integer page, UUID userId) {
    return new GetAllMyServiceRequestsUseCase.Command(
        new UserId(userId),
        Optional.ofNullable(status)
            .map(
                serviceRequestStatusDTO ->
                    ServiceRequestStatus.valueOf(serviceRequestStatusDTO.name()))
            .orElse(null),
        Optional.ofNullable(limit).orElse(10),
        Optional.ofNullable(page).orElse(0));
  }

  default GetAllServiceRequestsByProviderUseCase.Command toGetAllServiceRequestsByProviderCommand(
      Integer limit, @Nullable ServiceRequestStatusDTO status, Integer page, UUID userId) {
    return new GetAllServiceRequestsByProviderUseCase.Command(
        UserId.from(userId),
        Optional.ofNullable(status)
            .map(
                serviceRequestStatusDTO ->
                    ServiceRequestStatus.valueOf(serviceRequestStatusDTO.name()))
            .orElse(null),
        Optional.ofNullable(limit).orElse(10),
        Optional.ofNullable(page).orElse(0));
  }

  default ServiceRequestPaginateDTO toServiceRequestPaginateDTO(
      GetAllMyServiceRequestsUseCase.Response pageData) {
    return new ServiceRequestPaginateDTO()
        .count(pageData.count())
        .serviceRequest(
            pageData.serviceRequestView1s().stream().map(this::toServiceRequestDTO).toList());
  }

  default ServiceRequestPaginateDTO toServiceRequestPaginateDTO(
      GetAllServiceRequestsByProviderUseCase.Response pageData) {
    return new ServiceRequestPaginateDTO()
        .count(pageData.count())
        .serviceRequest(
            pageData.serviceRequestView1s().stream().map(this::toServiceRequestDTO).toList());
  }

  default ServiceRequestDTO toServiceRequestDTO(
      ServiceRequestViews.ServiceRequestView1 serviceRequestView1) {

    return new ServiceRequestDTO()
        .id(serviceRequestView1.getId())
        .userId(serviceRequestView1.getUserId())
        .providerId(serviceRequestView1.getServiceProviderId())
        .serviceTypeId(serviceRequestView1.getServiceTypeId())
        .status(ServiceRequestStatusDTO.valueOf(serviceRequestView1.getStatus()))
        .title(serviceRequestView1.getTitle())
        .description(serviceRequestView1.getDescription())
        .location(serviceRequestView1.getLocation())
        .reason(serviceRequestView1.getReason())
        .createdAt(serviceRequestView1.getCreatedAt())
        .updatedAt(serviceRequestView1.getUpdatedAt());
  }

  default AcceptServiceRequestUseCase.Command toAcceptServiceRequestCommand(
      UUID serviceRequestId, UUID userId) {
    return new AcceptServiceRequestUseCase.Command(
        UserId.from(userId), ServiceRequestId.from(serviceRequestId));
  }

  default RejectServiceRequestUseCase.Command toRejectServiceRequestCommand(
      UUID serviceRequestId, UUID userId, ServiceRequestRejectDTO serviceRequestRejectDTO) {
    return new RejectServiceRequestUseCase.Command(
        UserId.from(userId),
        ServiceRequestId.from(serviceRequestId),
        ServiceRequestReason.from(serviceRequestRejectDTO.getReason()));
  }

  default CancelServiceRequestUseCase.Command toCancelServiceRequestCommand(
      UUID serviceRequestId, UUID userId) {
    return new CancelServiceRequestUseCase.Command(
        UserId.from(userId), ServiceRequestId.from(serviceRequestId));
  }
}
