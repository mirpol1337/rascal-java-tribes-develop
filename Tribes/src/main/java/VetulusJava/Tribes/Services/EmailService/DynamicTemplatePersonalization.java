package VetulusJava.Tribes.Services.EmailService;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sendgrid.helpers.mail.objects.Personalization;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DynamicTemplatePersonalization extends Personalization {
    @JsonProperty(value = "dynamic_template_data")
    private Map<String, Object> dynamicTemplateData;

    @JsonProperty("dynamic_template_data")
    public Map<String, Object> getDynamicTemplateData() {
        if (dynamicTemplateData == null) {
            return Collections.<String, Object>emptyMap();
        }
        return dynamicTemplateData;
    }

    public void addDynamicTemplateData(String key, Object value) {
        if (dynamicTemplateData == null) {
            dynamicTemplateData = new HashMap<String, Object>();
            dynamicTemplateData.put(key, value);
        } else {
            dynamicTemplateData.put(key, value);
        }
    }
}
