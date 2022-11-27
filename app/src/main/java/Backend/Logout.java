package Backend;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

public abstract class Logout {

    //User signout function
    public static boolean signOut() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        try{
            auth.getInstance().signOut();
            Log.d("Signout", "Signout success");
            return true;

        } catch (Exception e) {
            //TODO Create alert upon failure
            Log.d("Signout", "Signout failed");
        }
        return false;
    }
}
