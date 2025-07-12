package AG_FinTrust;

import  java.util.Base64;

public class PinEncoderDecoder {
    protected static String encode(String pin_number){
        String encoded_pin = Base64.getEncoder().encodeToString(pin_number.getBytes());
        return encoded_pin;
    }
    protected static String decode(String encoded_pin){
        byte[] pin_array = Base64.getDecoder().decode(encoded_pin);
        return new String(pin_array);
    }
}

