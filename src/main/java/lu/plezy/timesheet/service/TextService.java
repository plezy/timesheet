package lu.plezy.timesheet.service;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lu.plezy.timesheet.i18n.StaticText;

@Service
@Cacheable("i18nCache")
public class TextService {
    
    private static Logger log = LoggerFactory.getLogger(TextService.class);

    public String getText(String textId, String lang) {
        log.debug("getText called for {} and lang {}", textId, lang);
        String text = StaticText.getInstance().getText(textId, Locale.forLanguageTag(lang));
        log.debug("found : {}", text);
        return text;
    }
}