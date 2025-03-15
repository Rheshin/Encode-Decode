package symmetric;
import java.util.Base64;

public class Xor {
    public static String encrypt( String message , byte shifter )
    {
        byte[] plain = message.getBytes();
        byte[] cipher = new byte[plain.length];

        for (int i = 0; i < message.length(); i++)
        {
            cipher[i]=(byte)(plain[i]^shifter);
        }

        return Base64.getEncoder().encodeToString(cipher);

    }
    public static String decrypt (String cipher, byte shifter ){
        byte[] cipherBytes = Base64.getDecoder().decode(cipher);
        byte[] result = new byte[cipherBytes.length];
        for (int i = 0; i < cipherBytes.length; i++) {
            result[i]= (byte)( cipherBytes[i] ^ shifter );
        }
        return encrypt(cipher, shifter); // X xor K xor K == X
    }

    public static void main(String[] args) {

        System.out.println("Testing Ceasar encryption : \n");
        String plaintext = "Hello World !";
        String deterministric = "XXXX";
        System.out.println(" Original message was : " + plaintext + " Encryption : " + encrypt(plaintext,(byte)20)
                + " Decryption : "+ decrypt(encrypt(plaintext,(byte) 20),(byte) 20) + "\n");
        System.out.println(" Original message was : " + deterministric + " Encryption : " + encrypt(deterministric,(byte)20)
                + " Decryption : "+ decrypt(encrypt(deterministric,(byte)15),(byte)15) + "\n");
    }
}
