package Backend;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

//class is abstract so it cannot be instantiated
public abstract class Login {
    static DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
    static FirebaseAuth auth = FirebaseAuth.getInstance();

    //Checks if the username and password on signup are valid
    public static String checkInfo(String username, String password) {
        //TODO integrate with UI alerts
        if (username.length() < 2)
                return "Username has to be at least 2 characters.";
        else if (password.length() < 6)
                return "Password has to be at least 6 characters.";

//        Check for special characters we don't want
        Pattern regex = Pattern.compile("[$&+,:;=\\\\?#|/'<>.^*()%!-]");
        if (regex.matcher(username).find() || regex.matcher(password).find()) {
            return "Invalid Character Found.";
        }
        //returns null on success (no errors found)
        return null;
    }

    //automatically checks email by firebase
    public static void signUp(String username, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    //TODO add something that happens on fail
                    Log.println(Log.WARN, "signup Failed", "Here");
                    return;
                }

                //signup successful, add student to database
                ProfileManager.changeProfile(username);
                usersRef.child("Students").child(auth.getCurrentUser()
                        .getUid()).setValue(new Student(username));
                Logout.signOut();
            }
        });
    }

    public static void signIn(String email, String password) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    //TODO add something that happens on fail
                    return;
                }
                Log.println(Log.WARN, "SignIn Success", "Here");
            }
        });
    }

    private static boolean isSignedIn() {
        return auth.getCurrentUser() != null;
    }

    public static void getProfile(GetProfileCallback callback) {
        if (!isSignedIn()) {
            Log.e("Firebase", "User is not signed in, getProfile() failed");
            return;
        }

        String id = auth.getCurrentUser().getUid();

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Students").hasChild(id)) {
                    callback.onCallback(snapshot.child("Students").child(id).getValue(Student.class));
                } else if (snapshot.child("Admins").hasChild(id)) {
                    callback.onCallback(snapshot.child("Admins").child(id).getValue(Admin.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Failed to retrieve profile");
            }
        });
    }
}
