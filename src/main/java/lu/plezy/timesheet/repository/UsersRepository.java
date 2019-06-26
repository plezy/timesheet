package lu.plezy.timesheet.repository;

//import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import lu.plezy.timesheet.entities.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Page<User> findAll(Pageable p);

    @Query("select u from #{#entityName} u where u.deleted=false")
    Page<User> findAllActive(Pageable p);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}