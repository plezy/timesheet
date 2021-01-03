package lu.plezy.timesheet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import lu.plezy.timesheet.entities.ProfileTask;

@Repository
public interface ProfileTaskRepository extends JpaRepository<ProfileTask, Long> {
    // parameter passed by ordered
    @Query("select p from #{#entityName} p where p.contract.id = ?#{[0]}")
    List<ProfileTask> findByContractId(Long id);
}