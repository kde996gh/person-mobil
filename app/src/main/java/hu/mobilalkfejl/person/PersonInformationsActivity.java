package hu.mobilalkfejl.person;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobScheduler;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import hu.mobilalkfejl.person.model.Person;

public class PersonInformationsActivity extends AppCompatActivity {

    private static final String LOG_TAG = PersonAddActivity.class.getName();

    private FireBaseCrud fbCrud = new FireBaseCrud();

    Gson gson;


    TextView PersonNameTextView;
   // TextView PhoneTextView;
    TextView BirthDateTextView;
    TextView GenderTextView;
    TextView AddressTextView;
    TextView activeTextView;
    TextView targetCategoryTextView;


    private NotificationHelper mNotificationHelper;
    private JobScheduler mJobScheduler;

    private FirebaseFirestore mFirestore;
    private CollectionReference mPersons;
    Person person;
    String personJsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_informations);
        setTitle("Adatlap");
        this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();
        PersonNameTextView = findViewById(R.id.PersonNameTextView);
      //  PhoneTextView = findViewById(R.id.PhoneTextView);
        BirthDateTextView = findViewById(R.id.BirthDateTextView);
        GenderTextView = findViewById(R.id.GenderTextView);
        AddressTextView = findViewById(R.id.AddressTextView);
        activeTextView = findViewById(R.id.activeTextView);
        targetCategoryTextView = findViewById(R.id.targetCategoryTextView);

        mFirestore = FirebaseFirestore.getInstance();
        mPersons = mFirestore.collection("Persons");

        Intent intent = getIntent();
        personJsonObject = intent.getStringExtra("personObjectInJson");
        gson = new Gson();

        person = gson.fromJson(personJsonObject, Person.class);
        checkBirthDay(this.person);
        mJobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        queryData();
    }

    public void setTextViewContent(Person person) {
        PersonNameTextView.setText(person.getName());
     //   PhoneTextView.setText(person.getPhoneNumber());
        BirthDateTextView.setText(person.getBirthDate());
        GenderTextView.setText(person.getGender());
        AddressTextView.setText(person.getAddress());
        activeTextView.setText(person.isActive() ? "Igen" : "Nem");
        targetCategoryTextView.setText(person.getLink());
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        queryData();
    }

    public void queryData() {
        fbCrud.getmFirestore().collection("Persons").document(person._getId())
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            Person person = queryDocumentSnapshots.toObject(Person.class);
            person.setId(queryDocumentSnapshots.getId());
            setTextViewContent(person);
            personJsonObject = gson.toJson(person);
            Log.i(LOG_TAG, "LOGTAG: " + person.getName());
        });
    }

    public void editPerson(View view) {
        Intent intent = new Intent(this, PersonAddActivity.class);
        intent.putExtra("personObjectInJson", personJsonObject);
        startActivity(intent);
    }

    public void checkBirthDay(Person person) {

        Date date = new Date();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        String[] bday = person.getBirthDate().split("-");
        Log.i(LOG_TAG, "LOGTAG:  " + cal + " " + "year: " + month + " , day: " + day);

        if (month == (Integer.parseInt(bday[1]) + 1) && day == Integer.parseInt(bday[2])) {
            mNotificationHelper = new NotificationHelper(this);

            Log.i(LOG_TAG, "Bejöttem az ifbe!");
            mNotificationHelper.send("Ma van a születésnapja: " + person.getName());
        }
    }

    public void deletePerson(View view) {
        fbCrud.deletePerson(person);
        finish();
        this.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }
}