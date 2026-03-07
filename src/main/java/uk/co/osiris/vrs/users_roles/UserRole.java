package uk.co.osiris.vrs.users_roles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_role", schema = "vrs")
@IdClass(UserRoleId.class)
@Getter
@Setter
public class UserRole {

	@Id
	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Id
	@Column(name = "role_id", nullable = false)
	private Long roleId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	@Setter(lombok.AccessLevel.NONE)
	private UserAccount user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", insertable = false, updatable = false)
	@Setter(lombok.AccessLevel.NONE)
	private Role role;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	public UserRole() {
	}

	public UserRole(UserAccount user, Role role) {
		this.user = user;
		this.role = role;
		this.userId = user.getId();
		this.roleId = role.getId();
	}

	public void setUser(UserAccount user) {
		this.user = user;
		this.userId = user != null ? user.getId() : null;
	}

	public void setRole(Role role) {
		this.role = role;
		this.roleId = role != null ? role.getId() : null;
	}

	@PrePersist
	protected void onCreate() {
		LocalDateTime now = LocalDateTime.now();
		if (createdAt == null) {
			createdAt = now;
		}
		if (updatedAt == null) {
			updatedAt = now;
		}
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UserRole other)) return false;
		return userId != null && roleId != null && userId.equals(other.userId) && roleId.equals(other.roleId);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
