package lu.plezy.timesheet.repository;

import lu.plezy.timesheet.entities.ContractualTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractualTaskRepository extends JpaRepository<ContractualTask, Long> {

}
