package balychev.oleh.blch.cardword.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import balychev.oleh.blch.cardword.R;
import balychev.oleh.blch.cardword.adapter.CardAdapter;
import balychev.oleh.blch.cardword.database.sqlite.CardDatabaseController;
import balychev.oleh.blch.cardword.model.Card;
import balychev.oleh.blch.cardword.utils.StateCardVariant;

public class DictionarySinglePageFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    static final String SAVE_PAGE_NUMBER = "save_page_number";

    private int mPageNumber;

    private ImageView mNotFoundImageView;
    private TextView mNotFoundTextView;
    private RecyclerView mRecyclerView;

    private CardAdapter mCardAdapter;

    private ArrayList<Card> mCards;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_PAGE_NUMBER, mPageNumber);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            mPageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        }else {
            mPageNumber = savedInstanceState.getInt(SAVE_PAGE_NUMBER);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary_single_page, container, false);

        mNotFoundImageView = view.findViewById(R.id.fg_dictionary_single_page_image_not_found);
        mNotFoundTextView = view.findViewById(R.id.fg_dictionary_single_page_text_not_found);
        mRecyclerView = view.findViewById(R.id.fg_dictionary_single_page_recycler_view);

        mCards = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mCardAdapter = new CardAdapter(getContext(), mCards);
        mRecyclerView.setAdapter(mCardAdapter);

        reload(null);
        return view;
    }

    public static DictionarySinglePageFragment newInstance(int position) {
        DictionarySinglePageFragment fragment = new DictionarySinglePageFragment();
        Bundle arg = new Bundle();
        arg.putInt(ARGUMENT_PAGE_NUMBER, position);
        fragment.setArguments(arg);
        return fragment;
    }

    public void reload(String word) {
        //Запрос к база данных
        StateCardVariant variant = null;
        switch (DictionaryPagerFragment.pagesTitle[mPageNumber]){
            case DictionaryPagerFragment.ALL:
                variant = StateCardVariant.ALL;
                break;
            case DictionaryPagerFragment.CURRENT:
                variant = StateCardVariant.IN_PROGRESS;
                break;
            case DictionaryPagerFragment.LEARNED:
                variant = StateCardVariant.FINISHED;
                break;
        }

        CardDatabaseController controller = new CardDatabaseController(getContext());
        if (word == null) {
            mCards = controller.getCards(variant);
        } else {
            mCards = controller.getCardsByWord(variant, word);
        }
        controller.close();


        mCardAdapter.setCards(mCards);
        mRecyclerView.setAdapter(mCardAdapter);
        mRecyclerView.getAdapter().notifyDataSetChanged();
        Log.d("myLogs", "reload " + variant + " size = " + mCards.size()  + " number = " + mPageNumber + " word = " + word);
        checkIfExist(mCards.size());
    }

    private void checkIfExist(int size){
        if(size == 0){
            mNotFoundImageView.setVisibility(View.VISIBLE);
            mNotFoundTextView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mNotFoundImageView.setVisibility(View.GONE);
            mNotFoundTextView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}
