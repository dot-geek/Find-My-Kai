package com.positivestuff.findmykai.ui.home;

import android.content.Intent;
import android.os.Bundle;
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
    CheckBox tagCheckBox1, tagCheckBox2, tagCheckBox3, tagCheckBox4, tagCheckBox5, tagCheckBox6,
    tagCheckBox7, tagCheckBox8, tagCheckBox9, tagCheckBox10, tagCheckBox11, tagCheckBox12;
    List<String> tags = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        findmykaibutton = binding.findmykaibutton;
        findmykaibutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RecipeListActivity.class);
                addTagsFromCheckBox();
                intent.putStringArrayListExtra("tags", (ArrayList<String>) tags);
                startActivity(intent);

            }
        });


        tagCheckBox1 = binding.tagCheckBox1;
        tagCheckBox2 = binding.tagCheckBox2;
        tagCheckBox3 = binding.tagCheckBox3;
        tagCheckBox4 = binding.tagCheckBox4;
        tagCheckBox5 = binding.tagCheckBox5;
        tagCheckBox6 = binding.tagCheckBox6;
        tagCheckBox7 = binding.tagCheckBox7;
        tagCheckBox8 = binding.tagCheckBox8;
        tagCheckBox9 = binding.tagCheckBox9;
        tagCheckBox10 = binding.tagCheckBox10;
        tagCheckBox11 = binding.tagCheckBox11;
        tagCheckBox12 = binding.tagCheckBox12;

        return root;
    }

    private void addTagsFromCheckBox() {
        tags.clear();
        // Go through checkboxes
        if (tagCheckBox1.isChecked())
        {
            tags.add(getResources().getString(R.string.food_tag_1).toLowerCase());
        }
        if (tagCheckBox2.isChecked())
        {
            tags.add(getResources().getString(R.string.food_tag_2).toLowerCase());
        }
        if (tagCheckBox3.isChecked())
        {
            tags.add(getResources().getString(R.string.food_tag_3).toLowerCase());
        }
        if (tagCheckBox4.isChecked())
        {
            tags.add(getResources().getString(R.string.food_tag_4).toLowerCase());
        }
        if (tagCheckBox5.isChecked())
        {
            tags.add(getResources().getString(R.string.food_tag_5).toLowerCase());
        }
        if (tagCheckBox6.isChecked())
        {
            tags.add(getResources().getString(R.string.food_tag_6).toLowerCase());
        }
        if (tagCheckBox7.isChecked())
        {
            tags.add(getResources().getString(R.string.food_tag_7).toLowerCase());
        }
        if (tagCheckBox8.isChecked())
        {
            tags.add(getResources().getString(R.string.food_tag_8).toLowerCase());
        }
        if (tagCheckBox9.isChecked())
        {
            tags.add(getResources().getString(R.string.food_tag_9).toLowerCase());
        }
        if (tagCheckBox10.isChecked())
        {
            tags.add(getResources().getString(R.string.food_tag_10).toLowerCase());
        }
        if (tagCheckBox11.isChecked())
        {
            tags.add(getResources().getString(R.string.food_tag_11).toLowerCase());
        }
        if (tagCheckBox12.isChecked())
        {
            tags.add(getResources().getString(R.string.food_tag_12).toLowerCase());
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}