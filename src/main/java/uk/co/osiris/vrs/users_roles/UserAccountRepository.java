package uk.co.osiris.vrs.users_roles;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

	UserAccount findByEmail(String email);
}
