package lu.plezy.timesheet.controller;

import java.io.IOException;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lu.plezy.timesheet.entities.messages.StringResponse;
import lu.plezy.tools.json.JsonPropertiesSerializer;

@RestController
@RequestMapping(path = "/appinfos")
public class ApplicationInfosController {

    private static Logger log = LoggerFactory.getLogger(ApplicationInfosController.class);

    @Value("${build.version}")
    private String buildVersion;
    @Value("${build.timestamp}")
    private String buildTimestamp;

    @Value("${build.git.infos.file}")
    private String gitInfosFileName;

    String jsonGitInfos = null;
    String jsonSysProperties = null;
    String jsonEnvVariables = null;

    @RequestMapping(value = "/version", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('MANAGE_SETTINGS')")
    public StringResponse getVersion() {
        log.info("getVersion called");
        return new StringResponse(buildVersion);
    }

    @RequestMapping(value = "/timestamp", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('MANAGE_SETTINGS')")
    public StringResponse getTimestamp() {
        log.info("getTimestamp called");
        return new StringResponse(buildTimestamp);
    }

    @RequestMapping(value = "/git", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('MANAGE_SETTINGS')")
    public String getGitInfos() throws IOException {
        log.info("getGitInfos called");
        if (jsonGitInfos == null) {
            Resource resource = new ClassPathResource(gitInfosFileName);
            Properties gitProps = new Properties();
            gitProps.load(resource.getInputStream());
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule(JsonPropertiesSerializer.class.getName());
            module.addSerializer(Properties.class, new JsonPropertiesSerializer());
            mapper.registerModule(module);
            jsonGitInfos = mapper.writeValueAsString(gitProps);
        }
        return jsonGitInfos;
    }

    @RequestMapping(value = "/properties", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('MANAGE_SETTINGS')")
    public String getRuntimeInfos() throws JsonProcessingException {
        log.info("getRuntimeInfos called");
        if (jsonSysProperties == null) {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule(JsonPropertiesSerializer.class.getName());
            module.addSerializer(Properties.class, new JsonPropertiesSerializer());
            mapper.registerModule(module);
            jsonSysProperties = mapper.writeValueAsString(System.getProperties());
        }
        return jsonSysProperties;
    }

    @RequestMapping(value = "/env", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('MANAGE_SETTINGS')")
    public String getEnvironmentVariables() throws JsonProcessingException {
        log.info("getEnvironmentVariables called");
        if (jsonEnvVariables == null) {
            ObjectMapper mapper = new ObjectMapper();
            jsonEnvVariables = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(System.getenv());
        }
        return jsonEnvVariables;
    }
}