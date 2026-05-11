package uk.co.osiris.vrs.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.osiris.vrs.security.jwt.JwtTokenProvider;
import uk.co.osiris.vrs.vessel.Vessel;
import uk.co.osiris.vrs.vessel.VesselRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final VesselRepository vesselRepository;

    public String loginUser(String email, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        log.info("User {} authenticated with roles {}", email, roles);
        return jwtTokenProvider.generateUserToken(email, roles);
    }

    @Transactional(readOnly = true)
    public String loginVessel(String accessToken) {
        Vessel vessel = vesselRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid vessel access token"));
        if (!vessel.isActive()) {
            throw new IllegalArgumentException("Vessel is not active");
        }
        log.info("Vessel {} authenticated via access token", vessel.getId());
        return jwtTokenProvider.generateVesselToken(vessel.getId(), accessToken);
    }
}
