package com.example.quizadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AddCatActivity extends AppCompatActivity {

    TextInputEditText catName;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    List<Category> categoryList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cat);

        catName = findViewById(R.id.addCatName);
    }

    public void addCatClick(View view) {

        final String catString = Objects.requireNonNull(catName.getText()).toString().trim();

        Map<String, Object> map = new ArrayMap<>();

        map.put("NAME", catString);
        map.put("SETS", 0);

        final String docId = firebaseFirestore.collection("QUIZ").document().getId();

        firebaseFirestore.collection("QUIZ").document(docId).set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Map<String, Object> catDoc = new ArrayMap<>();

                        catDoc.put("CAT" +String.valueOf(categoryList.size() + 1)+"_NAME", catString);
                        catDoc.put("CAT" +String.valueOf(categoryList.size() + 1)+"_ID", docId);
                        catDoc.put("COUNT", categoryList.size() + 1);

                        firebaseFirestore.collection("QUIZ").document("Categories")
                                .update(catDoc)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        categoryList.add(new Category(docId, catString, "0"));

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }
}