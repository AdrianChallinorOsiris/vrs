package uk.co.osiris.vrs.security;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.osiris.vrs.users_roles.UserAccount;
import uk.co.osiris.vrs.users_roles.UserAccountRepository;
import uk.co.osiris.vrs.users_roles.UserRole;

/**
 * Checks that the person logging in is permitted
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SessionUserDetails implements UserDetailsService {
    @NonNull private UserAccountRepository userAccountRepository; 

  	/**
	 * Load user by username.
	 * Spring Boot provides the name of the a user who is logging in. This
	 * method validates that the user is in the database and that they are active. 
	 * It then creates a User object with the username (ie the email address), the 
	 * users password (which is already stored hashed), and their roles.  
	 * 
	 *
	 * @param userName the user name
	 * @return the user details
	 * @throws UsernameNotFoundException the username not found exception
	 */
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UserAccount userAccount = userAccountRepository.findByEmail(userName);

		if (userAccount == null) {
			log.warn("User not found! {}", userName);
			throw new UsernameNotFoundException("user " + userName + " not found");	
		}
		if (!userAccount.isActive()) {
			log.warn("User is inactive! {}", userName);
			throw new UsernameNotFoundException("user " + userName + " not found!");	
		}
		
		// Get roles 
		String[] userRoles = 
        userAccount.getUserRoles().stream().map(UserRole::getRole).toArray(String[]::new);
		Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);
		log.info("User {} granted access with roles {} ", userAccount.getEmail(), authorities);

		User user = new User(userAccount.getEmail(), userAccount.getPasswordHash(), authorities);
		return user;
	}  

}
