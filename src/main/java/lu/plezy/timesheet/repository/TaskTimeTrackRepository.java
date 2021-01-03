package lu.plezy.timesheet.repository;

import lu.plezy.timesheet.entities.TaskTimeTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskTimeTrackRepository extends JpaRepository<TaskTimeTrack, Long> {
    List<TaskTimeTrack> findByUserIdAndDate(long userId, Date date);

    //@Query("select ttt from #{#entityName} ttt where ttt.user.id = :userId and ttt.date = :searchedDate")
    //List<TaskTimeTrack> findForUserIdAndDate(long userId, Date searchedDate);
}
