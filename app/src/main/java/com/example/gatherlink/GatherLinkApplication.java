package com.example.gatherlink;
import android.app.Application;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
public class GatherLinkApplication extends Application {
 @Override public void onCreate(){super.onCreate();
  if(BuildConfig.FIREBASE_EMULATORS){
   FirebaseApp.getInstance().delete();
   FirebaseOptions options=new FirebaseOptions.Builder(FirebaseOptions.fromResource(this)).setProjectId("demo-gatherlink").build();
   FirebaseApp.initializeApp(this,options);
   FirebaseAuth.getInstance().useEmulator("10.0.2.2",9099);
   FirebaseFirestore.getInstance().useEmulator("10.0.2.2",8089);
  }
 }
}
