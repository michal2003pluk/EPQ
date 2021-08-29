package com.thundercandy.epq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardRecViewAdapter extends RecyclerView.Adapter<CardRecViewAdapter.ViewHolder> {

    private ArrayList<Card> cards;
    private Context mContext;

    public CardRecViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_item, parent, false); // Creates a new view of the item that will hold each flashcard
        return new ViewHolder(view);                                    // Returns the ViewHolder instance of that CardView view
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtCardName.setText(cards.get(position).getTerm());
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public void setCards(ArrayList<Card> cards) {
        if (cards == null) {
            cards = new ArrayList<>();
        }
        this.cards = cards;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout card_item;
        private TextView txtCardName;
        private ImageView btnEdit, btnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card_item = itemView.findViewById(R.id.card_item);                 // Initializes all the GUI components
            txtCardName = itemView.findViewById(R.id.txtCardName);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnRemove = itemView.findViewById(R.id.btnRemove);

            card_item.setOnClickListener(v -> {
                Toast.makeText(mContext, "Card clicked", Toast.LENGTH_SHORT).show();
            });

            btnEdit.setOnClickListener(v -> {
                Toast.makeText(mContext, "Edit button clicked", Toast.LENGTH_SHORT).show();
            });

            btnRemove.setOnClickListener(v -> {
                Toast.makeText(mContext, "Remove button clicked", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
