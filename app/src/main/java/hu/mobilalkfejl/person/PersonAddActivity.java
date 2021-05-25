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

    EditText PersonFamilyNameEditText;
    EditText PersonGivenNameEditText;
    DatePicker BirthDateEditText;
    Spinner spinnerGender;
    Spinner spinnerActive;
    Spinner spinnerTargetCategory;

    private FireBaseCrud fbCrud = new FireBaseCrud();


    Person person;

    String[] gender = {"Nő", "Férfi", "Egyéb", "Ismeretlen"};
    String[] targetCategory = {"Orvos", "Beteg", "Hozzátartozó"};
    String[] activeStatus = {"Igen", "Nem"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_add);
        this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);

        PersonFamilyNameEditText = findViewById(R.id.PersonFamilyNameEditText);
        PersonGivenNameEditText = findViewById(R.id.PersonGivenNameEditText);
        BirthDateEditText = findViewById(R.id.BirthDateEditText);

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
            setTitle("Adatok módosítása");

            Gson gson = new Gson();
            person = gson.fromJson(jsonObject, Person.class);

            Person.HumanName humanName = person.getName();
            PersonFamilyNameEditText.setText(humanName.getFamily());
            PersonGivenNameEditText.setText(humanName.getGiven());
            spinnerGender.setSelection(getPositionFromArray(gender, person.getGender()));
            spinnerTargetCategory.setSelection(getPositionFromArray(targetCategory, person.getLink()));

            String[] currentDate = stringToDateArray(person.getBirthDate());
            BirthDateEditText.init(Integer.parseInt(currentDate[0]), Integer.parseInt(currentDate[1]), Integer.parseInt(currentDate[2]), null);
        } else {
            setTitle("Személy hozzáadása");
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

        Person.HumanName name = new Person.HumanName(PersonFamilyNameEditText.getText().toString(), PersonGivenNameEditText.getText().toString());
        String birthDate = createStringDateFromDatePicker(BirthDateEditText);
        String gender = spinnerGender.getSelectedItem().toString();
        boolean active = spinnerActive.getSelectedItem().toString().equals("Igen");
        String targetCategory = spinnerTargetCategory.getSelectedItem().toString();

        Person newPerson = new Person(name, birthDate, gender, active, targetCategory);

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