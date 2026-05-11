package uk.co.osiris.vrs.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uk.co.osiris.vrs.security.jwt.JwtAuthFilter;
import uk.co.osiris.vrs.security.jwt.JwtTokenProvider;

@Configuration
@Import({SecurityConfig.class, JwtTokenProvider.class, JwtAuthFilter.class})
class SecurityTestConfig {
}
