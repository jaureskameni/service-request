package cm.klg.service_request.config;

import static cm.klg.service_request.utils.Constants.REGEX_UUID_WITH_DELIMITER;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final KeycloakJwtConverter keycloakJwtConverter;

  @Bean
  public SecurityFilterChain protectedEndpoints(HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(HttpMethod.POST, "/service-request")
                    .authenticated()
                    .requestMatchers(
                        HttpMethod.GET,
                        "/service-request/{serviceRequestId:%s}"
                            .formatted(REGEX_UUID_WITH_DELIMITER))
                    .authenticated()
                    .requestMatchers(
                        HttpMethod.PUT,
                        "/service-request/{serviceRequestId:%s}/accept"
                            .formatted(REGEX_UUID_WITH_DELIMITER))
                    .authenticated()
                    .requestMatchers(
                        HttpMethod.PUT,
                        "/service-request/{serviceRequestId:%s}/reject"
                            .formatted(REGEX_UUID_WITH_DELIMITER))
                    .authenticated()
                    .requestMatchers(
                        HttpMethod.PUT,
                        "/service-request/{serviceRequestId:%s}/cancel"
                            .formatted(REGEX_UUID_WITH_DELIMITER))
                    .authenticated()
                    .requestMatchers(
                        HttpMethod.GET,
                        "/service-request/{serviceRequestId:%s}/follow-up"
                            .formatted(REGEX_UUID_WITH_DELIMITER))
                    .authenticated()
                    .requestMatchers(
                        HttpMethod.PUT,
                        "/service-request/{serviceRequestId:%s}/follow-up/agreement"
                            .formatted(REGEX_UUID_WITH_DELIMITER))
                    .authenticated()
                    .requestMatchers(
                        HttpMethod.PUT,
                        "/service-request/{serviceRequestId:%s}/follow-up/service-completion"
                            .formatted(REGEX_UUID_WITH_DELIMITER))
                    .authenticated()
                    .requestMatchers(
                        HttpMethod.POST,
                        "/service-request/{serviceRequestId:%s}/review"
                            .formatted(REGEX_UUID_WITH_DELIMITER))
                    .authenticated()
                    .requestMatchers(
                        HttpMethod.GET,
                        "/service-provider/{providerId:%s}/reviews"
                            .formatted(REGEX_UUID_WITH_DELIMITER))
                    .permitAll()
                    .requestMatchers(
                        HttpMethod.GET,
                        "/service-provider/{providerId:%s}/reviews/summary"
                            .formatted(REGEX_UUID_WITH_DELIMITER))
                    .permitAll()
                    .requestMatchers(
                        HttpMethod.PUT,
                        "/reviews/{reviewId:%s}/moderation".formatted(REGEX_UUID_WITH_DELIMITER))
                    .hasRole("review-moderate")
                    .requestMatchers(HttpMethod.GET, "/my/service-request")
                    .authenticated()
                    .requestMatchers(HttpMethod.GET, "/my/provider/service-requests")
                    .authenticated()
                    .anyRequest()
                    .denyAll())
        .oauth2ResourceServer(
            oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakJwtConverter)))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
  }
}
