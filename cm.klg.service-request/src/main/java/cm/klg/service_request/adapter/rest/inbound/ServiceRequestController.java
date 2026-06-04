package cm.klg.service_request.adapter.rest.inbound;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import cm.klg.common.base.adapter.inbound.rest.WithAuthenticationSupport;
import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.generated.service.request.adapter.rest.inbound.api.ServiceRequestApi;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.CreationResponseDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestPaginateDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestRegisterDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestStatusDTO;
import cm.klg.service_request.application.usecase.CreateNewServiceRequestUseCase;
import cm.klg.service_request.application.usecase.GetAllMyServiceRequestsUseCase;
import cm.klg.service_request.application.usecase.GetAllServiceRequestsByProviderUseCase;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ServiceRequestController implements ServiceRequestApi, WithAuthenticationSupport {
  private final UseCaseExecutor useCaseExecutor;
  private final RestMapper restMapper;
  private final CreateNewServiceRequestUseCase createNewServiceRequestUseCase;
  private final GetAllMyServiceRequestsUseCase getAllMyServiceRequestsUseCase;
  private final GetAllServiceRequestsByProviderUseCase getAllServiceRequestsByProviderUseCase;

  @Override
  public ResponseEntity<CreationResponseDTO> createNewServiceRequest(
      ServiceRequestRegisterDTO serviceRequestRegisterDTO) {
    ServiceRequestId result =
        useCaseExecutor.executeCommand(
            () ->
                createNewServiceRequestUseCase.execute(
                    restMapper.toCreateNewServiceRequestCommand(
                        serviceRequestRegisterDTO, getCurrentUserId())));
    return ResponseEntity.status(CREATED).body(new CreationResponseDTO().newId(result.value()));
  }

  @Override
  public ResponseEntity<ServiceRequestPaginateDTO> getAllMyServiceRequest(
      Integer limit, ServiceRequestStatusDTO status, Integer page) {
    var result =
        useCaseExecutor.executeQuery(
            () ->
                getAllMyServiceRequestsUseCase.execute(
                    restMapper.toGetAllMyServiceRequestsCommand(
                        limit, status, page, getCurrentUserId())));
    return ResponseEntity.status(OK).body(restMapper.toServiceRequestPaginateDTO(result));
  }

  @Override
  public ResponseEntity<ServiceRequestPaginateDTO> getAllServiceRequestByProvider(
      UUID serviceProviderId, Integer limit, ServiceRequestStatusDTO status, Integer page) {
    var result =
        useCaseExecutor.executeQuery(
            () ->
                getAllServiceRequestsByProviderUseCase.execute(
                    restMapper.toGetAllServiceRequestsByProviderCommand(
                        serviceProviderId, limit, status, page)));
    return ResponseEntity.status(OK).body(restMapper.toServiceRequestPaginateDTO(result));
  }

  private UUID getCurrentUserId() {
    return getCurrentUser().getId().map(UUID::fromString).orElseThrow();
  }
}
