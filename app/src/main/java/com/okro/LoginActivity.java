package com.okro;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    Context context;
    TextView tvLanding, tvNext;
    EditText etFirstName, etLastName, etMobile;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    FirebaseAuth auth;
    private String verificationCode;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        tvLanding = findViewById(R.id.tvLanding);
        tvNext = findViewById(R.id.tvNext);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etMobile = findViewById(R.id.etMobile);

        Typeface nexaBold = FontManager.getNexaBold(context);
        Typeface nexaLight = FontManager.getNexaLight(context);

        tvLanding.setTypeface(nexaBold);
        tvLanding.setTypeface(nexaBold);
        etFirstName.setTypeface(nexaLight);
        etLastName.setTypeface(nexaLight);
        etMobile.setTypeface(nexaLight);

        tvNext.setOnClickListener(this);

        StartFirebaseLogin();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvNext:
                if (checkValidation()) {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+etMobile.getText().toString().trim(), 60,
                            TimeUnit.SECONDS, LoginActivity.this, mCallback);

                }
                break;
        }
    }

    public Boolean checkValidation() {
        if (etFirstName.getText().toString().trim().length() == 0) {
            etFirstName.setError("Enter First Name");
            return false;
        } else if (etLastName.getText().toString().trim().length() == 0) {
            etLastName.setError("Enter Last Name");
            return false;
        } else if (etMobile.getText().toString().trim().length() == 0) {
            etMobile.setError("Enter Mobile Number");
            return false;
        } else if (etMobile.getText().toString().trim().length() < 10) {
            etMobile.setError("Enter correct Mobile Number");
            return false;
        }
        return true;
    }

    public void getVerifyOTPDialog() {
        dialog = new Dialog(LoginActivity.this, R.style.MyDialogThemeRatingBar);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.verify_otp_dialog);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

//        dialog.getWindow().setLayout((8 * width) / 9, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final TextView tvVerifyMobile = dialog.findViewById(R.id.tvVerifyMobile);
        final EditText etOtp = dialog.findViewById(R.id.etOtp);
        final TextView tvVerify = dialog.findViewById(R.id.tvVerify);

        Typeface nexaBold = FontManager.getNexaBold(LoginActivity.this);
        Typeface nexaLight = FontManager.getNexaLight(LoginActivity.this);


        tvVerifyMobile.setTypeface(nexaBold);
        etOtp.setTypeface(nexaLight);
        tvVerify.setTypeface(nexaBold);
        dialog.show();

        etOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etOtp.getText().toString().trim().length() >= 1){
                    if (etOtp.getError() != null)
                        etOtp.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etOtp.getText().toString().trim().length() == 0){
                    etOtp.setError("Enter otp");
                }
                else {
                    try {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, etOtp.getText().toString().trim());
                        SigninWithPhone(credential, etOtp);
                    }
                    catch (IllegalArgumentException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void StartFirebaseLogin() {

        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(LoginActivity.this,"verification completed",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,CategoriesActivity.class));
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                finish();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.e("failed",e.toString());
                Toast.makeText(LoginActivity.this,"verification failed",Toast.LENGTH_SHORT).show();
                if (dialog != null)
                    dialog.dismiss();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(LoginActivity.this,"Code sent", Toast.LENGTH_SHORT).show();
                getVerifyOTPDialog();
            }
        };
    }
    private void SigninWithPhone(PhoneAuthCredential credential, final EditText etOtp) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this,CategoriesActivity.class));
                            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                            finish();
                        } else {
                            etOtp.setError("Incorrect OTP");
                            Toast.makeText(LoginActivity.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
