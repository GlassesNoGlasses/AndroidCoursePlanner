package com.example.androidcourseplanner_final;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

import Backend.LoginModel;
import Backend.LoginPresenter;

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
    LoginModel model;

    @Test
    public void testPresenter() {

    }

    @Test
    public void testCheckInfo() {
        //To be completed
    }

    @Test
    public void testSignUp() {
        // To be completed
    }

    @Test
    public void testSignIn() {
        // To be completed
    }

    @Test
    public void testIsSignedIn() {
        // To be completed
    }

    @Test
    public void testGetProfile() {
        // To be completed
    }
}