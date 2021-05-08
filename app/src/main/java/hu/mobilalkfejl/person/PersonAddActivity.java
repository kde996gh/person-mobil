package hu.mobilalkfejl.person;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;

import hu.mobilalkfejl.person.model.Person;

public class PersonAddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String LOG_TAG = PersonAddActivity.class.getName();

    EditText PersonNameEditText;
    EditText PhoneEditText;
    DatePicker BirthDateEditText;
    Spinner spinnerGender;
    EditText AddressEditText;
    Spinner spinnerActive;
    Spinner spinnerTargetCategory;


  //  private FirebaseFirestore mFirestore;

    private FireBaseCrud fbCrud = new FireBaseCrud();


    Person person;

    //spinneres téma
    String[] gender = {"Nő", "Férfi"};
    String[] targetCategory = {"Orvos", "Beteg", "Hozzátartozó"};
    String[] activeStatus = {"Igen", "Nem"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_add);

        this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);


    //    mFirestore = FirebaseFirestore.getInstance();

        PersonNameEditText = findViewById(R.id.PersonNameEditText);
        PhoneEditText = findViewById(R.id.PhoneEditText);
        BirthDateEditText = findViewById(R.id.BirthDateEditText);
        //  GenderEditText = findViewById(R.id.GenderEditText);
        AddressEditText = findViewById(R.id.AddressEditText);
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

        spinnerActive = (Spinner) findViewById(R.id.spinnerActive);
        spinnerActive.setOnItemSelectedListener(this);

        ArrayAdapter activeArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, activeStatus);
        activeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActive.setAdapter(activeArrayAdapter);


        Intent intent = getIntent();
        String jsonObject = intent.getStringExtra("personObjectInJson");
        if (intent.hasExtra("personObjectInJson")) {
            Gson gson = new Gson();
            person = gson.fromJson(jsonObject, Person.class);

            PersonNameEditText.setText(person.getName());
            PhoneEditText.setText(person.getPhoneNumber());
            spinnerGender.setSelection(getPositionFromArray(gender, person.getGender()));
            AddressEditText.setText(person.getAddress());
            spinnerTargetCategory.setSelection(getPositionFromArray(targetCategory, person.getLink()));

            String[] currentDate = stringToDateArray(person.getBirthDate());
            BirthDateEditText.init(Integer.parseInt(currentDate[0]), Integer.parseInt(currentDate[1]), Integer.parseInt(currentDate[2]), null);
        }


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
        String gender = spinnerGender.getSelectedItem().toString();
        String address = AddressEditText.getText().toString();
        boolean active = spinnerActive.getSelectedItem().toString().equals("Igen");
        String targetCategory = spinnerTargetCategory.getSelectedItem().toString();

        Person newPerson = new Person(name, phone, birthDate, gender, address, active, targetCategory);

        if (person != null) {
            fbCrud.updatePerson(person, newPerson);
        } else {
            fbCrud.addPerson(newPerson);

        }
        finish();
        this.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);

    }

    public void cancel(View view) {
        finish();
        this.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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