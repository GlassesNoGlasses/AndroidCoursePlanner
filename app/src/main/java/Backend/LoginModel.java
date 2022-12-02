package Backend;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//Singleton; use for connecting to firebase and creating users
public final class LoginModel {

    private static DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static LoginModel instance;

    public static LoginModel getInstance() {
        if(instance == null) {
            instance = new LoginModel();
        }
        return instance;
    }

    public FirebaseAuth getFirebaseAuth() {
        return auth;
    }

    public DatabaseReference getUsersRef() {
        return usersRef;
    }

    public void signUp(String username, String email,
                       String password, AuthenticationCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.println(Log.WARN, "signup Failed", "Here");
                            callback.onFailure();
                            return;
                        }

                        //signup successful, add student to database
                        ProfileManager.changeProfile(username);
                        usersRef.child("Students").child(auth.getCurrentUser()
                                .getUid()).setValue(new Student(username));
                        Logout.signOut();

                        callback.onSuccess();
                    }
                });
    }
    private boolean isSignedIn() {
        return auth.getCurrentUser() != null;
    }

    public void getProfile(GetProfileCallback callback) {
        if (!isSignedIn()) {
            Log.d("Firebase", "User is not signed in, getProfile() failed");
            return;
        }

        String id = auth.getCurrentUser().getUid();

       usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Students").hasChild(id)) {
                    callback.onStudent(snapshot.child("Students").child(id).getValue(Student.class));
                } else if (snapshot.child("Admins").hasChild(id)) {
                    callback.onAdmin(snapshot.child("Admins").child(id).getValue(Profile.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase: ", "Failed to retrieve profile.");
            }
        });
    }

    public void setStudentChanges(Student s) {
        usersRef.child("Students").child(auth.getCurrentUser()
                .getUid()).setValue(s);
    }
}
