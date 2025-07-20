package AG_FinTrust;

import java.util.Scanner;

/**
 * Utility class to provide a centralized Scanner instance
 * for input handling across the Transacto: AG_FinTrust system.
 *
 * This avoids creating multiple Scanner instances throughout the codebase.
 *
 * @author Aditya Gupta
 * @version 1.0.0
 * @since July 20, 2025
 */
public class InputUtil {
    public static final Scanner sc = new Scanner(System.in);
}
