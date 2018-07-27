package balychev.oleh.blch.cardword.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private Context mContext;
    private ArrayList<Card> mCards;

    public CardAdapter(Context context, ArrayList<Card> cards) {
        this.mContext = context;
        this.mCards = cards;
    }

    public void setCards(ArrayList<Card> cards) {
        mCards = cards;
    }

    public ArrayList<Card> getCards(){
        return mCards;
    }

    private void deleteCard(int cardPosition){
        CardDatabaseController controller = new CardDatabaseController(mContext);
        controller.deleteCard(mCards.get(cardPosition).getId());
        mCards.remove(cardPosition);
        controller.close();
        notifyDataSetChanged();
    }

    private void resetProgress(int position, ImageView imageView) {
        CardDatabaseController controller = new CardDatabaseController(mContext);
        controller.setCardState(mCards.get(position).getId(), 0);
        controller.close();
        setIcon(mCards.get(position).getState(), imageView);
        notifyDataSetChanged();
    }

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
    public void onBindViewHolder(@NonNull final CardViewHolder holder, final int position) {
        Card card = mCards.get(position);

        holder.mPosition = position;
        holder.mWordText.setText(card.getFrontSide());
        holder.mDefinitionText.setText(card.getBackSide());
        holder.mDateAddText.setText(card.getDateAdd());

        setIcon(card.getState(), holder.mProgressImage);

    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }


    public class CardViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener  {

        private int mPosition;

        private View mView;
        private TextView mWordText;
        private TextView mDefinitionText;
        private TextView mDateAddText;
        private ImageView mProgressImage;

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
            //groupId, itemId, order, title
            menu.setHeaderTitle("Выберете пункт:");
            MenuItem resetProgressItem = menu.add(0, 1, 0, "Сбросить прогресс");
            MenuItem deleteItem = menu.add(0, 2, 0, "Удалить карточку");
            resetProgressItem.setOnMenuItemClickListener(CardViewHolder.this);
            deleteItem.setOnMenuItemClickListener(CardViewHolder.this);



        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (1 == item.getItemId()){
                resetProgress(mPosition, mProgressImage);
                Toast.makeText(mContext, "Прогресс сброшен", Toast.LENGTH_SHORT).show();
                return true;
            }else if (2 == item.getItemId()){
                deleteCard(mPosition);
                Toast.makeText(mContext, "Слово удалено", Toast.LENGTH_SHORT).show();
                return true;
            }
          //  return this.onMenuItemClick(item);
            return false;
        }
    }
}
