package lu.plezy.timesheet.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

// import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
@Table(name="DOCUMENTS")
@SequenceGenerator(name = "DOCUMENTS_SEQ", initialValue = 100, allocationSize = 1)
public class Document {
    @Id
    // @GeneratedValue(generator = "uuid")
    // @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENTS_SEQ")
    @Column(name = "ID", updatable = false, nullable = false)
    private String id;

    @NonNull
    @Column(name="FILE_NAME")
    private String fileName;

    @NonNull
    @Column(name="FILE_TYPE")
    private String fileType;

    @NonNull
    @Lob
    @Column(name="DATA")
    private byte[] data;

    @NonNull
    @ManyToOne(targetEntity=User.class)
    @JoinColumn(name="CREATED_BY")
    private User createdBy;

    @NonNull
    @Column(name="CREATED_ON", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
}