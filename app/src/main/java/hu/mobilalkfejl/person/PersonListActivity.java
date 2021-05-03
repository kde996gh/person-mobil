package hu.mobilalkfejl.person;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;

import hu.mobilalkfejl.person.model.Person;

public class PersonListActivity extends AppCompatActivity {

    private static final String LOG_TAG = PersonListActivity.class.getName();

    private FirebaseFirestore mFirestore;
    private CollectionReference mPersons;

    private RecyclerView mRecyclerView;
    private ArrayList<Person> mPersonsData;
    private PersonAdapter mAdapter;


    private int gridNumber = 1;
    private Integer itemLimit = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);

        this.overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);

        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new GridLayoutManager(
                this, gridNumber));

        mPersonsData = new ArrayList<>();

        mAdapter = new PersonAdapter(this, mPersonsData);
        mRecyclerView.setAdapter(mAdapter);


        mFirestore = FirebaseFirestore.getInstance();
        mPersons = mFirestore.collection("Persons");
        queryData();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        queryData();
        Log.i(LOG_TAG, "onResume");
    }

    private void queryData() {
        mPersonsData.clear();
        mPersons.orderBy("name", Query.Direction.DESCENDING).limit(itemLimit).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Person person = document.toObject(Person.class);
                        person.setId(document.getId());
                        mPersonsData.add(person);
                    }

                    // Notify the adapter of the change.
                    mAdapter.notifyDataSetChanged();
                });
    }


    public void deleteItem(Person currentPerson) {

        DocumentReference ref = mPersons.document(currentPerson._getId());
        ref.delete()
                .addOnSuccessListener(success -> {
                    Log.d(LOG_TAG, "Item is successfully deleted: " + currentPerson._getId());
                })
                .addOnFailureListener(fail -> {
                    Toast.makeText(this, "Item " + currentPerson._getId() + " cannot be deleted.", Toast.LENGTH_LONG).show();
                });

        queryData();
        // mNotificationHelper.cancel();
    }


    public void addNewPerson(View view) {
        Intent intent = new Intent(this, PersonAddActivity.class);
        startActivity(intent);
    }


    public void getPersonDatas(Person currentPerson) {
        Gson gson = new Gson();
        String personToJson = gson.toJson(currentPerson);

        Intent intent = new Intent(this, PersonInformationsActivity.class);
        intent.putExtra("personObjectInJson", personToJson);
        startActivity(intent);


    }
}