package com.skullzbones.novelnation.ui.featured;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.skullzbones.novelnation.R;


public class FeaturedFragment extends Fragment {

    private FeaturedViewModel mViewModel;
    private Button mb1;
    private Button mb2;
    public static FeaturedFragment newInstance() {
        return new FeaturedFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_featured, container, false);
        mb1 = v.findViewById(R.id.button);
        mb2 = v.findViewById(R.id.button1);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FeaturedViewModel.class);
        // TODO: Use the ViewModel

        mb1.setOnClickListener(v -> {
            String url = "https://paypal.me/hsk4pay";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        mb2.setOnClickListener(v -> {
            Intent intent=null;
            String url = "https://www.youtube.com/c/AgentOrangeYT";
            try {
                intent =new Intent(Intent.ACTION_VIEW);
                intent.setPackage("com.google.android.youtube");
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

}