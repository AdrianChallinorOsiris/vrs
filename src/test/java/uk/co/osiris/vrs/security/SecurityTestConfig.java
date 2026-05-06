package uk.co.osiris.vrs.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SecurityConfig.class)
class SecurityTestConfig {
}
