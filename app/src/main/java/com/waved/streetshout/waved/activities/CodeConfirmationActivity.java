package com.waved.streetshout.waved.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.waved.streetshout.waved.R;

import java.util.ArrayList;

import utils.ApiUtils;
import utils.Constants;

public class CodeConfirmationActivity extends Activity {

    private TextView digit1 = null;
    private TextView digit2 = null;
    private TextView digit3 = null;
    private TextView digit4 = null;
    private TextView phoneNumberTextView = null;
    private TextView statusTextView = null;
    private TextView timeRemainingTextView = null;
    private EditText codeEditText = null;
    private ArrayList<TextView> digits = null;
    private ProgressDialog dialog = null;
    private String phoneNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_confirmation);

        digit1 = (TextView) findViewById(R.id.code_confirmation_digit_1);
        digit2 = (TextView) findViewById(R.id.code_confirmation_digit_2);
        digit3 = (TextView) findViewById(R.id.code_confirmation_digit_3);
        digit4 = (TextView) findViewById(R.id.code_confirmation_digit_4);
        phoneNumberTextView = (TextView) findViewById(R.id.code_confirmation_phone_number);
        statusTextView = (TextView) findViewById(R.id.code_confirmation_status);
        timeRemainingTextView = (TextView) findViewById(R.id.code_confirmation_time_remaining);
        codeEditText = (EditText) findViewById(R.id.code_confirmation_edit_text);

        phoneNumber = getIntent().getStringExtra("phone_number");

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phoneNumberObject = null;

        try {
            phoneNumberObject = phoneUtil.parse(phoneNumber, "US");
            phoneNumberTextView.setText(phoneUtil.format(phoneNumberObject, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
            phoneNumberTextView.setText(phoneNumber);
        }

        digit1.setText("");
        digit2.setText("");
        digit3.setText("");
        digit4.setText("");

        digits = new ArrayList<TextView>();
        digits.add(digit1);
        digits.add(digit2);
        digits.add(digit3);
        digits.add(digit4);

        codeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.toString().length();

                for (int i = 0; i < Constants.CONFIRMATION_CODE_DIGITS; i++) {
                    if (i < length) {
                        digits.get(i).setText("" + s.toString().substring(i,i+1));
                    } else {
                        digits.get(i).setText("");
                    }
                }

                if (length >= Constants.CONFIRMATION_CODE_DIGITS) {
                    validateCode();
                    return;
                }
            }
        });
    }

    private void validateCode()
    {
        dialog = ProgressDialog.show(CodeConfirmationActivity.this, "",
                                        getString(R.string.phone_validation_loading_dialog), false);

        ApiUtils.checkConfirmationCode(this, phoneNumber, codeEditText.getText().toString(),
                                                                     new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        //Todo BB: profile picture activity
                        Log.d("BAB", "SUCCES!");
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.cancel();

                        for (int i = 0; i < Constants.CONFIRMATION_CODE_DIGITS; i++) {
                            digits.get(i).setText("");
                        }

                        codeEditText.setText("");

                        Toast.makeText(CodeConfirmationActivity.this,
                                getString(R.string.code_confirmation_wrong_code),
                                                                        Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Log.d("BAB", "Code " + codeEditText.getText());
    }
}
