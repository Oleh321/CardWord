package balychev.oleh.blch.cardword.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import balychev.oleh.blch.cardword.R;
import balychev.oleh.blch.cardword.data.CardCursorLab;
import balychev.oleh.blch.cardword.database.sqlite.CardDatabaseController;
import balychev.oleh.blch.cardword.model.Word;
import balychev.oleh.blch.cardword.utils.SystemUtils;

public class AddCardFragment extends Fragment {

    private EditText mWordEditText;
    private EditText mDefinitionEditText;

    private CardDatabaseController mController;

    private final String WORD = "word";
    private final String DEFINITION = "definition";

    private MenuItem mSaveMenuItem;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(WORD, mWordEditText.getText().toString());
        outState.putString(DEFINITION, mDefinitionEditText.getText().toString());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController = new CardDatabaseController(getContext());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_card, container, false);

        mWordEditText = view.findViewById(R.id.fg_add_card_front_side);
        mDefinitionEditText = view.findViewById(R.id.fg_add_card_back_side);

        if(savedInstanceState!=null) {
            mWordEditText.setText(savedInstanceState.getString(WORD));
            mDefinitionEditText.setText(savedInstanceState.getString(DEFINITION));
        }

        TextWatcher listener = new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                if (mSaveMenuItem != null)
                    controlSaveButton();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        };

        mWordEditText.addTextChangedListener(listener);
        mDefinitionEditText.addTextChangedListener(listener);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mSaveMenuItem = menu.add("save");
        mSaveMenuItem.setIcon(android.R.drawable.ic_menu_save);
        mSaveMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        controlSaveButton();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == mSaveMenuItem.getItemId()){
            SystemUtils.hideKeyBoard(getActivity());

            // TODO предоставить выбор пользователю
            Cursor cursor = mController.getCardByWord(mWordEditText.getText().toString().trim());
            if (cursor.getCount() != 0){
                Toast.makeText(getActivity(), "Такое слово уже есть.", Toast.LENGTH_SHORT).show();
                return true;
            }

            mController.addNewCard(new Word(
                mWordEditText.getText().toString().trim(),
                mDefinitionEditText.getText().toString().trim()
            ));
            mWordEditText.setText("");
            mDefinitionEditText.setText("");
            return true;
        }
        return false;
    }

    private void controlSaveButton(){
        if(!TextUtils.isEmpty(mWordEditText.getText().toString().trim())
                && !TextUtils.isEmpty(mDefinitionEditText.getText().toString().trim())){
            mSaveMenuItem.setVisible(true);
        } else {
            mSaveMenuItem.setVisible(false);
        }
    }

}
