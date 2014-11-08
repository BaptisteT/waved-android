package com.waved.streetshout.waved.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.waved.streetshout.waved.R;

import utils.ApiUtils;

public class PhoneValidationActivity extends Activity {

    private Button validateButton = null;
    private EditText phoneNumberEditText = null;
    private ProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_validation);

        validateButton = (Button) findViewById(R.id.phone_validation_validate_button);
        phoneNumberEditText = (EditText) findViewById(R.id.phone_validation_edit_text);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                Phonenumber.PhoneNumber phoneNumber = null;

                try {
                    phoneNumber = phoneUtil.parse(phoneNumberEditText.getText().toString(), "US");
                } catch (NumberParseException e) {
                    System.err.println("NumberParseException was thrown: " + e.toString());
                }

                if (phoneNumber == null || !phoneUtil.isValidNumber(phoneNumber)) {
                    Toast.makeText(PhoneValidationActivity.this,
                                        getString(R.string.phone_validation_invalid_phone_number),
                                                                         Toast.LENGTH_SHORT).show();
                    return;
                }


                dialog = ProgressDialog.show(PhoneValidationActivity.this, "",
                                        getString(R.string.phone_validation_loading_dialog), false);

                ApiUtils.requestSmsCode(PhoneValidationActivity.this,
                    phoneNumberEditText.getText().toString(), false, new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                dialog.cancel();
                                Intent intent = new Intent(PhoneValidationActivity.this, CodeConfirmationActivity.class);
                                intent.putExtra("phone_number", phoneNumberEditText.getText().toString());
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                dialog.cancel();
                                Toast.makeText(PhoneValidationActivity.this,
                                        getString(R.string.phone_validation_invalid_phone_number),
                                                                         Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });
    }
}
