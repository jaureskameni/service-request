package cm.klg.service_request.application.usecase;

import cm.klg.service_request.application.outbound.ServiceRequestRepository;
import cm.klg.service_request.application.views.ServiceRequestViews.ServiceRequestView1;
import cm.klg.service_request.domain.service_request.ServiceRequestStatus;
import cm.klg.service_request.domain.user.UserId;
import cm.klg.service_request.utils.PageData;
import cm.klg.service_request.utils.PaginationFetchRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class GetAllMyServiceRequestsUseCase {
  private final ServiceRequestRepository serviceRequestRepository;

  public Response execute(Command command) {
    var pagination = new PaginationFetchRequest(command.limit(), command.page());

    if (command.status() == null) {
      return toResponse(
          serviceRequestRepository.loadAllMyRequestsAsView1(command.userId, pagination));
    }
    return toResponse(
        serviceRequestRepository.loadAllMyRequestByStatusAsView1(
            command.userId, command.status(), pagination));
  }

  private static Response toResponse(PageData<? extends ServiceRequestView1> pageData) {
    return new Response(new ArrayList<>(pageData.elements()), pageData.total());
  }

  public record Command(
      UserId userId, @Nullable ServiceRequestStatus status, Integer limit, Integer page) {}

  public record Response(List<ServiceRequestView1> serviceRequestView1s, long count) {}
}
