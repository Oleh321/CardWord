package balychev.oleh.blch.cardword.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import balychev.oleh.blch.cardword.R;
import balychev.oleh.blch.cardword.activity.TodayRatePreviewActivity;
import balychev.oleh.blch.cardword.database.sqlite.CardDatabaseController;
import balychev.oleh.blch.cardword.model.Card;
import balychev.oleh.blch.cardword.utils.StateCardVariant;

//  TODO интерфейс для альбомной ориентации
public class TodayRateFragment extends Fragment implements View.OnClickListener {

    private TextView mAmountTextView;
    private Button mTestButton;
    private Button mPreviewButton;

    private ArrayList<Card> mCards;

    private boolean mNeedToUpdateData;

    private static final String CARD_LIST = "card_list";

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(CARD_LIST, mCards);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            mNeedToUpdateData = false;
            mCards = savedInstanceState.getParcelableArrayList(CARD_LIST);
        } else {
            mNeedToUpdateData = true;
            mCards = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_rate, container, false);
        mAmountTextView = view.findViewById(R.id.fg_daily_rate_amount);
        mTestButton = view.findViewById(R.id.fg_daily_rate_btn_test);
        mPreviewButton = view.findViewById(R.id.fg_daily_rate_btn_preview);

        mPreviewButton.setOnClickListener(this);
        mTestButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mNeedToUpdateData){
            loadTodayRateCards();
        }
        showCardAmount();
    }

    private void loadTodayRateCards(){
        CardDatabaseController controller = new CardDatabaseController(getActivity());
        mCards = controller.getCards(StateCardVariant.FOR_REPEAT);
        controller.close();
    }

    private void showCardAmount(){
        mAmountTextView.setText(String.valueOf(mCards.size()));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mPreviewButton.getId()){
            Intent intent = new Intent(getActivity(), TodayRatePreviewActivity.class);
            intent.putExtra(TodayRatePreviewActivity.CARD_LIST_TRANSMIT, mCards);
            startActivity(intent);
        }else if(v.getId() == mTestButton.getId()){

        }
    }
}
