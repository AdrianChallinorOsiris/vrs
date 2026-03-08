package uk.co.osiris.vrs.users_roles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

	UserAccount findByEmail(String email);

    @Query("""
        SELECT u
        FROM UserAccount u
        LEFT JOIN FETCH u.userRoles ur
        LEFT JOIN FETCH ur.role
        WHERE u.email = :email
    """)
    Optional<UserAccount> findByEmailWithRoles(String email);
}
