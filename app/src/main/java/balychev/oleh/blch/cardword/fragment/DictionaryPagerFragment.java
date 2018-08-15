package balychev.oleh.blch.cardword.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import balychev.oleh.blch.cardword.R;
import balychev.oleh.blch.cardword.adapter.CardAdapter;
import balychev.oleh.blch.cardword.data.CardCursorLab;
import balychev.oleh.blch.cardword.database.sqlite.CardDatabaseController;
import balychev.oleh.blch.cardword.utils.StateCardVariant;

public class DictionaryPagerFragment extends Fragment {

    static final String SHOW_SEARCH = "show search";
    static final String SEARCH_OPTION = "search option";
    static final String PAGE_NUMBER = "page number";

    static final String ALL = "All";
    static final String CURRENT = "In progress";
    static final String LEARNED = "Learned";

    final static String[] pagesTitle = {ALL, CURRENT, LEARNED};

    static final int SEARCH_ITEM_ID = 1;
    static final int SEARCH_OPTION_ITEM_ID = 2;

    private MenuItem mSearchMenuItem;
    private MenuItem mSearchOptionMenuItem;

    private boolean mIsShowSearch;
    private int mCurrentPage;
    private int mSearchOption; // 0 - слово, 1 - обозначение

    private EditText mSearchEditText;
    private TabLayout mTabLayout;
    private ImageView mNotFoundImageView;
    private TextView mNotFoundTextView;
    private RecyclerView mRecyclerView;
    private ScrollView mScrollView;

    private CardAdapter mCardAdapter;

    private CardDatabaseController mController;

    private CardCursorLab mCursorLab;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SHOW_SEARCH, mIsShowSearch);
        outState.putInt(PAGE_NUMBER, mCurrentPage);
        outState.putInt(SEARCH_OPTION, mSearchOption);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mCurrentPage = 0;
            mSearchOption = 0;
            mIsShowSearch = false;
        } else {
            mCurrentPage = savedInstanceState.getInt(PAGE_NUMBER);
            mIsShowSearch = savedInstanceState.getBoolean(SHOW_SEARCH);
            mSearchOption = savedInstanceState.getInt(SEARCH_OPTION);
        }

        mController = new CardDatabaseController(getContext());
        mCursorLab = CardCursorLab.getInstance();
        mCursorLab.setCursor(mController.getAllCards(getVariant(mCurrentPage)));

        mCardAdapter = new CardAdapter(getContext(), mCursorLab.getCursor(), getVariant(mCurrentPage));
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary_pager, container, false);

        mSearchEditText = view.findViewById(R.id.fg_dictionary_pager_et_search);
        mTabLayout = view.findViewById(R.id.fg_dictionary_pager_tabs);
        mNotFoundImageView = view.findViewById(R.id.fg_dictionary_pager_image_not_found);
        mNotFoundTextView = view.findViewById(R.id.fg_dictionary_pager_text_not_found);
        mRecyclerView = view.findViewById(R.id.fg_dictionary_pager_recycler_view);
        mScrollView = view.findViewById(R.id.fg_dictionary_pager_scroll_view);

        for(String title : pagesTitle){
            mTabLayout.addTab(mTabLayout.newTab().setText(title));
        }

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mCurrentPage = tab.getPosition();
                refreshAdapter(mSearchEditText.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {  }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {  }

        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mCardAdapter);
        mRecyclerView.getAdapter().notifyDataSetChanged();

        mSearchEditText.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                refreshAdapter(s.toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        mSearchEditText.setHint("Поиск по слову");
        refreshAdapter("");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private StateCardVariant getVariant(int position){
        StateCardVariant variant = null;
        switch (position){
            case 0:
                variant = StateCardVariant.ALL;
                break;
            case 1:
                variant = StateCardVariant.IN_PROGRESS;
                break;
            case 2:
                variant = StateCardVariant.FINISHED;
                break;
        }
        return variant;
    }

     private void refreshAdapter(final String s){
        reload(s.trim().equals("")? null : s, getVariant(mCurrentPage));
        mCursorLab.setCursor(mController.getAllCards(getVariant(mCurrentPage)));
        mCardAdapter.changeCursor(mCursorLab.getCursor());
        mCardAdapter.setCurrentVariant(getVariant(mCurrentPage));
        mRecyclerView.getAdapter().notifyDataSetChanged();
        checkIfExist(mCursorLab.getCursor().getCount());
        mScrollView.smoothScrollTo(0,0);
    }

    private void reload(String word, StateCardVariant variant) {

        if (word == null) {
            mCursorLab.setCursor(mController.getAllCards(variant));
        } else {
            mCursorLab.setCursor(mController.searchCards(variant, word, mSearchOption));
        }

    }

    private void checkIfExist(int size){
        if(size == 0){
            mNotFoundImageView.setVisibility(View.VISIBLE);
            mNotFoundTextView.setVisibility(View.VISIBLE);
        } else {
            mNotFoundImageView.setVisibility(View.GONE);
            mNotFoundTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mSearchMenuItem = menu.add(Menu.NONE, SEARCH_ITEM_ID, Menu.NONE, "Показать поиск");
        mSearchMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        mSearchOptionMenuItem = menu.add(Menu.NONE, SEARCH_OPTION_ITEM_ID, Menu.NONE, "Поиск по обозначению");
        mSearchOptionMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == SEARCH_ITEM_ID){
           if(mIsShowSearch){
               mSearchMenuItem.setTitle("Показать поиск");
               mSearchEditText.setVisibility(View.GONE);
               mSearchEditText.setText("");
           } else {
               mSearchMenuItem.setTitle("Спрятать поиск");
               mSearchEditText.setVisibility(View.VISIBLE);
           }
            mIsShowSearch = !mIsShowSearch;
           return true;
        } else if(item.getItemId() == SEARCH_OPTION_ITEM_ID){
            if(mSearchOption == 0){
                mSearchOptionMenuItem.setTitle("Поиск по слову");
                mSearchEditText.setHint("Поиск по обозначению");
                mSearchOption = 1;
            } else {
                mSearchOptionMenuItem.setTitle("Поиск по обозначению");
                mSearchEditText.setHint("Поиск по слову");
                mSearchOption = 0;
            }
            refreshAdapter(mSearchEditText.getText().toString());
            return true;
        }
        mScrollView.smoothScrollTo(0,0);
        return super.onOptionsItemSelected(item);
    }

}
