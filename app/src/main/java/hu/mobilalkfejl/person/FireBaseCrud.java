package hu.mobilalkfejl.person;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import hu.mobilalkfejl.person.model.Person;

public class FireBaseCrud {
    private static final String LOG_TAG = FireBaseCrud.class.getName();

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mPersons = mFirestore.collection("Persons");


    public void getPersons(ArrayList<Person> persons, PersonAdapter mAdapter) {
        mPersons.orderBy("name", Query.Direction.DESCENDING).limit(5).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Person person = document.toObject(Person.class);
                        person.setId(document.getId());
                        persons.add(person);
                    }
                    mAdapter.notifyDataSetChanged();
                });
    }

    public CollectionReference getmPersons() {
        return mPersons;
    }

    public FirebaseFirestore getmFirestore() {
        return mFirestore;
    }

    public void addPerson(Person person) {
        mPersons.add(person)
                .addOnSuccessListener(aVoid -> Log.d(LOG_TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w(LOG_TAG, "Error writing document", e));
    }

    public void updatePerson(Person oldData, Person newData) {
        mPersons.document(oldData._getId()).update(
                "active", newData.isActive(),
                "address", newData.getAddress(),
                "birthDate", newData.getBirthDate(),
                "gender", newData.getGender(),
                "name", newData.getName(),
             //   "phoneNumber", newData.getPhoneNumber(),
                "link", newData.getLink()
        ).addOnSuccessListener(aVoid -> Log.d(LOG_TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w(LOG_TAG, "Error writing document", e));
    }

    public void deletePerson(Person person) {
        mPersons.document(person._getId()).delete()
                .addOnSuccessListener(success -> {
                    Log.d(LOG_TAG, "Item is successfully deleted: " + person._getId());
                })
                .addOnFailureListener(fail -> {
                    Log.d(LOG_TAG, "Failed on delete: " + person._getId());
                });
    }


}
