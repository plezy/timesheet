package lu.plezy.timesheet.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lu.plezy.timesheet.entities.messages.StringResponse;
import lu.plezy.timesheet.service.TextService;

@RestController
@RequestMapping("/i18n")
public class TextController {

    private static Logger log = LoggerFactory.getLogger(TextController.class);

    @Autowired
    TextService textService;

    private static final String defaultLanguage = "en";
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping({"/{text}", "/{text}/{lang}"})
    public StringResponse getText(@PathVariable("text") String text, @PathVariable("lang") Optional<String> lang) {
        log.debug("getText called");
        String uiText = "ui." + text;
        String language = defaultLanguage;
        if (lang.isPresent())
            language = lang.get();
        return new StringResponse(textService.getText(uiText, language));
    }

}