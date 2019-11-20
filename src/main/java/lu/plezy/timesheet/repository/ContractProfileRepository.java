package lu.plezy.timesheet.repository;

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

}