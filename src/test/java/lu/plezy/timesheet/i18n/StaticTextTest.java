package lu.plezy.timesheet.i18n;

import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Test;

public class StaticTextTest {

    @Test
    public void testDefaultStaticTextGetter() {
        StaticText st = StaticText.getInstance();

        assertNotNull("Can't get StaticText instance", st);

        String result = st.getText("role.enter_time_track");
        System.out.println(result);
    }

    @Test
    public void testDefaultStaticTextGetterNonexistent() {
        StaticText st = StaticText.getInstance();

        assertNotNull("Can't get StaticText instance", st);

        String result = st.getText("non.existent.text");
        System.out.println("Non-existent value : " + result);
    }

    // @Test
    public void testGetLocale() {
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            System.out.println(String.format("Language : %s, Country : %s", locale.getLanguage(), locale.getCountry()));
        }
    }

    @Test
    public void testDefaultResourceBundle() {
        StaticText st = StaticText.getInstance();
        Locale locale = Locale.getDefault();

        ResourceBundle bundle = ResourceBundle.getBundle("i18n/messages", Locale.getDefault());
        for (String key : Collections.list(bundle.getKeys())) {
            System.out.printf("%20s = %s\n", key, st.getText(key));
        }
    }

    @Test
    public void testFrenchResourceBundle() {
        StaticText st = StaticText.getInstance();
        Locale locale = Locale.FRENCH;

        ResourceBundle bundle = ResourceBundle.getBundle("i18n/messages", Locale.getDefault());
        for (String key : Collections.list(bundle.getKeys())) {
            System.out.printf("%20s = %s\n", key, st.getText(key, locale));
        }
    }
}