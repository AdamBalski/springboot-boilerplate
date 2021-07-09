package pl.adambalski.springbootboilerplate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.adambalski.springbootboilerplate.model.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Performs SQL queries on database and is called by {@link pl.adambalski.springbootboilerplate.service.AdminService}.<br><br>
 *
 * @author Adam Balski
 * @see pl.adambalski.springbootboilerplate.service.AdminService
 * @see JpaRepository
 * @see User
 */
@Transactional(readOnly = true)
@Repository
public interface AdminRepository extends JpaRepository<User, UUID> {
    Optional<User> findByLogin(String login);

    Optional<User> findByUuid(UUID uuid);

    @Modifying
    int deleteByLogin(String login);
}
