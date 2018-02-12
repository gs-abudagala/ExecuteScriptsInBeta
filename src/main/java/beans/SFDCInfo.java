package beans;

import com.sforce.soap.partner.LoginResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SFDCInfo {

    private String org;
    private String userId;
    private String sessionId;
    private String endpoint;
    private LoginResult loginResult;

    private String userName;
    private String userFullName;
    private String userEmail;
    private String authEndPoint;
    private String serviceEndPoint;
    private String userCurrencySymbol;
    private String userLocale;
    private String userTimeZone;
}
