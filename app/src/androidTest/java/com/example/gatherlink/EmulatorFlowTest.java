package com.example.gatherlink;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.gatherlink.model.ProfileData;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.*;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;
@RunWith(AndroidJUnit4.class)
public class EmulatorFlowTest {
 @Test public void accountGroupMembershipAndPostFlow()throws Exception{
  assumeTrue("Run with -PfirebaseEmulators=true and local emulators",BuildConfig.FIREBASE_EMULATORS);
  FirebaseAuth auth=FirebaseAuth.getInstance();FirebaseFirestore db=FirebaseFirestore.getInstance();auth.signOut();
  String suffix=UUID.randomUUID().toString();String email="owner-"+suffix+"@example.test";
  Tasks.await(auth.createUserWithEmailAndPassword(email,"test-password-234"),20,TimeUnit.SECONDS);
  String owner=auth.getCurrentUser().getUid();
  Map<String,Object> profile=ProfileData.create(owner,"Demo","Owner",email);
  Tasks.await(db.collection("Users").document(owner).set(profile),20,TimeUnit.SECONDS);
  assertFalse(Tasks.await(db.collection("Users").document(owner).get(),20,TimeUnit.SECONDS).contains("password"));
  String gid="group-"+suffix;Map<String,Object> group=new HashMap<>();group.put("groupId",gid);group.put("ownerUid",owner);group.put("groupName","Study group");
  Tasks.await(db.collection("Groups").document(gid).set(group),20,TimeUnit.SECONDS);
  auth.signOut();Tasks.await(auth.createUserWithEmailAndPassword("member-"+suffix+"@example.test","test-password-234"),20,TimeUnit.SECONDS);String member=auth.getCurrentUser().getUid();
  try {Tasks.await(db.collection("Users").document(owner).get(com.google.firebase.firestore.Source.SERVER),20,TimeUnit.SECONDS);fail("Other profile must be private");}catch(java.util.concurrent.ExecutionException expected){assertNotNull(expected.getCause());}
  Map<String,Object> membership=new HashMap<>();membership.put("userId",member);membership.put("groupId",gid);
  Tasks.await(db.collection("GroupMemberships").document(member+"_"+gid).set(membership),20,TimeUnit.SECONDS);
  Map<String,Object> post=new HashMap<>();post.put("userId",member);post.put("groupId",gid);post.put("message","Hello from a member");post.put("likes",0);
  var reference=db.collection("Groups").document(gid).collection("Posts").document("post");
  Tasks.await(reference.set(post),20,TimeUnit.SECONDS);Tasks.await(reference.update("message","Edited message"),20,TimeUnit.SECONDS);
  assertEquals("Edited message",Tasks.await(reference.get(com.google.firebase.firestore.Source.SERVER),20,TimeUnit.SECONDS).getString("message"));
  auth.signOut();Tasks.await(auth.signInWithEmailAndPassword(email,"test-password-234"),20,TimeUnit.SECONDS);assertEquals(owner,auth.getCurrentUser().getUid());

 }
}
