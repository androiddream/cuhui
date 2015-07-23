package com.example.blank.cuhui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.umeng.message.PushAgent;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class SignActivity extends ActionBarActivity {

    @InjectView(R.id.email)
    AutoCompleteTextView mEmail;
    @InjectView(R.id.password)
    EditText mPassword;
    private String mUser;
    private String mPass;
    private String admin ="admin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();
        getSupportActionBar().setTitle("登陆");


    }


    public void sign(View v) {
        Toast.makeText(this, "test", Toast.LENGTH_SHORT
        ).show();
        mUser= mEmail.getText().toString();
        mPass = mPassword.getText().toString();
        if (mUser.equals(admin)&mPass.equals(admin))
        {

            startActivity(new Intent(this, MainActivity.class));
        }
        else {
            Toast.makeText(this,"用户名或密码不正确",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
