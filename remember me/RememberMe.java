package Extras;

import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by ACER on 16-Feb-16.
 */
public class RememberMe
{

    MySharedPreferences mySharedPreferences;
    EditText etUserId;
    EditText etPassword;

    public RememberMe(MySharedPreferences mySharedPreferences,CheckBox chkRememberMe,EditText etUserId,EditText etPassword) {

        this.etUserId=etUserId;
        this.etPassword=etPassword;
        this.mySharedPreferences=mySharedPreferences;
        chkRememberMe.setChecked(isRemembered());
    }

    public boolean isRemembered()
    {
        if(!mySharedPreferences.getSharedPreferences().getString("user_id", "").equals(""))
        {
            etUserId.setText(mySharedPreferences.getSharedPreferences().getString("user_id", ""));
            etPassword.setText(mySharedPreferences.getSharedPreferences().getString("password", ""));
            return true;
        }
        else
        {
            return false;
        }
    }

    public void set()
    {
        Log.i("STS " + RememberMe.class.getSimpleName(), "saving id password " + etUserId.getText().toString() + " " + etPassword.getText().toString());
        mySharedPreferences.addString("user_id", etUserId.getText().toString());
        mySharedPreferences.addString("password", etPassword.getText().toString());
    }

    public void unset()
    {
        mySharedPreferences.addString("user_id", "");
        mySharedPreferences.addString("password", "");
    }

}
