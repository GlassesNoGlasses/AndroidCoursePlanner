package Backend;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.androidcourseplanner_final.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

//Use for actual login functionality
public class LoginPresenter {
    private Login view;
    private LoginModel model;

    public LoginPresenter(Login view, LoginModel model) {
        this.view = view;
        this.model = model;
    }

    public void signIn(String email, String password, AuthenticationCallback callback) {

        model.getFirebaseAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            callback.onFailure();
                            return;
                        }
                        Log.d("SignIn Success", "LoginPresenter");
                        callback.onSuccess();
                    }
                });
    }

}
