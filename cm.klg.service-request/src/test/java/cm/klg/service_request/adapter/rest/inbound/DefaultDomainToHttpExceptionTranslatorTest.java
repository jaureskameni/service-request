package cm.klg.service_request.adapter.rest.inbound;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.common.base.exception.InternalException;
import cm.klg.common.base.exception.ResourceNotFoundException;
import cm.klg.service_request.domain.service_provider.ServiceProviderNotFoundException;
import cm.klg.service_request.domain.user.UserNotFoundException;
import org.junit.jupiter.api.Test;

class DefaultDomainToHttpExceptionTranslatorTest {

  private final DefaultDomainToHttpExceptionTranslator translator =
      new DefaultDomainToHttpExceptionTranslator();

  @Test
  void shouldTranslateUserNotFoundToResourceNotFound() {
    assertThat(translator.translate(new UserNotFoundException()))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("SERVICE_REQUEST_404_001");
  }

  @Test
  void shouldTranslateServiceProviderNotFoundToResourceNotFound() {
    assertThat(translator.translate(new ServiceProviderNotFoundException()))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("SERVICE_REQUEST_404_002");
  }

  @Test
  void shouldTranslateUnknownExceptionToInternalException() {
    RuntimeException exception = new RuntimeException("boom");

    assertThat(translator.translate(exception))
        .isInstanceOf(InternalException.class)
        .hasMessage("boom")
        .hasCause(exception);
  }

  @Test
  void shouldUseFallbackMessageWhenExceptionMessageIsNull() {
    RuntimeException exception = new RuntimeException();

    assertThat(translator.translate(exception))
        .isInstanceOf(InternalException.class)
        .hasMessage("missing error code");
  }
}
