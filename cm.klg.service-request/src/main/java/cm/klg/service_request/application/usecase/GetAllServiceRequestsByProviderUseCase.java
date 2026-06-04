package cm.klg.service_request.application.usecase;

import cm.klg.service_request.application.outbound.ServiceRequestRepository;
import cm.klg.service_request.application.views.ServiceRequestViews.ServiceRequestView1;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_request.ServiceRequestStatus;
import cm.klg.service_request.utils.PageData;
import cm.klg.service_request.utils.PaginationFetchRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class GetAllServiceRequestsByProviderUseCase {
  private final ServiceRequestRepository serviceRequestRepository;

  public Response execute(Command command) {

    var pagination = new PaginationFetchRequest(command.limit(), command.page());

    if (command.status() == null) {
      return toResponse(
          serviceRequestRepository.loadAllRequestsByProviderAsView1(
              command.providerId, pagination));
    }
    return toResponse(
        serviceRequestRepository.loadAllRequestByProviderAndStatusAsView1(
            command.providerId, command.status(), pagination));
  }

  private static Response toResponse(PageData<ServiceRequestView1> pageData) {
    return new Response(new ArrayList<>(pageData.elements()), pageData.total());
  }

  public record Command(
      ServiceProviderId providerId,
      @Nullable ServiceRequestStatus status,
      Integer limit,
      Integer page) {}

  public record Response(List<ServiceRequestView1> serviceRequestView1s, long count) {}
}
