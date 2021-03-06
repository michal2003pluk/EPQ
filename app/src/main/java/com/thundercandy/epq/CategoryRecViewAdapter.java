package com.thundercandy.epq;

import static com.thundercandy.epq.database.DbUtils.removeCategory;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.thundercandy.epq.data.Category;
import com.thundercandy.epq.data.ExpandableCategory;
import com.thundercandy.epq.database.DbUtils;
import com.thundercandy.epq.events.CardAddedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class CategoryRecViewAdapter extends RecyclerView.Adapter<CategoryRecViewAdapter.ViewHolder> {

    private ArrayList<ExpandableCategory> categories = new ArrayList<>();
    private Context mContext;

    public CategoryRecViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card, parent, false); // Creates a new view of the CardView that will hold each category
        return new ViewHolder(view);                                    // Returns the ViewHolder instance of that CardView view
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtCategoryName.setText(categories.get(position).getName());

        if (categories.get(position).isExpanded()) {
            TransitionManager.beginDelayedTransition(holder.parent);

            holder.imgExpandedState.setImageResource(R.drawable.ic_card_open);
            holder.parent.setBackgroundResource(R.drawable.card_header_expanded);

            holder.btnRemoveCategory.setVisibility(View.VISIBLE);
            holder.header_buffer.setVisibility(View.GONE);

            CardRecViewAdapter adapter = new CardRecViewAdapter(mContext, categories.get(position).getId());
            holder.cardRecView.setAdapter(adapter);
            holder.cardRecView.setLayoutManager(new LinearLayoutManager(mContext));
            adapter.setCards(categories.get(position).getCards());

            holder.btnAddNewItem.setVisibility(View.VISIBLE);

            holder.cardRecView.setVisibility(View.VISIBLE);
            TransitionManager.endTransitions(holder.parent);
        } else {
            TransitionManager.beginDelayedTransition(holder.parent);

            holder.imgExpandedState.setImageResource(R.drawable.ic_card_closed);
            holder.parent.setBackgroundResource(R.drawable.card_header_collapsed);

            holder.btnRemoveCategory.setVisibility(View.GONE);
            holder.header_buffer.setVisibility(View.VISIBLE);

            holder.btnAddNewItem.setVisibility(View.GONE);

            holder.cardRecView.setVisibility(View.GONE);
            TransitionManager.endTransitions(holder.parent);
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(ArrayList<Category> inputCategories) {
        if (inputCategories == null) {
            categories = new ArrayList<>();
        }

        for (Category c : inputCategories) {
            categories.add(c.toExpandableCategory());
        }

        notifyDataSetChanged();
    }

    public void addCategory(String cat_name) {
        int date = 0; //TODO: Change this to an actual date later on

        int cat_id = DbUtils.addCategory(mContext, cat_name, date);

        ExpandableCategory created_category = new ExpandableCategory(cat_id, cat_name, null);
        categories.add(0, created_category);
        notifyItemInserted(0);
    }

    public void updateCategory(int targetPosition) {
        categories.get(targetPosition).update(mContext);
        notifyItemChanged(targetPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView parent;                                         // Initializes all the GUI component variables
        private LinearLayout header;
        private ImageView imgExpandedState, btnRemoveCategory;
        private TextView txtCategoryName;
        private View header_buffer;
        private RecyclerView cardRecView;
        private LinearLayout btnAddNewItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.card_parent);                 // Initializes all the GUI components
            header = itemView.findViewById(R.id.header);
            header_buffer = itemView.findViewById(R.id.header_buffer);
            imgExpandedState = itemView.findViewById(R.id.imgExpandedState);
            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
            btnRemoveCategory = itemView.findViewById(R.id.btnRemoveCategory);
            cardRecView = itemView.findViewById(R.id.cardRecView);
            btnAddNewItem = itemView.findViewById(R.id.btnAddNewItem);

            header.setOnClickListener(v -> {
                ExpandableCategory c = categories.get(getAdapterPosition());
                c.flipExpanded();
                notifyItemChanged(getAdapterPosition());
            });

            btnRemoveCategory.setOnClickListener(v -> {
                Category c = categories.get(getAdapterPosition());
                //TODO: Open a dialog to ask user if they really want to remove the category
                //TODO: Ask user if they want to the keep the category's categories or remove them as well - this is the boolean `removeChildren`
                removeCategory(mContext, c.getId(), false);
                categories.remove(c);
                notifyItemRemoved(getAdapterPosition());
            });

            btnAddNewItem.setOnClickListener(v -> {
                Category c = categories.get(getAdapterPosition());
                Intent intent = new Intent(mContext, NewCardActivity.class);
                intent.putExtra("targetCategoryID", c.getId());
                intent.putExtra("targetCategoryPosition", getAdapterPosition());
                mContext.startActivity(intent);
            });
        }

    }
}
