package Backend;

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
}
