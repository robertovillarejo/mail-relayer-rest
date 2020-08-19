package mx.conacyt.crip.mail.domain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Random;

import lombok.Getter;

/**
 * Una clave secreta.
 * <p>
 * Se usa como credencial para enviar correos electrónicos.
 */
public class SecretKey {

    private static final int NUMERAL_ZERO = 48;
    private static final int NUMERAL_NINE = 57;
    private static final int LETTER_A_LOWERCASE = 97;
    private static final int LETTER_A_UPPERCASE = 65;
    private static final int LETTER_Z_LOWERCASE = 122;
    private static final int LETTER_Z_UPPERCASE = 90;
    private static final int CONTENT_LENGTH = 24;
    private static final int BIT_MASK = 0xff;

    @Getter
    private String content;

    SecretKey(String content) {
        this.content = content;
    }

    /**
     * Obtiene el contenido hasheado de la secretKey.
     *
     * @return hash del contenido.
     * @throws NoSuchAlgorithmException
     */
    public String hashed() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = md.digest(content.getBytes());
        return bytesToHex(encodedHash);
    }

    public static SecretKey generate() {
        return new SecretKey(generateContent());
    }

    public static SecretKey of(String content) {
        return new SecretKey(content);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(BIT_MASK & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static String generateContent() {
        int leftLimit = NUMERAL_ZERO;
        int rightLimit = LETTER_Z_LOWERCASE;

        return new Random().ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= NUMERAL_NINE || i >= LETTER_A_UPPERCASE)
                        && (i <= LETTER_Z_UPPERCASE || i >= LETTER_A_LOWERCASE))
                .limit(CONTENT_LENGTH)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SecretKey)) {
            return false;
        }
        SecretKey secretKey = (SecretKey) o;
        return Objects.equals(content, secretKey.content);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(content);
    }

}
