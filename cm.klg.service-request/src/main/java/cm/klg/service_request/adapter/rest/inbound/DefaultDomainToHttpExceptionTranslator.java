package cm.klg.service_request.adapter.rest.inbound;

import cm.klg.common.base.exception.ConflictException;
import cm.klg.common.base.exception.ForbiddenException;
import cm.klg.common.base.exception.HttpErrorException;
import cm.klg.common.base.exception.InternalException;
import cm.klg.common.base.exception.ResourceNotFoundException;
import cm.klg.common.base.transaction.DomainToHttpExceptionTranslator;
import cm.klg.service_request.domain.service_provider.ServiceProviderNotFoundException;
import cm.klg.service_request.domain.service_provider.UnauthorizedProviderException;
import cm.klg.service_request.domain.service_request.InvalidServiceRequestStatusException;
import cm.klg.service_request.domain.service_request.RequestDoesNotBelongToProviderException;
import cm.klg.service_request.domain.service_request.RequestDoesNotBelongToUserException;
import cm.klg.service_request.domain.service_request.ServiceRequestNotFoundException;
import cm.klg.service_request.domain.user.UserNotFoundException;
import java.util.Optional;

public record DefaultDomainToHttpExceptionTranslator() implements DomainToHttpExceptionTranslator {
  @Override
  public HttpErrorException translate(RuntimeException ex) {
    var message = Optional.ofNullable(ex.getMessage()).orElse("missing error code");
    return switch (ex) {
      case RequestDoesNotBelongToProviderException _,
          RequestDoesNotBelongToUserException _,
          UnauthorizedProviderException _ ->
          new ForbiddenException(message);
      case UserNotFoundException _,
          ServiceProviderNotFoundException _,
          ServiceRequestNotFoundException _ ->
          new ResourceNotFoundException(message);
      case InvalidServiceRequestStatusException _ -> new ConflictException(message);
      default -> new InternalException(message, ex);
    };
  }
}
