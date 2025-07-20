package AG_FinTrust;

import java.util.Base64;

/**
 * Utility class for encoding and decoding PINs using Base64.
 * Enhances security by storing encoded PINs instead of plain text.
 *
 * Used across various modules of the Transacto: AG_FinTrust system.
 *
 * @author Aditya Gupta
 * @version 1.0.0
 * @since July 20, 2025
 */
public class PinEncoderDecoder {

    /**
     * Encodes a plain PIN using Base64 encoding.
     *
     * @param pin_number The plain text PIN
     * @return Encoded PIN as Base64 string
     */
    protected static String encode(String pin_number) {
        return Base64.getEncoder().encodeToString(pin_number.getBytes());
    }

    /**
     * Decodes an encoded Base64 PIN back to plain text.
     *
     * @param encoded_pin The encoded Base64 PIN
     * @return Decoded plain text PIN
     */
    protected static String decode(String encoded_pin) {
        byte[] pin_array = Base64.getDecoder().decode(encoded_pin);
        return new String(pin_array);
    }
}
