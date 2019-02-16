package lu.plezy.timesheet.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {
    @Autowired
    private Environment env;

    @Override
    public void run(String... args) throws Exception {
        String[] defaultProfiles = env.getDefaultProfiles();
        System.out.println("Default profiles :");
        for (String str : defaultProfiles) {
            System.out.println(" |--> " + str);
        }

        String[] activeProfiles = env.getActiveProfiles();
        System.out.println("Active profiles  :");
        for (String str : activeProfiles) {
            System.out.println(" |--> " + str);
        }

        Map<String, Object> map = new HashMap<>();
        for (Iterator<PropertySource<?>> it = ((AbstractEnvironment) env).getPropertySources().iterator(); it
                .hasNext();) {
            PropertySource<?> propertySource = it.next();
            if (propertySource instanceof MapPropertySource) {
                map.putAll(((MapPropertySource) propertySource).getSource());
            }
        }

        System.out.println("Properties   :");
        List<String> keys = map.keySet().stream().collect(Collectors.toList());
        Collections.sort(keys);
        for (String key : keys) {
            System.out.printf("%-30s = %s\n", key, map.get(key));
        }
    }

}