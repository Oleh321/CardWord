package balychev.oleh.blch.cardword.activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import balychev.oleh.blch.cardword.R;
import balychev.oleh.blch.cardword.adapter.CardAdapter;
import balychev.oleh.blch.cardword.data.CardCursorLab;
import balychev.oleh.blch.cardword.model.Card;
import balychev.oleh.blch.cardword.utils.StateCardVariant;

public class TodayRatePreviewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private static final String CARD_LIST = "card_list";
    public static final String CARD_LIST_TRANSMIT = "transmit_card_list";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_rate_preview);

        Cursor cursor = CardCursorLab.getInstance().getCursor();

        mRecyclerView = findViewById(R.id.act_daily_rate_preview_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        CardAdapter adapter = new CardAdapter(this, cursor, StateCardVariant.FOR_REPEAT);
        mRecyclerView.setAdapter(adapter);

    }

}
