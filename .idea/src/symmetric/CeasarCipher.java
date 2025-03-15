package symmetric;

public class CeasarCipher {

    public static String encrypt( String message , int shifter ){
        StringBuilder cipher = new StringBuilder();
        shifter = shifter%26;
        if(shifter<0) shifter+=26;
        for( char c : message.toCharArray() ){
            if(Character.isLetter(c))
            {
                char lower_bound = Character.isLowerCase(c)? 'a': 'A'; /* setting the lower bound to 'a'
                                                                        if the char c is lowercase, 'A' else*/
                int tempo_ = ( c + shifter - lower_bound);
                cipher.append((char)(  tempo_ % 26 + lower_bound));
            }
            else
            {
                cipher.append(c); /* we don't encrypt other symbols */
            }
        }
        return cipher.toString();

    }
    public static String decrypt (String cipher, int shifter ){

        return encrypt(cipher, 26 - (shifter % 26));
    }

    public static void main(String[] args) {

        System.out.println("Testing Ceasar encryption : \n");
        String plaintext = "Hello World !";
        String deterministric = "XXXX";
        System.out.println(" Original message was : " + plaintext + " Encryption : " + encrypt(plaintext,20)
                            + " Decryption : "+ decrypt(encrypt(plaintext,20),20) + "\n");
        System.out.println(" Original message was : " + deterministric + " Encryption : " + encrypt(deterministric,20)
                + " Decryption : "+ decrypt(encrypt(deterministric,15),15) + "\n");
    }
}
