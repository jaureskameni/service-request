package cm.klg.service_request.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ScopesTest {

  @Test
  void shouldExposeServiceProviderScopes() {
    assertThat(Scopes.SERVICE_PROVIDER_APPROVE).isEqualTo("SCOPE_service-provider:approve");
    assertThat(Scopes.SERVICE_PROVIDER_REJECT).isEqualTo("SCOPE_service-provider:reject");
    assertThat(Scopes.SERVICE_PROVIDER_READ_ALL).isEqualTo("SCOPE_service-provider:read:all");
  }
}
