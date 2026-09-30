import {before, after, beforeEach, test} from 'node:test';
import {readFileSync} from 'node:fs';
import {initializeTestEnvironment, assertFails, assertSucceeds} from '@firebase/rules-unit-testing';
import {doc, setDoc, getDoc, updateDoc, deleteDoc, writeBatch} from 'firebase/firestore';
let env;
before(async () => { env = await initializeTestEnvironment({projectId:'demo-gatherlink',firestore:{host:'127.0.0.1',port:8089,rules:readFileSync('firestore.rules','utf8')}}); });
after(async () => { await env.cleanup(); });
beforeEach(async () => { await env.clearFirestore(); });
const db = uid => env.authenticatedContext(uid).firestore();
const profile = {userID:'a',firstName:'First',lastName:'Last',email:'a@example.test'};
test('profile is owner-only and never accepts password', async () => {
  await assertSucceeds(setDoc(doc(db('a'),'Users/a'),profile));
  await assertFails(getDoc(doc(db('b'),'Users/a')));
  await assertFails(updateDoc(doc(db('b'),'Users/a'),{firstName:'Hijack'}));
  await assertFails(setDoc(doc(db('a'),'Users/a'),{...profile,password:'plaintext'}));
  await assertSucceeds(updateDoc(doc(db('a'),'Users/a'),{firstName:'Updated'}));
});
test('unauthenticated data access denied', async () => {
  await assertFails(getDoc(doc(env.unauthenticatedContext().firestore(),'Groups/g')));
});
test('membership controls creation and post ownership controls changes', async () => {
  const a=db('a'), b=db('b'), c=db('c');
  await assertSucceeds(setDoc(doc(a,'Groups/g'),{ownerUid:'a',groupId:'g',groupName:'Group'}));
  const post={userId:'b',groupId:'g',message:'Hello',likes:0};
  await assertFails(setDoc(doc(b,'Groups/g/Posts/p'),post));
  await assertSucceeds(setDoc(doc(b,'GroupMemberships/b_g'),{userId:'b',groupId:'g'}));
  await assertFails(setDoc(doc(b,'GroupMemberships/random'),{userId:'b',groupId:'g'}));
  await assertSucceeds(setDoc(doc(b,'Groups/g/Posts/p'),post));
  await assertFails(updateDoc(doc(c,'Groups/g/Posts/p'),{message:'Hijack'}));
  await assertFails(deleteDoc(doc(a,'Groups/g/Posts/p')));
  await assertSucceeds(updateDoc(doc(b,'Groups/g/Posts/p'),{message:'Updated'}));
  await assertFails(updateDoc(doc(b,'Groups/g/Posts/p'),{userId:'c'}));
  await assertSucceeds(deleteDoc(doc(b,'Groups/g/Posts/p')));
});
test('group owner cannot be replaced and membership cannot target someone else', async () => {
  await assertSucceeds(setDoc(doc(db('a'),'Groups/g'),{ownerUid:'a',groupId:'g',groupName:'Group'}));
  await assertFails(updateDoc(doc(db('b'),'Groups/g'),{groupName:'Hijack'}));
  await assertFails(updateDoc(doc(db('a'),'Groups/g'),{ownerUid:'b'}));
  await assertFails(setDoc(doc(db('b'),'GroupMemberships/a_g'),{userId:'a',groupId:'g'}));
});

test('group and owner membership can be created atomically', async () => {
  const a=db('a');const batch=writeBatch(a);
  batch.set(doc(a,'Groups/new'),{ownerUid:'a',groupId:'new',groupName:'Group'});
  batch.set(doc(a,'GroupMemberships/a_new'),{userId:'a',groupId:'new'});
  await assertSucceeds(batch.commit());
});
