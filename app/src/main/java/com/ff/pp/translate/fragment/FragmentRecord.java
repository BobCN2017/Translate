package com.ff.pp.translate.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ff.pp.translate.R;
import com.ff.pp.translate.bean.Record;
import com.ff.pp.translate.utils.Constants;
import com.ff.pp.translate.utils.PreferencesUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PP on 2017/6/2.
 */

public class FragmentRecord extends Fragment {
    private static final String TAG = "FragmentRecord";
    private List<Record> mData;
    private RecordAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        initRecyclerView(view);
        return view;
    }

    private void initRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_list);
        mData = new ArrayList<>();

        readDiskDataToList(mData);
        mAdapter = new RecordAdapter(getContext(), mData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        scrollToBottom(mRecyclerView);
    }

    private void scrollToBottom(RecyclerView recyclerView) {
        if (mData != null)
            recyclerView.scrollToPosition(mData.size() - 1);
    }

    private void readDiskDataToList(List<Record> mData) {
        mData.clear();
        List<Record> list = new Gson().fromJson(PreferencesUtil.getString(Constants.RECORD_NAME),
                new TypeToken<List<Record>>() {
                }.getType());
        if (list != null)
            mData.addAll(list);
    }

    public void update() {
        readDiskDataToList(mData);
        mAdapter.notifyDataSetChanged();
        scrollToBottom(mRecyclerView);
    }
}
