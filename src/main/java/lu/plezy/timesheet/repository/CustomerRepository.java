package lu.plezy.timesheet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import lu.plezy.timesheet.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findById(long Id);

    Page<Customer> findAll(Pageable p);

    @Query("select c from #{#entityName} c where c.deleted=false and c.archived=false")
    List<Customer> findAllActive();

    @Query("select c from #{#entityName} c where c.deleted=false and c.archived=false")
    Page<Customer> findAllActive(Pageable p);

    @Query("select c from #{#entityName} c where c.archived=true")
    Page<Customer> findAllArchived(Pageable p);

    @Query("select c from #{#entityName} c where c.archived=false")
    Page<Customer> findAllNotArchived(Pageable p);

    List<Customer> findFirst10ByDeletedFalseAndArchivedFalse();

    List<Customer> findFirst10ByDeletedFalseAndArchivedFalseAndNameContainsIgnoringCase(String filter);
}