package com.humanity.vs.cards.cardsvshumanity.ui.adapters;

import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.humanity.vs.cards.cardsvshumanity.R;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robot on 21.11.15.
 */
public class WhiteCardsAdapter extends RecyclerView.Adapter<WhiteCardsAdapter.WhiteCardsViewHolder> {

    private Activity activity;
    private List<JsonCard> items;
    private WhiteCardsAdapter_CardsSelectedCallback allSelectedCallback;
    private int cardsToSelectCount;

    private ArrayList<Integer> selectedCardsIds = new ArrayList<>();

    private static float defaultElevation = -1;
    private static final float floatingElevation = 6; // guideline is 8, but render is invalid

    public WhiteCardsAdapter(Activity activity, List<JsonCard> cards, WhiteCardsAdapter_CardsSelectedCallback allSelectedCallback, int cardsToSelectCount) {
        this.activity = activity;
        this.items = cards;
        this.allSelectedCallback = allSelectedCallback;
        this.cardsToSelectCount = cardsToSelectCount;
    }

    @Override
    public WhiteCardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_white_card, parent, false);

        return new WhiteCardsViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(final WhiteCardsViewHolder holder, int position) {
        final JsonCard item = items.get(position);

        // get default elevation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (defaultElevation == -1)
                defaultElevation = holder.cvGameCard.getElevation();
        }

        // set id tag
        holder.cvGameCard.setTag(item.id);

        // click handler for selection
        holder.cvGameCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleWhiteCardClick(item);
            }
        });

        // visual update
        boolean isCardSelected = false;

        int order = 1;
        holder.tvCardText.setText(item.text);
        for (Integer id : selectedCardsIds) {
            if (id.equals(item.id)) {
                String text = String.format(activity.getString(R.string.dynamic_selected_card_number), order);
                holder.tvSelectedCardOrder.setText(text);
                isCardSelected = true;
            }

            order++;
        }

        if (isCardSelected) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.cvGameCard.setElevation(floatingElevation);
            }
        } else {
            holder.tvSelectedCardOrder.setText("");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.cvGameCard.setElevation(defaultElevation);
            }
        }

        // selecting done callback
        if (selectedCardsIds.size() == cardsToSelectCount && allSelectedCallback != null) {
            ArrayList<JsonCard> selectedCards = new ArrayList<>();
            for (int i = 0; i < selectedCardsIds.size(); i++) {
                for (JsonCard card : items) {
                    if (card.id == selectedCardsIds.get(i)) {
                        selectedCards.add(card);
                        break;
                    }
                }
            }
            allSelectedCallback.selected(selectedCards);
        } else if (allSelectedCallback != null) {
            allSelectedCallback.notSelectedYet();
        }
    }

    private void handleWhiteCardClick(JsonCard item) {
        boolean removeOperation = false;

        // remove
        for (Integer id : selectedCardsIds) {
            if (id.equals(item.id)) {
                selectedCardsIds.remove(id);
                removeOperation = true;
                break;
            }
        }

        // add
        if (!removeOperation) {
            if (selectedCardsIds.size() < cardsToSelectCount)
                selectedCardsIds.add(item.id);
        }

        // notify
        notifyDataSetChanged();
    }

    public class WhiteCardsViewHolder extends RecyclerView.ViewHolder {

        CardView cvGameCard;
        TextView tvCardText;
        TextView tvSelectedCardOrder;

        public WhiteCardsViewHolder(View itemView) {
            super(itemView);

            cvGameCard = (CardView) itemView.findViewById(R.id.cvGameCard);
            tvCardText = (TextView) itemView.findViewById(R.id.tvCardText);
            tvSelectedCardOrder = (TextView) itemView.findViewById(R.id.tvSelectedCardOrder);
        }
    }

    public interface WhiteCardsAdapter_CardsSelectedCallback {
        void selected(ArrayList<JsonCard> selectedCards);

        void notSelectedYet();
    }
}
