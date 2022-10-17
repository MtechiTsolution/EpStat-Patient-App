package com.example.addpatient;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences  usersSession;
    Context context;
    SharedPreferences.Editor editor;

    private static final String IS_LOGIN="IsLoggedIn";

    public  static  final String KEY_FULLNAME="fullName";
    public  static  final String KEY_USERNAME="username";
    public  static  final String KEY_PHONE="phone";
    public  static  final String KEY_EMAIL="email";
    public  static  final String KEY_GENDER="gender";
    public  static  final String KEY_DESIGNATION="designation";
    public  static  final String KEY_PASSWORD="password";
    public  static  final String KEY_DATE="date";
    public static final String KEY_IMAGE="image";
    public SessionManager(Context _context)

    {
        context=_context;
        usersSession=context.getSharedPreferences ( "userLoginSession",Context.MODE_PRIVATE );
        editor=usersSession.edit ();

    }

    public  void createLoginSession(String fullName,String username,String phone,String email,String designation,String gender,String password,String imageurl)
    {
        editor.putBoolean ( IS_LOGIN,true );

        editor.putString ( KEY_FULLNAME,fullName );
        editor.putString ( KEY_USERNAME,username );
        editor.putString ( KEY_PHONE,phone );
        editor.putString ( KEY_EMAIL,email );
        editor.putString ( KEY_DESIGNATION,designation );
        editor.putString ( KEY_GENDER,gender );
        editor.putString ( KEY_PASSWORD,password );
        editor.putString ( KEY_IMAGE,imageurl );

        editor.commit ();
    }
public HashMap<String,String> getUsersDetailFromSession()
{
    HashMap<String,String> userData=new HashMap<String,String> ();
    userData.put ( KEY_FULLNAME,usersSession.getString ( KEY_FULLNAME,null ) );
    userData.put ( KEY_USERNAME,usersSession.getString (KEY_USERNAME ,null ) );
    userData.put ( KEY_PHONE,usersSession.getString (KEY_PHONE ,null ) );
    userData.put ( KEY_EMAIL,usersSession.getString (KEY_EMAIL ,null ) );
    userData.put ( KEY_DESIGNATION,usersSession.getString ( KEY_DESIGNATION,null ) );
    userData.put (KEY_GENDER ,usersSession.getString (KEY_GENDER ,null ) );
    userData.put ( KEY_PASSWORD,usersSession.getString ( KEY_PASSWORD,null ) );
    userData.put(KEY_IMAGE,usersSession.getString ( KEY_IMAGE,null ));

    return  userData;
}
public boolean checkLogin() {
    if (usersSession.getBoolean ( IS_LOGIN, false )) {
        return true;
    } else {
        return false;

    }
}

    public void logout()
    {
        editor.clear ();
        editor.commit ();
    }
}

