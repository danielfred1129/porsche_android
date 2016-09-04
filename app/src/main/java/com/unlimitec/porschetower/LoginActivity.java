package com.unlimitec.porschetower;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.net.Uri;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.unlimitec.porschetower.datamodel.UserObject;
import com.unlimitec.network.PorscheTowerResponseHandler;
import com.unlimitec.utils.UserUtils;
import com.unlimitec.utils.Utils;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private int nNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = (EditText) findViewById(R.id.activity_login_email);
        edtPassword = (EditText) findViewById(R.id.activity_login_password);

        nNew = 1;

        UserObject user = UserUtils.getSession(this);
        if (user != null) {
            edtEmail.setText(user.getEmail());
            edtPassword.setText(user.getUserPass());

            nNew = 0;
            onLogin(null);
        }
    }

    public void onLogin(View v) {
        if (edtEmail.getText().toString().isEmpty()) {
            edtEmail.setError("Cannot be blank");
            return;
        }
        if (edtPassword.getText().toString().isEmpty()) {
            edtPassword.setError("Cannot be blank");
            return;
        }

        RequestParams params = new RequestParams();
        params.put("email", edtEmail.getText().toString());
        params.put("password", edtPassword.getText().toString());
        params.put("device_token", "d212d100f8e5706e257088151fe90fff669040e92c1eef5c5a12d3d4580b5837");

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Utils.BASE_URL + "login", params, new PorscheTowerResponseHandler(this) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,	JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (response != null) {
                    try {

                        if (!(response.isNull("owner"))) {
                            JSONArray jsonArray = response.getJSONArray("owner");
                            JSONObject json = jsonArray.getJSONObject(0);

                            UserObject user = new UserObject();
                            user.setIndex(json.getInt("index"));
                            user.setFirstName(json.getString("first_name"));
                            user.setLastName(json.getString("last_name"));
                            user.setEmail(json.getString("email"));
                            user.setPhone(json.getString("phone"));
                            user.setUnit(json.getJSONObject("unit"));
                            user.setId(json.getString("id"));
                            user.setLanguage(json.getString("language"));
                            user.setCatID(json.getInt("cat_id"));
                            UserUtils.storeSession(LoginActivity.this, user);
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                        else {
                            String login_url = "http://192.168.1.87/porsche/index.php/Login/LoginProcess?email=" + edtEmail.getText().toString() + "&password=" + edtPassword.getText().toString();

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(login_url));
                            startActivity(browserIntent);
                        }
                    } catch (JSONException e) {
                        Utils.showAlert(LoginActivity.this, "Happen some issues on the server. Please try again!");
                    }
                }
            }

        });

        nNew = 1;
    }
}
