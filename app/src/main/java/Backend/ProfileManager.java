package Backend;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

//Manages the Profile accounts;
public abstract class ProfileManager {
    private static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public static void changeProfile(String username) {
        if(user == null) return;
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username).build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Profile", "User profile updated.");
                            return;
                        }
                        Log.d("Profile", "User profile Failed.");
                    }
                });
    }

    public static String getDisplayName() {
        if(user == null)  return null;
        return user.getDisplayName();
    }
    public static void changeEmail(String email) {
        if(user == null) return;
        user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Log.d("EmailChange", "Success");
                    return;
                }
                Log.d("EmailChange", "Invalid Email");
            }
        });
    }

    public static void changePassword(String password) {
        user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Log.d("PasswordChange", "Success");
                    return;
                }
                Log.d("PasswordChange", "Invalid Password");
            }
        });
    }
}
