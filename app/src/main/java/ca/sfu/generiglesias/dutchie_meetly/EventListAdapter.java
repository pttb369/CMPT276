package ca.sfu.generiglesias.dutchie_meetly;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by David on 2015-03-07.
 */
public class EventListAdapter extends ArrayAdapter<Event> {
    private List<Event> events;

    public EventListAdapter(Context context, int resource, List<Event> objects) {
        super(context, resource, objects);
        events = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //make sure we have a view to work with if given null
        View itemView = convertView;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if(itemView == null) {
            itemView = inflater.inflate(R.layout.event_list_item, parent, false);
        }

        //find the event to work with
        Event currentEvent = events.get(position);

        //fill the view
        ImageView imageView = (ImageView)itemView.findViewById(R.id.event_icon);
        imageView.setImageResource(currentEvent.getEventIconId());

        TextView title = (TextView) itemView.findViewById(R.id.event_name);
        title.setText(currentEvent.getEventName());
        title.setTextColor(Color.BLACK);

        TextView duration = (TextView) itemView.findViewById(R.id.event_duration);
        duration.setText(currentEvent.getEventDuration());
        duration.setTextColor(Color.BLACK);

        TextView date = (TextView) itemView.findViewById(R.id.event_date);
        date.setText(currentEvent.getEventDate());
        date.setTextColor(Color.BLACK);


        return itemView;
        //return super.getView(position, convertView, parent);
    }

}
