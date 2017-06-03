package com.ff.pp.translate.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ff.pp.translate.R;
import com.ff.pp.translate.bean.Record;
import com.ff.pp.translate.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by PP on 2017/6/2.
 */

public class RecordAdapter extends RecyclerView.Adapter {
    private static final String TAG = "RecordAdapter";
    List<Record> mData;
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Context mContext;

    public RecordAdapter(Context context, List<Record> mData) {
        this.mData = mData;
        mContext = context;
        Log.e(TAG, "initRecyclerView mData.size(): " + mData.size() + "," + mData.get(0).getContent());
    }

    @Override
    public Holder onCreateViewHolder(final ViewGroup parent, int viewType) {
//        Log.e(TAG, "onCreateViewHolder: ");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        final Holder holder = new Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Record record = mData.get(position);
        Holder realHolder= (Holder) holder;
        if (record.getPosition() == Constants.POSITION_UPPER) {
            realHolder.underLayout.setVisibility(View.GONE);
            realHolder.upperLayout.setVisibility(View.VISIBLE);
            realHolder.upperTime.setText(sf.format(new Date(record.getTime())));
            realHolder.upperContent.setText(record.getContent());
            if (!record.isInput()) {
                realHolder.upperTime.setVisibility(View.GONE);
                realHolder.circleUpper.setImageResource(R.drawable.america);
            }else {
                realHolder.upperTime.setVisibility(View.VISIBLE);
                realHolder.circleUpper.setImageResource(R.drawable.china);
            }
        } else {
            realHolder.underLayout.setVisibility(View.VISIBLE);
            realHolder.upperLayout.setVisibility(View.GONE);
            realHolder.underTime.setText(sf.format(new Date(record.getTime())));
            realHolder.underContent.setText(record.getContent());
            if (!record.isInput()) {
                realHolder.underTime.setVisibility(View.GONE);
                realHolder.circleUnder.setImageResource(R.drawable.china);
            }else {
                realHolder.underTime.setVisibility(View.VISIBLE);
                realHolder.circleUnder.setImageResource(R.drawable.america);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void add(Record record) {
        mData.add(record);
        notifyDataSetChanged();
    }
}

class Holder extends RecyclerView.ViewHolder {
    public LinearLayout underLayout;
    public LinearLayout upperLayout;
    public TextView underContent;
    public TextView upperContent;

    public TextView underTime;
    public TextView upperTime;

    public CircleImageView circleUpper, circleUnder;

    public Holder(View itemView) {
        super(itemView);
        underLayout = (LinearLayout) itemView.findViewById(R.id.record_under_layout);
        upperLayout = (LinearLayout) itemView.findViewById(R.id.record_upper_layout);

        underContent = (TextView) itemView.findViewById(R.id.textview_under_content);
        upperContent = (TextView) itemView.findViewById(R.id.textview_upper_content);

        underTime = (TextView) itemView.findViewById(R.id.textview_under_time);
        upperTime = (TextView) itemView.findViewById(R.id.textview_upper_time);

        circleUnder = (CircleImageView) itemView.findViewById(R.id.circle_under);
        circleUpper = (CircleImageView) itemView.findViewById(R.id.circle_upper);
    }
}
