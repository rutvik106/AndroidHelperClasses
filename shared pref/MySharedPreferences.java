package Extras;


import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class MySharedPreferences {

    public static final String TAG="STS "+MySharedPreferences.class.getSimpleName();

    private SharedPreferences sharedPreferences;

    private Editor editor;


    public MySharedPreferences(Context context)
    {
        sharedPreferences=context.getSharedPreferences("sts_shared_preferences", Context.MODE_PRIVATE);
        this.editor=sharedPreferences.edit();
    }

    public void addString(String key,String value)
    {
        editor.putString(key, value);
        editor.commit();
    }

    public void addBoolean(String key,Boolean value)
    {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void addFoloat(String key,Float value)
    {
        editor.putFloat(key, value);
        editor.commit();
    }

    public void addLong(String key,Long value)
    {
        editor.putLong(key, value);
        editor.commit();
    }

    public void addInt(String key,int value)
    {
        editor.putInt(key, value);
        editor.commit();
    }

    public void addStringSet(String key,Set<String> values)
    {
        editor.putStringSet(key, values);
        editor.commit();
    }

    public void addIntoSet(String key,String value)
    {
        Log.i(TAG, "adding into set key: "+key+" value: "+value);

        Set<String> newSet=new HashSet<String>();
        newSet=sharedPreferences.getStringSet(key, new HashSet<String>());

        editor.remove(key);

        editor.commit();

        newSet.add(value);

        editor.putStringSet(key, newSet);
        editor.commit();
    }

    public void removeFromSet(String key,String value)
    {
        Log.i(TAG, "removing from set key: "+key+" value: "+value);

        Set<String> newSet=new HashSet<String>();
        newSet=sharedPreferences.getStringSet(key, new HashSet<String>());
        newSet.remove(value);

        editor.putStringSet(key, newSet);
        editor.commit();
    }

    public SharedPreferences getSharedPreferences()
    {
        return sharedPreferences;
    }



}