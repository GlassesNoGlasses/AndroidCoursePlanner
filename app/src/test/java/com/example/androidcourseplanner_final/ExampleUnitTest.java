package com.example.androidcourseplanner_final;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.firebase.auth.FirebaseAuth;

import Backend.AuthenticationCallback;
import Backend.GetProfileCallback;
import Backend.LoginModel;
import Backend.LoginModelAdapter;
import Backend.LoginPresenter;
import Backend.Profile;
import Backend.Student;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Mock
    Login view;

    @Mock
    LoginModelAdapter modelAdapter;

    @Test
    public void testPresenter() {
        String email = "testUser@gmail.com";
        String pass = "pass123";
        when(view.getEmail()).thenReturn(email);
        when(view.getPassword()).thenReturn(pass);
        when(modelAdapter.model.getFirebaseAuth().signInWithEmailAndPassword(email, pass)).thenReturn(null);
        LoginPresenter presenter = new LoginPresenter(view, modelAdapter.model);
        presenter.signIn(email, pass, new AuthenticationCallback() {
            @Override
            public void onSuccess() {
                //Do nothing, let view take care of execution
            }
            @Override
            public void onFailure() {
                //Do nothing, let view take care of execution
            }
        });
        Mockito.verify(view).generateMessage("Invalid Login Information");

    }
    @Test
    public void testGetEmail() {
        String email = "testUser@gmail.com";
        when(view.getEmail()).thenReturn(email);
        assertEquals(view.getEmail(), email);
    }

}