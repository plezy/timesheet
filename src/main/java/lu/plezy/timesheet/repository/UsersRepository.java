package lu.plezy.timesheet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT u FROM #{#entityName} u " +
            "WHERE (UPPER(u.firstName) LIKE CONCAT('%', UPPER(:filter), '%') OR UPPER(u.lastName) LIKE CONCAT('%', UPPER(:filter), '%')) " +
            "AND u.deleted=false " +
            "AND 'ENTER_TIME_TRACK' MEMBER OF u.roles " +
            "ORDER BY u.firstName, u.lastName")
    List<User> findBillableWithFilter(@Param("filter") String filter);

    @Query("SELECT u FROM #{#entityName} u " +
            "WHERE u.deleted=false " +
            "AND 'ENTER_TIME_TRACK' MEMBER OF u.roles " +
            "ORDER BY u.firstName, u.lastName")
    List<User> findBillable();

}