package com.example.android.bakingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for Recipe Ingredient objects.
 */
public class RecipeIngredientArrayAdapter extends
        RecyclerView.Adapter<RecipeIngredientArrayAdapter.RecipeIngredientViewHolder> {

    public static final String TAG = RecipeIngredientArrayAdapter.class.getSimpleName();

    public interface RecipeIngredientArrayAdapterOnClickHandler {
        void onClick(RecipeIngredient m);
    }

    private Context mContext;

    private final RecipeIngredientArrayAdapterOnClickHandler mClickHandler;

    private List<RecipeIngredient> mRecipeIngredients = new ArrayList<>();

    public RecipeIngredientArrayAdapter(Context context, RecipeIngredientArrayAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public RecipeIngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View RecipeIngredientView = inflater.inflate(R.layout.item_ingredient, parent, shouldAttachToParentImmediately);
        RecipeIngredientViewHolder viewHolder = new RecipeIngredientViewHolder(RecipeIngredientView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeIngredientViewHolder holder, int position) {
        RecipeIngredient RecipeIngredient = mRecipeIngredients.get(position);
        TextView ingredientView = holder.tvIngredient;
        TextView quantityView = holder.tvQuantity;
        TextView measureView = holder.tvMeasure;
        Log.d("INGREDIENT ADAPTER", "step description is: " + RecipeIngredient.getIngredient());
        quantityView.setText(RecipeIngredient.getQuantity());
        measureView.setText(RecipeIngredient.getMeasure());
        ingredientView.setText(RecipeIngredient.getIngredient());
    }

    @Override
    public int getItemCount() {
        return (mRecipeIngredients == null ? 0 : mRecipeIngredients.size());
    }

    /**
     * ViewHolder class
     */
    public class RecipeIngredientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ButterKnife binding */
        @BindView(R.id.tvIngredient) TextView tvIngredient;
        @BindView(R.id.tvQuantity) TextView tvQuantity;
        @BindView(R.id.tvMeasure) TextView tvMeasure;

        public RecipeIngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            RecipeIngredient RecipeIngredient = mRecipeIngredients.get(adapterPosition);
            mClickHandler.onClick(RecipeIngredient);
        }
    }

    public void setRecipeIngredientData(/*Array*/List<RecipeIngredient> RecipeIngredientData) {
        mRecipeIngredients.clear();
        mRecipeIngredients.addAll(RecipeIngredientData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

}
