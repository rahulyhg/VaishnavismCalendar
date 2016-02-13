package com.nirmalam.vaishnavismeclass.vaishnavismcalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by spillaip on 12/31/2015.
 */
public class CustomAdapter extends BaseAdapter {
    String[] result;
    String mDate;
    String mHighlightTa;
    String mStarTa;
    String mTamilDayTa;
    String mTamilMonthTa;
    String mThithiTa;
    Context context;
    private static LayoutInflater inflater=null;

    public CustomAdapter(MainActivity mainActivity, ArrayList<String> calList) {
        // TODO Auto-generated constructor stub
        result=calList.toArray(new String[calList.size()]);
        context=mainActivity;


        //assign values
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView tvDate;
        TextView tvHighlightTa;
        TextView tvStarTa;
        TextView tvTamilDayTa;
        TextView tvTamilMonthTa;
        TextView tvThithiTa;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.listline, null);
        String[] valString = result[position].split(";");

        String xDate=valString[0];
        DateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        try {
            Date date = format.parse(xDate);
            SimpleDateFormat ft =
                    new SimpleDateFormat ("E dd MMM yyyy");
            mDate = ft.format(date); //date.toString();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        mHighlightTa=valString[1];
        mStarTa=valString[2];
        mTamilDayTa=valString[3];
        mTamilMonthTa=valString[4];
        mThithiTa=valString[5];

        holder.tvDate=(TextView) rowView.findViewById(R.id.tvDate);
        holder.tvDate.setText(mDate);

        holder.tvHighlightTa=(TextView) rowView.findViewById(R.id.tvHighlightTa);
        holder.tvHighlightTa.setText(mHighlightTa);

        holder.tvStarTa=(TextView) rowView.findViewById(R.id.tvStarTa);
        holder.tvStarTa.setText(mStarTa);

        holder.tvTamilDayTa=(TextView) rowView.findViewById(R.id.tvTamilDayTa);
        holder.tvTamilDayTa.setText(mTamilDayTa);

        holder.tvTamilMonthTa=(TextView) rowView.findViewById(R.id.tvTamilMonthTa);
        holder.tvTamilMonthTa.setText(mTamilMonthTa);

        holder.tvThithiTa=(TextView) rowView.findViewById(R.id.tvThithiTa);
        holder.tvThithiTa.setText(mThithiTa);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked " + position, Toast.LENGTH_LONG).show();
            }
        });
        return rowView;    }

}
