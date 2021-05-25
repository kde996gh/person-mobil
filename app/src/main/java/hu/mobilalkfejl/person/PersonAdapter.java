package hu.mobilalkfejl.person;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hu.mobilalkfejl.person.model.Person;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    private ArrayList<Person> mPersonData;
    private Context mContext;

    public PersonAdapter(Context mContext, ArrayList<Person> personData) {
        this.mPersonData = personData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PersonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PersonAdapter.ViewHolder holder, int position) {
        Person currentPerson = mPersonData.get(position);
        holder.bindTo(currentPerson);
    }

    @Override
    public int getItemCount() {
        return mPersonData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mPersonName;
        private TextView mTargetCategory;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mPersonName = itemView.findViewById(R.id.personName);
            mTargetCategory = itemView.findViewById(R.id.targetCategory);

        }

        public void bindTo(Person currentPerson) {
            Person.HumanName humanName = currentPerson.getName();
            String concatName = humanName.getFamily() + " " + humanName.getGiven();
            mPersonName.setText(concatName);
            mTargetCategory.setText(currentPerson.getLink());

            itemView.findViewById(R.id.informations).setOnClickListener(view -> ((PersonListActivity) mContext).getPersonDatas(currentPerson));

        }
    }
}
