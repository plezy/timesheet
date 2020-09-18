package lu.plezy.timesheet.entities;

import java.util.Date;

import javax.persistence.*;

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
    @JoinColumn(name="CREATED_BY", foreignKey = @ForeignKey(name = "FK_DOCUMENT_CREATEDBY_USER"))
    private User createdBy;

    @NonNull
    @Column(name="CREATED_ON", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
}