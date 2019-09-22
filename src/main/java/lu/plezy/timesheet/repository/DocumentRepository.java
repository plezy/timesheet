package lu.plezy.timesheet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lu.plezy.timesheet.entities.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
}