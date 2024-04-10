package com.positivestuff.findmykai.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.positivestuff.findmykai.R;
import com.positivestuff.findmykai.RecipeListActivity;
import com.positivestuff.findmykai.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    Button findmykaibutton;
    HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        findmykaibutton = binding.findmykaibutton;
        findmykaibutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RecipeListActivity.class);
                addTagsFromCheckBox();
                intent.putStringArrayListExtra("tags", (ArrayList<String>) homeViewModel.getTags());
                startActivity(intent);
            }
        });

        return root;
    }

    private void addTagsFromCheckBox() {
        homeViewModel.clearTags();
        // Go through checkboxes
        for (int i = 0; i < 12; i++){
            int resID = getResources().getIdentifier("tagCheckBox"+(i+1), "id", getActivity().getPackageName());
            CheckBox checkBox = (CheckBox)getView().findViewById(resID);
            if (checkBox.isChecked())
            {
                String tag = (String)checkBox.getText();
                homeViewModel.addTag(tag.toLowerCase());
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}