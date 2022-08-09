package VetulusJava.Tribes.Entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Log{
    @Id
    @GeneratedValue
    public long id;
    public String loggingLvl;
    public LocalDateTime created;
    public int executionTime;
    public String requestMethod;
    public int responseStatus;
    @Column(length=1024)
    public String requestString;
    @Column(length=1024)
    public String responseString;
    public HttpStatus errorStatus;
    public String errorMessage;

    @Override
    public String toString(){
        var infoString = "Created: " + created + "\n" +
                         "Execution time: " + executionTime + "ms\n" +
                         "Request Method: " + requestMethod + "\n" +
                         "Response Status: " + responseStatus+"\n";

        if (loggingLvl.equals("info")){
            return  infoString;
        }
        else if (loggingLvl.equals("debug")) {
            return  infoString +
                    requestString + "\n" +
                    responseString + "\n";
        }
        else if (loggingLvl.equals("error")){
            return  "Created: " + created + "\n" +
                    "Error Status: " + errorStatus + "\n" +
                    "Error message: " + errorMessage+ "\n";
        }
        return "invalid logging level set";
    }
}
