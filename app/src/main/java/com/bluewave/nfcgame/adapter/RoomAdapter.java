package com.bluewave.nfcgame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bluewave.nfcgame.R;
import com.bluewave.nfcgame.model.Room;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Developer on 2016-06-17.
 */
public class RoomAdapter extends ArrayAdapter<Room> {

    private LayoutInflater mInflater;
    private int mRes;

    public RoomAdapter(Context context, int resource) {
        super(context, resource);
        mInflater = LayoutInflater.from(context);
        mRes = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(mRes, parent, false);
            holder.bind(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Room room = getItem(position);
        holder.setView(room);

        return convertView;
    }

    class ViewHolder
    {
        @BindView(R.id.tv_room_name)
        TextView tvRoomName;

        @BindView(R.id.tv_count)
        TextView tvCount;

        void bind(View rootView)
        {
            ButterKnife.bind(this,rootView);
        }

        void setView(Room room)
        {
            tvRoomName.setText(room.name);
            tvCount.setText(room.getCountString());
        }
    }
}
