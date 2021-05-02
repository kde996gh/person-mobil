package hu.mobilalkfejl.person;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.Collection;

import hu.mobilalkfejl.person.model.Person;

public class PersonInformationsActivity extends AppCompatActivity {

    private static final String LOG_TAG = PersonAddActivity.class.getName();

    TextView PersonNameTextView;
    TextView PhoneTextView;
    TextView BirthDateTextView;
    TextView GenderTextView;
    TextView AddressTextView;
    TextView activeTextView;
    TextView targetCategoryTextView;

    private FirebaseFirestore mFirestore;
    private CollectionReference mPersons;

    Person person;
    String personJsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_informations);

        PersonNameTextView = findViewById(R.id.PersonNameTextView);
        PhoneTextView = findViewById(R.id.PhoneTextView);
        BirthDateTextView = findViewById(R.id.BirthDateTextView);
        GenderTextView = findViewById(R.id.GenderTextView);
        AddressTextView = findViewById(R.id.AddressTextView);
        activeTextView = findViewById(R.id.activeTextView);
        targetCategoryTextView = findViewById(R.id.targetCategoryTextView);


        mFirestore = FirebaseFirestore.getInstance();
        mPersons = mFirestore.collection("Persons");

        Intent intent = getIntent();
        personJsonObject = intent.getStringExtra("personObjectInJson");
        Gson gson = new Gson();

        person = gson.fromJson(personJsonObject, Person.class);

        setTextViewContent();


    }

    public void setTextViewContent(){
        PersonNameTextView.setText(person.getName());
        PhoneTextView.setText(person.getPhoneNumber());
        BirthDateTextView.setText(person.getBirthDate());
        GenderTextView.setText(person.getGender());
        AddressTextView.setText(person.getAddress());
        activeTextView.setText(person.isActive() ? "Igen" : "Nem");
        targetCategoryTextView.setText(person.getTargetCategory());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        Log.i(LOG_TAG, "onResume");
    }

    public void editPerson(View view) {
        Intent intent = new Intent(this, PersonAddActivity.class);
        intent.putExtra("personObjectInJson", personJsonObject);
        startActivity(intent);
    }

    public void deletePerson(View view) {

        DocumentReference ref = mPersons.document(person._getId());
        ref.delete()
                .addOnSuccessListener(success -> {
                    Log.d(LOG_TAG, "Item is successfully deleted: " + person._getId());
                })
                .addOnFailureListener(fail -> {
                    Toast.makeText(this, "Item " + person._getId() + " cannot be deleted.", Toast.LENGTH_LONG).show();
                });

        finish();
    }
}