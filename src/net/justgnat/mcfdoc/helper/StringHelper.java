package net.justgnat.mcfdoc.helper;

public class StringHelper {

    /**
     * @throws StringIndexOutOfBoundsException if the string is empty
     */
    public static String capitaliseFirstCharacter(String string) {
        char firstChar = string.charAt(0);
        if (firstChar >= 'a' && firstChar <= 'z') {
            firstChar = (char)(firstChar - 32);
            string = firstChar + string.substring(1);
        }
        return string;
    }

}
