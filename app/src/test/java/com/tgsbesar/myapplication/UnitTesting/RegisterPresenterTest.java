package com.tgsbesar.myapplication.UnitTesting;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RegisterPresenterTest {
    @Mock
    private RegisterView view;
    @Mock
    private RegisterService service;
    private RegisterPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new RegisterPresenter(view, service);
    }

    @Test
    public void shouldShowErrorWhenPasswordEmailIsEmpty() throws Exception{
        when(view.getEmail()).thenReturn("");
        System.out.println("email : "+view.getEmail());

        when(view.getPassword()).thenReturn("");
        System.out.println("Password : "+view.getPassword());

        presenter.onRegisterClicked();

        verify(view).showEmailError("Email tidak boleh kosong");
        verify(view).showPasswordError("Password tidak boleh kosong");
    }

    @Test
    public void shouldShowErrorWhenEmailIsEmpty() throws Exception {
        when(view.getEmail()).thenReturn("");
        System.out.println("email : "+view.getEmail());
        when(view.getPassword()).thenReturn("123456");
        System.out.println("password : "+view.getPassword());

        presenter.onRegisterClicked();

        verify(view).showEmailError("Email tidak boleh kosong");
    }

    @Test
    public void shouldShowErrorWhenPasswordIsEmpty() throws Exception{
        when(view.getEmail()).thenReturn("angelagloria68@gmail.com");
        System.out.println("email : "+view.getEmail());

        when(view.getPassword()).thenReturn("");
        System.out.println("Password : "+view.getPassword());

        presenter.onRegisterClicked();

        verify(view).showPasswordError("Password tidak boleh kosong");
    }

    @Test
    public void shouldShowErrorWhenEmailInvalid() throws Exception{
        when(view.getEmail()).thenReturn("angelagloria");
        System.out.println("email : "+view.getEmail());
        when(view.getPassword()).thenReturn("123456");
        System.out.println("password : "+view.getPassword());
        presenter.onRegisterClicked();

        verify(view).showEmailError("Email Tidak Valid");
    }

    @Test
    public void shouldShowErrorWhenPasswordLessThanSix() throws Exception{
        when(view.getEmail()).thenReturn("angelagloria68@gmail.com");
        System.out.println("email : "+view.getEmail());
        when(view.getPassword()).thenReturn("123");
        System.out.println("password : "+view.getPassword());
        presenter.onRegisterClicked();

        verify(view).showPasswordError("Password Tidak Boleh Kurang dari 6");
    }

    @Test
    public void shouldStartMainActivityWhenEmailandPasswordAreCorrect() throws Exception{
        when(view.getEmail()).thenReturn("angelagloria68@gmail.com");
        System.out.println("email : "+view.getEmail());
        when(view.getPassword()).thenReturn("123456");
        System.out.println("password : "+view.getPassword());
        when(service.getValid(view, view.getEmail(),
                view.getPassword())).thenReturn(true);
        System.out.println("Hasil : "+service.getValid(view,view.getEmail(),
                view.getPassword()));
        presenter.onRegisterClicked();
    }
}