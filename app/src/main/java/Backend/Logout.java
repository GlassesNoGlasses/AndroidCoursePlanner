package Backend;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

public abstract class Logout {

    //User sign-out function
    public static boolean signOut() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        try{
            auth.signOut();
            Log.d("Signout", "Signout success");
            return true;

        } catch (Exception e) {
            Log.d("Signout", "Signout failed");
        }
        return false;
    }
}
