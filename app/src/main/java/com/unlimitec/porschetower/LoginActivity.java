package com.unlimitec.porschetower;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.unlimitec.porschetower.datamodel.UserObject;
import com.unlimitec.porschetower.network.PorscheTowerResponseHandler;
import com.unlimitec.porschetower.utils.UserUtils;
import com.unlimitec.porschetower.utils.Utils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private TextView txt_porsche_design, txt_tower_miami, txt_p0001, txt_login;
    private Button activity_login_btn;
    private int nNew;
    private String tempLocale;
    private int tempLogoutTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = (EditText) findViewById(R.id.activity_login_email);
        edtPassword = (EditText) findViewById(R.id.activity_login_password);

        Typeface font = Typeface.createFromAsset(getAssets(), "porschedesignfont.otf");
        activity_login_btn = (Button) findViewById(R.id.activity_login_btn);
        activity_login_btn.setTypeface(font);

        nNew = 1;

        UserObject user = UserUtils.getSession(this);
        if (user != null) {
            edtEmail.setText(user.getEmail());
            edtPassword.setText(user.getUserPass());
            if (user.getLocale() != null) {
                tempLocale = user.getLocale();
            }
            if (user.getLogoutTime() != 0) {
                tempLogoutTime = user.getLogoutTime();
            }

            nNew = 0;
            onLogin(null);
        }
//          This was on PorscheTowerApplication.java before
//        initImageLoader();
        initImageLoader(getApplicationContext());
    }

    public void initImageLoader() {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(getApplicationContext());
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        ImageLoader.getInstance().init(config.build());
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
        ImageLoader.getInstance().init(config);
        ImageLoader.getInstance().clearDiskCache();
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
        params.put("device_token", "1324325435346346456546");
        params.put("os_type", "android");

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
                            user.setUserPass(json.getString("password"));
                            user.setPhone(json.getString("phone"));
                            user.setUnit(json.getJSONObject("unit"));
                            user.setId(json.getString("id"));
                            user.setLanguage(json.getString("language"));
                            user.setCatID(json.getInt("cat_id"));

                            if (tempLocale == null)
                            {
                                Locale current = getResources().getConfiguration().locale;
                                user.setLocale(current.toString());
                            }
                            else
                            {
                                user.setLocale(tempLocale);
                            }

                            if (tempLogoutTime == 0)
                            {
                                user.setLogoutTime(1);
                            }
                            else
                            {
                                user.setLogoutTime(tempLogoutTime);
                            }

                            UserUtils.storeSession(LoginActivity.this, user);
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                        else {
                            String login_url = Utils.LOGIN_BASE_URL + edtEmail.getText().toString() + "&password=" + edtPassword.getText().toString();
//                            String login_url = "http://pdtowerapp.com/index.php/Login/LoginProcess?email=" + edtEmail.getText().toString() + "&password=" + edtPassword.getText().toString();

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
