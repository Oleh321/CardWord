package balychev.oleh.blch.cardword.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import balychev.oleh.blch.cardword.R;
import balychev.oleh.blch.cardword.activity.TodayRatePreviewActivity;
import balychev.oleh.blch.cardword.data.CardCursorLab;
import balychev.oleh.blch.cardword.database.sqlite.CardDatabaseController;
import balychev.oleh.blch.cardword.utils.StateCardVariant;

//  TODO интерфейс для альбомной ориентации
public class TodayRateFragment extends Fragment implements View.OnClickListener {

    private TextView mAmountTextView;
    private Button mTestButton;
    private Button mPreviewButton;

    private CardCursorLab mCursorLab;

    private boolean mNeedToUpdateData;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCursorLab = CardCursorLab.getInstance();
        if (savedInstanceState != null){
            mNeedToUpdateData = false;
        } else {
            mNeedToUpdateData = true;
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
        CardDatabaseController controller = new CardDatabaseController(getContext());
        mCursorLab.setCursor(controller.getAllCards(StateCardVariant.FOR_REPEAT));
    }

    private void showCardAmount(){
        mAmountTextView.setText(String.valueOf(mCursorLab.getCursor().getCount()));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mPreviewButton.getId()){
            Intent intent = new Intent(getActivity(), TodayRatePreviewActivity.class);
            startActivity(intent);
        }else if(v.getId() == mTestButton.getId()){

        }
    }
}
