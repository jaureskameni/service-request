package cm.klg.service_request.adapter.rest.inbound;

import static org.springframework.http.HttpStatus.CREATED;

import cm.klg.common.base.adapter.inbound.rest.WithAuthenticationSupport;
import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.generated.service.request.adapter.rest.inbound.api.ServiceRequestApi;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.CreationResponseDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestRegisterDTO;
import cm.klg.service_request.application.usecase.CreateNewServiceRequestUseCase;
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

  private UUID getCurrentUserId() {
    return getCurrentUser().getId().map(UUID::fromString).orElseThrow();
  }
}
