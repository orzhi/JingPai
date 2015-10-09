package com.xcode.lockcapture.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xcode.lockcapture.R;
import com.xcode.lockcapture.common.IFragment;
import com.xcode.lockcapture.common.Utils;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutMe extends Fragment implements IFragment {


    public AboutMe() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_me, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView version = (TextView) view.findViewById(R.id.about_app_version);
        version.setText(String.format("程序版本: %s", Utils.getVersionName(getActivity())));
    }

    @Override
    public void OnEnter() {

    }
}
