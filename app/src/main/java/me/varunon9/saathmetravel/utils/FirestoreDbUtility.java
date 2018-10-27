package me.varunon9.saathmetravel.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;

import me.varunon9.saathmetravel.constants.AppConstants;

public class FirestoreDbUtility {

    private FirebaseFirestore db;
    private String TAG = "FirestoreDbUtility";

    public FirestoreDbUtility() {
        db = FirebaseFirestore.getInstance();
    }

    public void createOrMerge(final String collectionName, final String documentName,
                              Object object, final FirestoreDbOperationCallback callback) {
        db.collection(collectionName).document(documentName)
                .set(object, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "createOrMerge success: "
                                + collectionName
                                + " "
                                + documentName);
                        callback.onSuccess(null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "createOrMerge failure: "
                                + collectionName
                                + " "
                                + documentName);
                        callback.onFailure(null);
                    }
                });
    }

    public void update(final String collectionName, final String documentName,
                       final Map hashMap, final FirestoreDbOperationCallback callback) {
        db.collection(collectionName).document(documentName)
                .update(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "update success: "
                                + collectionName
                                + " "
                                + documentName
                                + " "
                                + hashMap.toString()
                        );
                        callback.onSuccess(null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "update failure: "
                                + collectionName
                                + " "
                                + documentName);
                        callback.onFailure(null);
                    }
                });
    }

    public void getOne(final String collectionName, final String documentName,
                       final FirestoreDbOperationCallback callback) {
        db.collection(collectionName).document(documentName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        callback.onSuccess(documentSnapshot);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure(null);
                    }
                });
    }

    // this is not generalized
    public void getNearbyTravellers(GeoPoint lesserGeoPoint, GeoPoint greaterGeoPoint,
                                    final FirestoreDbOperationCallback callback) {
        db.collection(AppConstants.Collections.USERS)
                .whereGreaterThan("location", lesserGeoPoint)
                .whereLessThan("location", greaterGeoPoint)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        callback.onSuccess(querySnapshot);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure(null);
                    }
                });
    }
}
