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


    private RecyclerView mRecyclerView;
    private ArrayList<Person> mPersonsData;
    private PersonAdapter mAdapter;

    private FireBaseCrud fbCrud = new FireBaseCrud();


    private int gridNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getActionBar().setDisplayHomeAsUpEnabled(true);
   //     getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);

        setContentView(R.layout.activity_person_list);


        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new GridLayoutManager(
                this, gridNumber));

        mPersonsData = new ArrayList<>();

        mAdapter = new PersonAdapter(this, mPersonsData);
        mRecyclerView.setAdapter(mAdapter);

        queryData();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        queryData();
        Log.i(LOG_TAG, "onResume");
    }

    private void queryData() {
        mPersonsData.clear();
        fbCrud.getPersons(mPersonsData, mAdapter);
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