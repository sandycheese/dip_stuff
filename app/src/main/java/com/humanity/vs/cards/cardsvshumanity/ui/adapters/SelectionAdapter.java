package com.humanity.vs.cards.cardsvshumanity.ui.adapters;

import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.humanity.vs.cards.cardsvshumanity.App;
import com.humanity.vs.cards.cardsvshumanity.R;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonWhiteCardsSelection;

import java.util.List;

/**
 * Created by robot on 22.11.15.
 */
public class SelectionAdapter extends RecyclerView.Adapter<SelectionAdapter.SelectionViewHolder> {

    private List<JsonWhiteCardsSelection> items;
    private SelectionAdapter_SelectCallback selectCallback;

    private static float defaultElevation = -1;
    private static final float floatingElevation = 6; // guideline is 8, but render is invalid

    private String selectedSelectionPlayerId = null;

    public SelectionAdapter(List<JsonWhiteCardsSelection> cards, SelectionAdapter_SelectCallback selectCallback) {
        this.items = cards;
        this.selectCallback = selectCallback;
    }

    @Override
    public SelectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selection, parent, false);

        return new SelectionViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(final SelectionViewHolder holder, int position) {
        final JsonWhiteCardsSelection item = items.get(position);

        // get default elevation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (defaultElevation == -1)
                defaultElevation = holder.cvSelection.getElevation();
        }

        // set id tag
        holder.cvSelection.setTag(item.playerId);

        // click handler for selection
        holder.cvSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCardClick(item, holder);
            }
        });

        // UI
        holder.tvSelectionSelected.setText("");

        int cardsCount = item.selectedWhiteCards.length;

        if (cardsCount == 1) {
            holder.setTextForCard1(item.selectedWhiteCards[0].text);
        } else if (cardsCount == 2) {
            holder.setTextForCard1(item.selectedWhiteCards[0].text);
            holder.setTextForCard2(item.selectedWhiteCards[1].text);
        } else if (cardsCount == 3) {
            holder.setTextForCard1(item.selectedWhiteCards[0].text);
            holder.setTextForCard2(item.selectedWhiteCards[1].text);
            holder.setTextForCard3(item.selectedWhiteCards[2].text);
        }
    }

    private void handleCardClick(JsonWhiteCardsSelection item, SelectionViewHolder holder) {
        if (!item.playerId.equals(selectedSelectionPlayerId)) {
            selectedSelectionPlayerId = item.playerId;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.cvSelection.setElevation(floatingElevation);
            }
            holder.tvSelectionSelected.setText(R.string.text_selected);

            if (selectCallback != null) {
                for (JsonWhiteCardsSelection selection : items) {
                    if (item.playerId.equals(selection.playerId)) {
                        selectCallback.selected(selection);
                        break;
                    }
                }

                Log.d(App.TAG, "Invalid behaviour: selected selection not found");
            }
        } else {
            selectedSelectionPlayerId = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.cvSelection.setElevation(defaultElevation);
            }
            holder.tvSelectionSelected.setText("");

            if (selectCallback != null) {
                selectCallback.notSelectedYet();
            }
        }
    }

    public interface SelectionAdapter_SelectCallback {
        void selected(JsonWhiteCardsSelection selection);

        void notSelectedYet();
    }


    public class SelectionViewHolder extends RecyclerView.ViewHolder {

        CardView cvSelection;
        TextView tvCardText1;
        TextView tvCardText2;
        TextView tvCardText3;
        TextView tvSelectionSelected;
        RelativeLayout rlSelectionCard1;
        RelativeLayout rlSelectionCard2;
        RelativeLayout rlSelectionCard3;
        View divider1;
        View divider2;

        public SelectionViewHolder(View itemView) {
            super(itemView);

            cvSelection = (CardView) itemView.findViewById(R.id.cvSelection);
            tvCardText1 = (TextView) itemView.findViewById(R.id.tvCardText1);
            tvCardText2 = (TextView) itemView.findViewById(R.id.tvCardText2);
            tvCardText3 = (TextView) itemView.findViewById(R.id.tvCardText3);
            tvSelectionSelected = (TextView) itemView.findViewById(R.id.tvSelectionSelected);
            rlSelectionCard1 = (RelativeLayout) itemView.findViewById(R.id.rlSelectionCard1);
            rlSelectionCard2 = (RelativeLayout) itemView.findViewById(R.id.rlSelectionCard2);
            rlSelectionCard3 = (RelativeLayout) itemView.findViewById(R.id.rlSelectionCard3);
            divider1 = itemView.findViewById(R.id.dividerForSelection1);
            divider2 = itemView.findViewById(R.id.dividerForSelection2);
        }

        public void setTextForCard1(String text) {
            tvCardText1.setText(text);

            rlSelectionCard2.setVisibility(View.GONE);
            rlSelectionCard3.setVisibility(View.GONE);
            divider1.setVisibility(View.GONE);
            divider2.setVisibility(View.GONE);
        }

        public void setTextForCard2(String text) {
            tvCardText2.setText(text);

            rlSelectionCard2.setVisibility(View.VISIBLE);
            rlSelectionCard3.setVisibility(View.GONE);
            divider1.setVisibility(View.VISIBLE);
            divider2.setVisibility(View.GONE);
        }

        public void setTextForCard3(String text) {
            tvCardText3.setText(text);

            rlSelectionCard2.setVisibility(View.VISIBLE);
            rlSelectionCard3.setVisibility(View.VISIBLE);
            divider1.setVisibility(View.VISIBLE);
            divider2.setVisibility(View.VISIBLE);
        }
    }
}
