package com.h.itservices.ui.QustAns_Page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.h.itservices.R;

public class qansewrPageFragment extends Fragment {

    private qansewrPageViewModel qansewrPageViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        qansewrPageViewModel =
                ViewModelProviders.of(this).get(qansewrPageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_qanswer, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        qansewrPageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}