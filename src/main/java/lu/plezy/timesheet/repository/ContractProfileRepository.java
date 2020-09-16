package lu.plezy.timesheet.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import lu.plezy.timesheet.entities.ContractProfile;

@Repository
public interface ContractProfileRepository extends JpaRepository<ContractProfile, Long> {
    Optional<ContractProfile> findById(long Id);

    // parameter passed by ordered
    @Query("select p from #{#entityName} p where p.contract.id = ?#{[0]}")
    List<ContractProfile> findByContractId(Long id);

    @Query("select p from #{#entityName} p join p.assignees a where a.id = ?#{[0]} and p.contract.deleted = false and p.contract.archived = false" +
            " and ( p.contract.plannedStart is null or ?#{[1]} >= p.contract.plannedStart )" +
            " and ( p.contract.plannedEnd is null or ?#{[1]} <= p.contract.plannedEnd )" )
    List<ContractProfile> findActiveByUserId(Long id, Date date);

}