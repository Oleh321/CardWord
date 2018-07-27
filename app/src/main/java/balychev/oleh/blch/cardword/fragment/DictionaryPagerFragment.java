package balychev.oleh.blch.cardword.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import balychev.oleh.blch.cardword.R;

public class DictionaryPagerFragment extends Fragment {

    static final String SHOW_SEARCH = "show search";

    static final String ALL = "All";
    static final String CURRENT = "In progress";
    static final String LEARNED = "Learned";
    final static String[] pagesTitle = {ALL, CURRENT, LEARNED};

    static final int SEARCH_ITEM_ID = 1;
    private boolean mIsShowSearch;
    private MenuItem mSearchMenuItem;
    private int mCurrentPage;

    private EditText mSearchEditText;
    private ViewPager mViewPager;

    private DictionaryPagerAdapter mAdapter;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SHOW_SEARCH, mIsShowSearch);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary_pager, container, false);

        mSearchEditText = view.findViewById(R.id.fg_dictionary_pager_et_search);
        mViewPager = view.findViewById(R.id.fg_dictionary_pager_view_pager);

        mAdapter =  new DictionaryPagerAdapter(getActivity().getSupportFragmentManager());
        mCurrentPage = 0;

        if (savedInstanceState == null) {
            mIsShowSearch = false;
        } else {
            mIsShowSearch = savedInstanceState.getBoolean(SHOW_SEARCH);
        }


        setHasOptionsMenu(true);

      //  adapter.mListFragments.get(mCurrentPage).reload(null);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mSearchEditText.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                mAdapter.mListFragments.get(mCurrentPage).reload(TextUtils.isEmpty(s.toString().trim())? null : s.toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        mViewPager.setCurrentItem(mCurrentPage, true);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mCurrentPage = position;
                mAdapter.mListFragments.get(mCurrentPage).reload(mIsShowSearch?
                        mSearchEditText.getText().toString()
                        :null);
            }
        });
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mSearchMenuItem = menu.add(Menu.NONE, SEARCH_ITEM_ID, Menu.NONE, "show search");
        mSearchMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        mIsShowSearch = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == SEARCH_ITEM_ID){
           if(mIsShowSearch){
               mSearchMenuItem.setTitle("show search");
               mSearchEditText.setVisibility(View.GONE);
               mSearchEditText.setText("");
           } else {
               // Открываем
               mSearchMenuItem.setTitle("hide search");
               mSearchEditText.setVisibility(View.VISIBLE);
           }
            mIsShowSearch = !mIsShowSearch;
           return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DictionaryPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<DictionarySinglePageFragment> mListFragments;


        public DictionaryPagerAdapter(FragmentManager fm) {
            super(fm);
            mListFragments = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            DictionarySinglePageFragment fragment = DictionarySinglePageFragment.newInstance(position);
            mListFragments.add(fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            return pagesTitle.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pagesTitle[position];
        }
    }


}
