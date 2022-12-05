package Backend;

import java.util.regex.Pattern;

public abstract class UserInfoChecker {

    static Pattern regex = Pattern.compile("[$&+,:;=\\\\?#|/'<>.^*()%!-]");

    //Checks email
    public static String checkEmail(String email) {
        String emailCheck = null;
        if (!(email.contains("@") && email.contains(".")) || email.length() < 6)
            emailCheck = new String("Invalid Email");
        return emailCheck;
    }

    //Checks password
    public static String checkPassword(String password) {
        String passwordCheck = null;
        if (password.length() < 6)
            passwordCheck = new String("Password has to be at least 6 characters.");
        else if (regex.matcher(password).find()) passwordCheck = new String("Invalid Character Found");
        return passwordCheck;
    }

    //Checks username
    private static String checkUsername(String username) {
        String usernameCheck = null;
        if (username.length() < 2)
            usernameCheck = new String("Username has to be at least 2 characters.");
        else if (regex.matcher(username).find()) usernameCheck = new String("Invalid Character Found");
        return usernameCheck;
    }

    public static String checkAll(String username, String email, String password) {
        String name = checkUsername(username);
        String mail = checkEmail(email);
        String pass = checkPassword(password);
        if(name != null) return name;
        else if(mail != null) return mail;
        return pass;
    }
}
