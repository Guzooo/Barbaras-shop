package pl.Arkonsoft.Barbaras;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    public static final String LOGIN_URL = "http://restbrb.work.arkonsoft.pl/users";

    private ImageView imageSend;
    private EditText loginEdit;
    private EditText passwordEdit;
    private View progressBar;
    private View loginBox;
    private View errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView versionText = findViewById(R.id.main_version_text);
        loginBox = findViewById(R.id.main_login_box);
        imageSend = findViewById(R.id.main_login_image_send);
        loginEdit = findViewById(R.id.main_login_edit);
        passwordEdit = findViewById(R.id.main_password_edit);
        progressBar = findViewById(R.id.main_login_progress);
        errorText = findViewById(R.id.main_login_error);

        loginEdit.addTextChangedListener(this);
        passwordEdit.addTextChangedListener(this);
        imageSend.setOnClickListener(this);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionText.setText("v." + pInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                loginBox.setVisibility(View.VISIBLE);
            }
        }, 2000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_login_image_send:
                login();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        errorText.setVisibility(View.GONE);
        if(!isEmptyEditText(loginEdit) && !isEmptyEditText(passwordEdit))
            imageSend.setVisibility(View.VISIBLE);
        else
            imageSend.setVisibility(View.GONE);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private boolean isEmptyEditText(EditText editText){
        if(editText.getText().toString().equals(""))
            return true;
        else
            return false;
    }

    private String getStringOfEditText (EditText editText){
        return editText.getText().toString();
    }

    private void login(){
        if(!InternetUtils.isOnline(this)){
            Toast.makeText(this, R.string.internet_error, Toast.LENGTH_SHORT);
            return;
        }
        LoginTask loginTask = new LoginTask(getStringOfEditText(loginEdit), getStringOfEditText(passwordEdit));
        loginTask.execute();
    }

    private class LoginTask extends AsyncTask<Void, Void, Boolean>{

        private String email;
        private String password;

        LoginTask(String email, String password){
            this.email = email;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loginBox.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String pass = email + ":" + password;
            String string = "Basic " + Base64.encodeToString(pass.getBytes(), 2);
            String response;
            try {
                response = InternetUtils.makeServiceCallGetAuth(LOGIN_URL, string);
            } catch (Exception e){
                return false;
            }

            if(response == null || response.equals(""))
                return false;

            StorageUtils.getStorage(getApplicationContext()).setString(Constants.Z, response);
            StorageUtils.getStorage(getApplicationContext()).setString(Constants.CURRENCY, response);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if(aBoolean){
                Toast.makeText(getApplicationContext(), "Sukces", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.GONE);
                errorText.setVisibility(View.VISIBLE);
                loginBox.setVisibility(View.VISIBLE);
            }
        }
    }
}
