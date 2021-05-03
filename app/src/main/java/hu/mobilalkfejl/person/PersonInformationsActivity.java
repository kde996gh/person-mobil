package hu.mobilalkfejl.person;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

    private NotificationHelper mNotificationHelper;
    private JobScheduler mJobScheduler;



    Person person;
    String personJsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_informations);

        this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();
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
        checkBirthDay(this.person);
        mJobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
      //  setJobScheduler();


        setTextViewContent(person);
    }

    public void setTextViewContent(Person person) {
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
        queryData();
        Log.i(LOG_TAG, "onRestart a personInofrmationsben!");
    }

    public void queryData() {
        mFirestore.collection("Persons").document(person._getId()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            Person person = queryDocumentSnapshots.toObject(Person.class);
            person.setId(queryDocumentSnapshots.getId());
            setTextViewContent(person);
            Log.i(LOG_TAG, "LOGTAG: " + person.getName());
        });
    }


    public void editPerson(View view) {
        Intent intent = new Intent(this, PersonAddActivity.class);
        intent.putExtra("personObjectInJson", personJsonObject);
        startActivity(intent);
    }

    public void checkBirthDay(Person person){

        Date date = new Date(); // your date
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        String[] bday = person.getBirthDate().split("-");
        Log.i(LOG_TAG, "LOGTAG:  " + cal + " " + "year: " + month + " , day: "+ day);

        if(month == Integer.parseInt(bday[1]) && day == Integer.parseInt(bday[2])){
            mNotificationHelper = new NotificationHelper(this);

             Log.i(LOG_TAG, "Bejöttem az ifbe!");
            mNotificationHelper.send("Ma van a születésnapja: " + person.getName());


        }



    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void setJobScheduler() {
//        // SeekBar, Switch, RadioButton
//        int networkType = JobInfo.NETWORK_TYPE_UNMETERED;
//        Boolean isDeviceCharging = true;
//        int hardDeadline = 5000; // 5 * 1000 ms = 5 sec.
//
//        ComponentName serviceName = new ComponentName(getPackageName(), NotificationJobService.class.getName());
//        JobInfo.Builder builder = new JobInfo.Builder(0, serviceName)
//                .setRequiredNetworkType(networkType)
//                .setRequiresCharging(isDeviceCharging)
//                .setOverrideDeadline(hardDeadline);
//
//        JobInfo jobInfo = builder.build();
//        mJobScheduler.schedule(jobInfo);
//
//        // mJobScheduler.cancel(0);
//        // mJobScheduler.cancelAll();
//
//    }

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
        this.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }
}