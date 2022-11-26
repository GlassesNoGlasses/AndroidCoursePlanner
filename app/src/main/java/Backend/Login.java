package Backend;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//class is abstract so it cannot be instantiated
public abstract class Login {
    static DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

    public static void signUp(FirebaseAuth auth, String username, String password) {
        auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    //TODO add something that happens on fail
                    return;
                }

                //signup successful, add student to database
                usersRef.child("Students").child(auth.getCurrentUser()
                        .getUid()).setValue(new Student(username));
            }
        });
    }

    public static void signIn(FirebaseAuth auth, String username, String password) {

        auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    //TODO add something that happens on fail
                    return;
                }
            }
        });
    }

    public static Profile getProfile(DataSnapshot s, String id) {
        if (s.child("Students").hasChild(id))
            return s.child("Students").child(id).getValue(Student.class);
        if (s.child("Admins").hasChild(id))
            return s.child("Admins").child(id).getValue(Admin.class);
        return null;
    }
}
