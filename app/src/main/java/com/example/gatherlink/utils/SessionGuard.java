package com.example.gatherlink.utils;
import android.app.Activity;
import android.content.Intent;
import com.example.gatherlink.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
public final class SessionGuard {
 private SessionGuard(){}
 public static boolean require(Activity activity){
  if(FirebaseAuth.getInstance().getCurrentUser()!=null)return true;
  Intent intent=new Intent(activity,LoginActivity.class);intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);activity.startActivity(intent);activity.finish();return false;
 }
}
