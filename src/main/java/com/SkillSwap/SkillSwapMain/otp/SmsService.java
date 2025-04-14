package com.SkillSwap.SkillSwapMain.otp;
import com.twilio.exception.ApiException;
import com.twilio.type.PhoneNumber;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    public void sendOtp(String mobileNumber, String otp) {
        try {
            Twilio.init(accountSid, authToken);
            Message.creator(
                    new PhoneNumber(mobileNumber),
                    new PhoneNumber(fromPhoneNumber),
                    "Your OTP is: " + otp
            ).create();
            System.out.println("OTP sent successfully to " + mobileNumber);
        } catch (ApiException e) {
            System.err.println("Failed to send OTP: " + e.getMessage());
            throw new RuntimeException("Failed to send OTP: " + e.getMessage(), e);
        }
    }
}