import javax.crypto.*;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.security.*;
/* This File Should describe the Encoding and Decoding methods of AES ciphering
* For the moment, the implemented mode is the CBC mode,
* Authentication using a TAG will be implemented later on */
public class AES {
    public static final int AES_KEY_SIZE_128 = 128;
    public static final int AES_KEY_SIZE_192 = 192;
    public static final int AES_KEY_SIZE_256 = 256;
    public static final int GCM_IV_LENGTH = 12;
    public static final int CBC_IV_LENGTH = 16;
    public static final int GCM_TAG_LENGTH = 16;
    public static final String AES_CBC_PADDING = "AES/CBC/PKCS5Padding";
    public static final String AES_GCM_PADDING = "AES/GCM/NoPadding";


    public static IvParameterSpec CBCIVGen(){
        /* generates an initialization vector for the CBC mode */
        byte[] Iv = new byte[CBC_IV_LENGTH]; /* the initialization vector that will be used in the first E */

        new SecureRandom().nextBytes(Iv); /*Generating new random bytes using a Secure RNG */

        return new IvParameterSpec(Iv); /* Encapsulate the IV */
    }

    public static byte[]  GCMIVGen (){
        /* Generates an initialization vector for the GCM Mode*/

        byte[] IV = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(IV); /* secure gen of a vector of random bytes*/
        return IV;
    }
    public static SecretKey generateKey( int keySize ) throws NoSuchAlgorithmException{

        KeyGenerator keyGen= KeyGenerator.getInstance("AES");

        keyGen.init(keySize);
        return keyGen.generateKey();
    }
    public SecretKey KeyToString ( byte[] keyBytes){

        return new SecretKeySpec(keyBytes,"AES");
    }

    public static String CBC_Encrypt(String PlainText, SecretKey Key, IvParameterSpec IV)
    {
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE,Key,IV);
            byte[] encrypted = cipher.doFinal(PlainText.getBytes());

            ByteBuffer byteBuffer = ByteBuffer.allocate(IV.getIV().length+ encrypted.length);
            byteBuffer.put(IV.getIV());
            byteBuffer.put(encrypted);
            return Base64.getEncoder().encodeToString(byteBuffer.array());
        }catch (Exception e){
            throw new RuntimeException("Something bad occured during the encryption : ",e);
        }
    }
    public static String CBC_decrypt( String CipheredText, SecretKey Key )
    {
        try
        {
            byte[] Decrypted = Base64.getDecoder().decode(CipheredText);
            ByteBuffer byteBuffer = ByteBuffer.wrap(Decrypted);
            byte[] IV = new byte[CBC_IV_LENGTH];
            byteBuffer.get(IV);
            byte[] cipherBytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(cipherBytes);
            Cipher cipher = Cipher.getInstance(AES_CBC_PADDING);
            cipher.init(Cipher.DECRYPT_MODE,Key, new IvParameterSpec(IV));
            byte[] plainBytes = cipher.doFinal(cipherBytes);
            return new String(plainBytes);
        }
        catch (Exception e)
        {
            System.err.println("An error occured during the Dec \n"+ e);
        }
        return "";/* not accessible..*/
    }

    public static void main(String[] args) {
        try{
            SecretKey key = generateKey(128);
            String MessageToEncrypt = "Ceci est le message qui sera encrypté !";
            System.out.println("Message de base : "+ MessageToEncrypt + "\n");

            IvParameterSpec IV = CBCIVGen();
            String CBC_Encrypted_Message = CBC_Encrypt(MessageToEncrypt, key, IV);
            System.out.println("Encoded : "+CBC_Encrypted_Message + "\n");

            String CBC_Decrypted_Message = CBC_decrypt(CBC_Encrypted_Message,key);
            System.out.println("Decoded : " + CBC_Decrypted_Message +"\n" );

        }catch (Exception e){
            throw new RuntimeException("Something bad occured during the key gen : ",e);
        }
    }
}

