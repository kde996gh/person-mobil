package hu.mobilalkfejl.person;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import hu.mobilalkfejl.person.model.Person;

public class PersonAddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String LOG_TAG = PersonAddActivity.class.getName();

    EditText PersonNameEditText;
    EditText PhoneEditText;
    DatePicker BirthDateEditText;
    Spinner spinnerGender;
    //    EditText GenderEditText;
    EditText AddressEditText;
    EditText LanguageEditText;
    Spinner spinnerTargetCategory;


    private FirebaseFirestore mFirestore;
    // private String id;

    Person person;

    //spinneres téma
    String[] gender = {"Nő", "Férfi"};
    String[] targetCategory = {"Orvos", "Beteg", "Hozzátartozó"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_add);

        mFirestore = FirebaseFirestore.getInstance();

        PersonNameEditText = findViewById(R.id.PersonNameEditText);
        PhoneEditText = findViewById(R.id.PhoneEditText);
        BirthDateEditText = findViewById(R.id.BirthDateEditText);
        //  GenderEditText = findViewById(R.id.GenderEditText);
        AddressEditText = findViewById(R.id.AddressEditText);
        LanguageEditText = findViewById(R.id.LanguageEditText);
        /// CategoryEditText = findViewById(R.id.CategoryEditText);

        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
        spinnerGender.setOnItemSelectedListener(this);

        ArrayAdapter genderArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, gender);
        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderArrayAdapter);

        spinnerTargetCategory = (Spinner) findViewById(R.id.spinnerTargetCategory);
        spinnerTargetCategory.setOnItemSelectedListener(this);

        ArrayAdapter targetCategoryArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, targetCategory);
        targetCategoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTargetCategory.setAdapter(targetCategoryArrayAdapter);


        Intent intent = getIntent();
        String jsonObject = intent.getStringExtra("personObjectInJson");
        if (intent.hasExtra("personObjectInJson")) {
            Gson gson = new Gson();
            person = gson.fromJson(jsonObject, Person.class);

            PersonNameEditText.setText(person.getName());
            PhoneEditText.setText(person.getPhoneNumber());
//            BirthDateEditText.setText(person.getBirthDate());
            //  GenderEditText.setText(person.getGender());
            spinnerGender.setSelection(getPositionFromArray(gender, person.getGender()));
            AddressEditText.setText(person.getAddress());
            LanguageEditText.setText(person.getLanguage());
            spinnerTargetCategory.setSelection(getPositionFromArray(targetCategory, person.getTargetCategory()));
            //  CategoryEditText.setText(person.getTargetCategory());

            String[] currentDate = stringToDateArray(person.getBirthDate());
            BirthDateEditText.init(Integer.parseInt(currentDate[0]), Integer.parseInt(currentDate[1]), Integer.parseInt(currentDate[2]), null);
        }

        //spinneres dolog

    }


    public int getPositionFromArray(String[] array, String type) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(type))
                return i;
        }
        return 0;
    }

    public void addPerson(View view) {


        String name = PersonNameEditText.getText().toString();
        String phone = PhoneEditText.getText().toString();
        String birthDate = createStringDateFromDatePicker(BirthDateEditText);
        //String gender = GenderEditText.getText().toString();
        String gender = spinnerGender.getSelectedItem().toString();
        String address = AddressEditText.getText().toString();
        String language = LanguageEditText.getText().toString();
        // String targetCategory = CategoryEditText.getText().toString();
        String targetCategory = spinnerTargetCategory.getSelectedItem().toString();

        Person newPerson = new Person(name, phone, birthDate, gender, address, language, targetCategory);

        if (person != null) {
            mFirestore.collection("Persons").document(person._getId())
                    .set(newPerson).addOnSuccessListener(aVoid -> Log.d(LOG_TAG, "DocumentSnapshot successfully written!"))
                    .addOnFailureListener(e -> Log.w(LOG_TAG, "Error writing document", e));
        } else {
            mFirestore.collection("Persons").add(newPerson)
                    .addOnSuccessListener(aVoid -> Log.d(LOG_TAG, "DocumentSnapshot successfully written!"))
                    .addOnFailureListener(e -> Log.w(LOG_TAG, "Error writing document", e));
            finish();
        }
    }

    public void cancel(View view) {
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //  Toast.makeText(getApplicationContext(), gender[position], Toast.LENGTH_LONG).show();
    }

    public String createStringDateFromDatePicker(DatePicker dp) {
        return dp.getYear() + "-" + dp.getMonth() + "-" + dp.getDayOfMonth();
    }

    public String[] stringToDateArray(String date) {
        return date.split("-");
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}