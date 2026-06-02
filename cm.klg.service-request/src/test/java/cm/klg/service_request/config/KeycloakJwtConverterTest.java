package cm.klg.service_request.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@ExtendWith(MockitoExtension.class)
class KeycloakJwtConverterTest {

  @Mock private JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;

  @Test
  void shouldConvertJwtToAuthenticationTokenWithSubjectAsPrincipal() {
    Jwt jwt =
        Jwt.withTokenValue("token")
            .header("alg", "none")
            .subject("user-123")
            .claim("scope", "service-provider:read:all")
            .build();
    List<GrantedAuthority> authorities =
        List.of(new SimpleGrantedAuthority(Scopes.SERVICE_PROVIDER_READ_ALL));
    when(jwtGrantedAuthoritiesConverter.convert(jwt)).thenReturn(authorities);

    var authentication = new KeycloakJwtConverter(jwtGrantedAuthoritiesConverter).convert(jwt);

    assertThat(authentication).isInstanceOf(JwtAuthenticationToken.class);
    assertThat(authentication.getName()).isEqualTo("user-123");
    assertThat(authentication.getAuthorities()).containsExactlyElementsOf(authorities);
    assertThat(((JwtAuthenticationToken) authentication).getToken()).isEqualTo(jwt);
  }
}
