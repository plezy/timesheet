package lu.plezy.timesheet.repository;

import lu.plezy.timesheet.entities.ContractTypeEnum;
import lu.plezy.timesheet.entities.ContractualTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ContractualTaskRepository extends JpaRepository<ContractualTask, Long> {
    // parameter passed by ordered
    @Query("select ct from #{#entityName} ct where ct.contract.id = ?#{[0]}")
    List<ContractualTask> findByContractId(Long id);

    // parameter passed by ordered
    @Query("select ct from #{#entityName} ct where ct.contract.id = ?#{[0]} and ct.contract.contractType = ?#{[1]}")
    List<ContractualTask> findByContractIdAndType(Long id, ContractTypeEnum type);


    @Query("select ct from #{#entityName} ct join ct.assignees a where a.id = ?#{[0]} and ct.contract.deleted = false and ct.contract.archived = false" +
            " and ( ct.contract.plannedStart is null or ?#{[1]} >= ct.contract.plannedStart )" +
            " and ( ct.contract.plannedEnd is null or ?#{[1]} <= ct.contract.plannedEnd )" )
    List<ContractualTask> findActiveByUserId(Long id, Date date);
}
