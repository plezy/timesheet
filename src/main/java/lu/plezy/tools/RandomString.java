package lu.plezy.tools;

public class RandomString {

    private static final String CharacterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";

    // function to generate a random string of length n
    public static String getAlphaNumericString(int n) {
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index = (int) (CharacterSet.length() * Math.random());
            sb.append(CharacterSet.charAt(index));
        }

        return sb.toString();
    }
}