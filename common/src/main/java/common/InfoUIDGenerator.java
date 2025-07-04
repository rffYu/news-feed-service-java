package common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class InfoUIDGenerator {

    public static String generateInfoUID(String source, String title, String additionalIDString) {
        try {
            // Concatenate the input strings
            String combined = source + title + additionalIDString;

            // Create MD5 digest instance
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Calculate the hash bytes
            byte[] hashBytes = md.digest(combined.getBytes(StandardCharsets.UTF_8));

            // Convert the hash bytes to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available.", e);
        }
    }
}
