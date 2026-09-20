package cm.klg.service_request.adapter.messaging.inbound;

import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamUserCreatedEventDTO;
import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamUserUpdatedEventDTO;
import cm.klg.service_request.application.usecase.ApproveServiceProviderUseCase;
import cm.klg.service_request.application.usecase.CreateNewUserUseCase;
import cm.klg.service_request.application.usecase.CreateServiceProviderUseCase;
import cm.klg.service_request.application.usecase.RejectServiceProviderUseCase;
import cm.klg.service_request.application.usecase.UpdateUserUseCase;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.openapitools.model.ServiceProviderServiceProviderApprovedEventDTO;
import org.openapitools.model.ServiceProviderServiceProviderCreatedEventDTO;
import org.openapitools.model.ServiceProviderServiceProviderRejectedEventDTO;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessagingInboundMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id")
  @Mapping(target = "firstname", source = "firstname")
  @Mapping(target = "lastname", source = "lastname")
  @Mapping(target = "countryCode", source = "phoneNumber.countryCode")
  @Mapping(target = "phoneNumber", source = "phoneNumber.number")
  @Mapping(target = "email", source = "email")
  @Mapping(target = "createdAt", source = "createdAt")
  CreateNewUserUseCase.CreateNewUserCommand toCreateUserCommand(
      UamUserCreatedEventDTO userCreatedEventDTO);

  @Mapping(target = "userId.value", source = "id")
  @Mapping(target = "lastname.value", source = "lastname")
  @Mapping(target = "firstname.value", source = "firstname")
  @Mapping(target = "email.value", source = "email")
  @Mapping(target = "phoneNumber.countryCode", source = "phoneNumber.countryCode")
  @Mapping(target = "phoneNumber.number", source = "phoneNumber.number")
  UpdateUserUseCase.UpdateUserCommand toUpdateUserCommand(
      UamUserUpdatedEventDTO userUpdatedEventDTO);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "userId.value", source = "userId")
  @Mapping(target = "serviceProviderId.value", source = "serviceProviderId")
  @Mapping(target = "approvedAt.value", source = "approvedAt")
  ApproveServiceProviderUseCase.ApproveServiceProviderCommand toApproveServiceProviderCommand(
      ServiceProviderServiceProviderApprovedEventDTO providerApprovedEventDTO);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "userId.value", source = "userId")
  @Mapping(target = "serviceProviderId.value", source = "serviceProviderId")
  CreateServiceProviderUseCase.Command toCreateServiceProviderCommand(
      ServiceProviderServiceProviderCreatedEventDTO providerCreatedEventDTO);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "userId.value", source = "userId")
  @Mapping(target = "serviceProviderId.value", source = "serviceProviderId")
  RejectServiceProviderUseCase.Command toRejectServiceProviderCommand(
      ServiceProviderServiceProviderRejectedEventDTO providerRejectedEventDTO);
}
