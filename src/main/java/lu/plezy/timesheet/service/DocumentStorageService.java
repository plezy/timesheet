package lu.plezy.timesheet.service;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lu.plezy.timesheet.entities.Document;
import lu.plezy.timesheet.entities.User;
import lu.plezy.timesheet.exception.FileStorageException;
import lu.plezy.timesheet.i18n.StaticText;
import lu.plezy.timesheet.repository.DocumentRepository;

@Service
public class DocumentStorageService {
    
    private static Logger log = LoggerFactory.getLogger(DocumentStorageService.class);
 
    @Autowired
    private DocumentRepository documentRepository;

    @Value("${timesheet.storage.temp}")
    public String tempFolder;

    DocumentStorageService() {
        log.info("Temp folder : " + tempFolder);
    }

    public Document storeDocument(MultipartFile file, User user) throws FileStorageException {
        Document result = null;
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new FileStorageException(StaticText.getInstance().getText("exception.invalid.path"));
            }

            Document doc = new Document(fileName, file.getContentType(), file.getBytes(), user, new Date());
        
            result = documentRepository.save(doc);

        } catch(IOException ioe) {
            throw new FileStorageException(ioe);
        }
        return result;
    }

    public Document getDocument(long documentId) throws FileStorageException {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new FileStorageException(StaticText.getInstance().getText("exception.filenotfound.with.id")));
    }
}