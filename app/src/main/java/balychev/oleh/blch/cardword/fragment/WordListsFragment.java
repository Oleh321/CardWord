package balychev.oleh.blch.cardword.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import balychev.oleh.blch.cardword.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WordListsFragment extends Fragment {


    public WordListsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_word_lists, container, false);
    }

}
