package balychev.oleh.blch.cardword.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.zip.Inflater;

import balychev.oleh.blch.cardword.R;
import balychev.oleh.blch.cardword.database.sqlite.CardDatabaseController;
import balychev.oleh.blch.cardword.model.Card;
import balychev.oleh.blch.cardword.utils.StateCardVariant;


public class CardAdapter extends AbstractCursorRecyclerViewAdapter<CardAdapter.CardViewHolder>{

    private StateCardVariant mCurrentVariant;

    public void setCurrentVariant(StateCardVariant currentVariant) {
        mCurrentVariant = currentVariant;
    }

    public CardAdapter(Context context, Cursor cursor, StateCardVariant variant) {
        super(context, cursor);
        this.mCurrentVariant = variant;
    }

    /*  @SuppressLint("StaticFieldLeak")
    private void deleteCard(int cardPosition){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                CardDatabaseController controller = new CardDatabaseController(mContext);
               // mCursor.moveToPosition(cardPosition);
                controller.deleteCard(getItemId(cardPosition));
                controller.close();
                return null;
            }
        };
    }

    @SuppressLint("StaticFieldLeak")
    private void resetProgress(int position, ImageView imageView) {
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                CardDatabaseController controller = new CardDatabaseController(mContext);
                controller.resetProgress(mCards.get(position).getId());
                controller.close();
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mCards.get(position).setState(0);
                setIcon(mCards.get(position).getState(), imageView);
            }
        };
    }

    @SuppressLint("StaticFieldLeak")
    private void markAsLearned(int position, ImageView imageView){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                CardDatabaseController controller = new CardDatabaseController(mContext);
                controller.markAsLearned(mCards.get(position).getId());
                controller.close();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mCards.get(position).setState(null);
                setIcon(mCards.get(position).getState(), imageView);
            }
        };
    }
*/
    private void setIcon(Integer state, ImageView imageView){
        Drawable drawable = null;

        if(state == null){
            drawable = mContext.getResources().getDrawable(R.drawable.check_mark);
        }else {
            switch (state){
                case 0:
                    drawable = mContext.getResources().getDrawable(R.drawable.first_state);
                    break;
                case 1:
                    drawable = mContext.getResources().getDrawable(R.drawable.second_state);
                    break;
                case 2:
                    drawable = mContext.getResources().getDrawable(R.drawable.third_state);
                    break;
                case 3:
                    drawable = mContext.getResources().getDrawable(R.drawable.fourth_state);
                    break;
                case 4:
                    drawable = mContext.getResources().getDrawable(R.drawable.fifth_state);
                    break;
                default:
                    imageView.setVisibility(View.GONE);
                    return;
            }
        }
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageDrawable(drawable);
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_card_layout, parent, false);
        return new CardViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CardViewHolder viewHolder, int position, Cursor cursor) {
        Card card =Card.parseCursor(cursor);
        viewHolder.mView.setVisibility(View.VISIBLE);
        viewHolder.mPosition = position;
        viewHolder.mWordText.setText(card.getFrontSide());
        viewHolder.mDefinitionText.setText(card.getBackSide());
        viewHolder.mDateAddText.setText(Card.getDateInFormat(card.getDateAdd()));

        setIcon(card.getState(), viewHolder.mProgressImage);
    }


    public class CardViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener /*, MenuItem.OnMenuItemClickListener */ {

        private int mPosition;

        private View mView;
        private TextView mWordText;
        private TextView mDefinitionText;
        private TextView mDateAddText;
        private ImageView mProgressImage;

        private static final int RESET_ITEM_ID = 1;
        private static final int DELETE_ITEM_ID = 2;
        private static final int LEARNED_ITEM_ID = 3;

        public CardViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mWordText = itemView.findViewById(R.id.single_card_layout_text_word);
            mDefinitionText = itemView.findViewById(R.id.single_card_layout_text_definition);
            mDateAddText = itemView.findViewById(R.id.single_card_layout_text_date);
            mProgressImage = itemView.findViewById(R.id.single_card_layout_img_progress);
            mView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Выберете пункт:");
            MenuItem resetProgressItem = menu.add(0, RESET_ITEM_ID, 0, "Сбросить прогресс");
         //   resetProgressItem.setOnMenuItemClickListener(CardViewHolder.this);
            MenuItem deleteItem = menu.add(0, DELETE_ITEM_ID, 0, "Удалить карточку");
       //     deleteItem.setOnMenuItemClickListener(CardViewHolder.this);

            if(mCurrentVariant != StateCardVariant.FINISHED) {
                MenuItem markAsLearnedItem = menu.add(0, LEARNED_ITEM_ID, 0, "Пометить как выученное");
        //        markAsLearnedItem.setOnMenuItemClickListener(CardViewHolder.this);
            }
        }

     /*   private void reset(MenuItem item, boolean needToDelete){
            if (RESET_ITEM_ID == item.getItemId()) {
            //    resetProgress(mPosition, mProgressImage);
                Toast.makeText(mContext, "Прогресс сброшен", Toast.LENGTH_SHORT).show();
                if (needToDelete) {
                //    mCards.remove(mPosition);
                    notifyItemRemoved(mPosition);
                    notifyDataSetChanged();
                  //  mView.setVisibility(View.GONE);

                } else {
                    notifyItemChanged(mPosition);
                }
            }
        }

        private void mark(MenuItem item, boolean needToDelete){
            if (LEARNED_ITEM_ID == item.getItemId()) {
                markAsLearned(mPosition, mProgressImage);
                Toast.makeText(mContext, "Помечено как выученное", Toast.LENGTH_SHORT).show();
                if (needToDelete) {
                   mCards.remove(mPosition);
                    notifyDataSetChanged();
               //     notifyItemRemoved(mPosition);
                   // mView.setVisibility(View.GONE);
                }else {
                    notifyItemChanged(mPosition);
                }
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            // Удалить слово (в любом случае одинаково)
            if (DELETE_ITEM_ID == item.getItemId()) {
                deleteCard(mPosition);
                Toast.makeText(mContext, "Слово удалено", Toast.LENGTH_SHORT).show();
               // notifyItemRemoved(mPosition);
                mCards.remove(mPosition);
                notifyDataSetChanged();
                //mView.setVisibility(View.GONE);
                //notifyItemChanged(mPosition);
                return true;
            }

            switch (mCurrentVariant) {
                case ALL:
                    reset(item, false);
                    mark(item, false);
                    break;
                case IN_PROGRESS:
                    reset(item, false);
                    mark(item, true);
                    break;
                case FINISHED:
                    reset(item, true);
                    break;
                case FOR_REPEAT:
                    reset(item, false);
                    mark(item, true);
                    break;
            }

            return false;
        }*/

    }
}
