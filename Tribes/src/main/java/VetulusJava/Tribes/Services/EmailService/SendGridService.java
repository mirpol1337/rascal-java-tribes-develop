package VetulusJava.Tribes.Services.EmailService;

import VetulusJava.Tribes.DTOs.CreateUserDto;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendGridService implements ISendGridService {
    @Value("${SENDGRID.APIKEY}")
    private String Apikey;
    @Value("${SENDGRID.TEMPLATEID}")
    private String TempleteId;
    @Value("${SENDGRID.FROMNAME}")
    private String fromName;
    @Value("${SENDGRID.FROMEMAIL}")
    private String from;

    private Mail PersonalizeEmail(CreateUserDto createUserDto) {
        Mail mail = new Mail();
        /*
         * Personalization setting,  add recipient
         */
        Email fromEmail = new Email();
        fromEmail.setName(fromName);
        fromEmail.setEmail(from);
        mail.setFrom(fromEmail);

        Personalization personalization = new Personalization();
        Email to = new Email();
        to.setName(createUserDto.getName());
        to.setEmail(createUserDto.getEmail());
        personalization.addTo(to);

        //personalization.addHeader("X-Test", "test");
        //personalization.addHeader("X-Mock", "true");

        /* Substitution value settings */
        personalization.addDynamicTemplateData("UserName", createUserDto.getName());
        personalization.addDynamicTemplateData("UserKingdom", createUserDto.getUserKingdom());

        mail.addPersonalization(personalization);
        /* Set template id */
        mail.setTemplateId(TempleteId);
        return mail;
    }

    @Override
    public String sendMail(CreateUserDto createUserDto) {
        SendGrid sg = new SendGrid(Apikey);
        //sg.addRequestHeader("X-Mock", "true");
        Request request = new Request();
        Mail mail = PersonalizeEmail(createUserDto);
        try {
            request.setMethod(Method.POST);
            //Set endpoint = "mail/send" to send email. This endpoint allows us to delivery email over SendGrid’s v3 Web API.
            request.setEndpoint("mail/send");
            // Set body of the Request instance with the help of build method of the Mail.
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
            if (response.getStatusCode()==202) {
                return "Email is sent Successfully!!";
            }
            else
                return String.valueOf(response.getStatusCode()) + response.getBody().toString();
        } catch (IOException ex) {
            ex.printStackTrace();
            return "Failed to send mail! " + ex.getMessage();
        }
    }
}
