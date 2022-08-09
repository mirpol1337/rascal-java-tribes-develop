package VetulusJava.Tribes.SendGridTest.Helpers;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.IOException;

public class MailTest {
    ObjectForCreateEmail createEmail = new ObjectForCreateEmail();

    @Test
    public void testPersonalizeEmail() throws IOException {
        Mail mail = new Mail();

        Email fromEmail = new Email();
        fromEmail.setName("FromName");
        fromEmail.setEmail("FromName@Email.cz");
        mail.setFrom(fromEmail);

        Email to = new Email();
        to.setName(createEmail.name);
        to.setEmail(createEmail.email);

        Personalization personalization = new Personalization();
        personalization.addTo(to);

        personalization.addHeader("X-Test", "test");
        personalization.addHeader("X-Mock", "true");
        personalization.addDynamicTemplateData("name", createEmail.name);
        personalization.addDynamicTemplateData("userKingdom", createEmail.userKingdom);
        mail.addPersonalization(personalization);
        mail.setTemplateId("d-6f6a2623f01e4f648a0c2f194052bc75");
        mail.addHeader("X-Test1", "1");
        mail.addHeader("X-Test2", "2");

        assertEquals(mail.build(), "{\"from\":{\"name\":\"FromName\",\"email\":\"FromName@Email.cz\"},\"personalizations\":[{\"to\":[{\"name\":\"User\",\"email\":\"User@TestEmail.cz\"}],\"headers\":{\"X-Mock\":\"true\",\"X-Test\":\"test\"},\"dynamic_template_data\":{\"name\":\"User\",\"userKingdom\":\"UserKingdom\"}}],\"template_id\":\"d-6f6a2623f01e4f648a0c2f194052bc75\",\"headers\":{\"X-Test1\":\"1\",\"X-Test2\":\"2\"}}");
    }
}
