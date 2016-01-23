package me.yogeshwardan.gvcsmartdoor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.Arrays;

/**
 * Created by yogeshwardancharan on 23/1/16.
 */
public class DoorAdapter extends ParseQueryAdapter<ParseObject> {

    public DoorAdapter(Context context){
        // Use the QueryFactory to construct a PQA that will only show
        // doorName
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("UserDeviceRelation");
                query.whereEqualTo("userEmail",ParseUser.getCurrentUser().getEmail() );
                query.selectKeys(Arrays.asList("doorName"));
                return query;
            }
        });

    }

    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.door_info, null);
        }

        super.getItemView(object, v, parent);

        Button unlockButton = (Button)v.findViewById(R.id.unlock_button);
        unlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Unlock pressed!", Toast.LENGTH_SHORT).show();
                /*
                * 1.connect
                * 2.publish
                * 3.disconnect or wait until you receive on subscribed topic
                * */
            }
        });

        // Add the title view
        TextView titleTextView = (TextView) v.findViewById(R.id.door_name);
        titleTextView.setText(object.getString("doorName"));

        return v;
    }
}
