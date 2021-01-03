package lu.plezy.timesheet.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticText {
    private final static Logger log = LoggerFactory.getLogger(StaticText.class);

    private final static StaticText _instance = new StaticText();

    private static HashMap<String, ResourceBundle> langBundlePool = new HashMap<>();

    public static StaticText getInstance() {
        return _instance;
    }

    public String getText(String in) {       
        return getText(in, Locale.getDefault());
    }

    public String getText(String in, Locale locale) {
        ResourceBundle bundle = null;
        if ( ! langBundlePool.containsKey(locale.getLanguage())) {
            bundle = ResourceBundle.getBundle("i18n/messages", locale);
        } else {
            bundle = langBundlePool.get(locale.getLanguage());
        }
        if (bundle !=null) {
            String str = null;
            try {
                str = bundle.getString(in);
            } catch (MissingResourceException mrex) {
                log.error("MissingResourceException occurs");
                mrex.printStackTrace();
                return getText("error.non.existent.resource", locale);
            }
            return str;

        } else {
            return null;
        }
    }
}