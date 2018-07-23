package com.example.android.bakingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.RecipeStep;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for Recipe Step objects.
 */
public class RecipeStepArrayAdapter extends
        RecyclerView.Adapter<RecipeStepArrayAdapter.RecipeStepViewHolder> {

    public static final String TAG = RecipeStepArrayAdapter.class.getSimpleName();

    public interface RecipeStepArrayAdapterOnClickHandler {
        void onClick(RecipeStep m);
    }

    private Context mContext;

    private final RecipeStepArrayAdapterOnClickHandler mClickHandler;

    private List<RecipeStep> mRecipeSteps = new ArrayList<>();

    public RecipeStepArrayAdapter(Context context, RecipeStepArrayAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public RecipeStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View RecipeStepView = inflater.inflate(R.layout.item_step, parent, shouldAttachToParentImmediately);
        RecipeStepViewHolder viewHolder = new RecipeStepViewHolder(RecipeStepView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeStepViewHolder holder, int position) {
        RecipeStep recipeStep = mRecipeSteps.get(position);
        TextView textView = holder.tvRecipeStep;
        Log.d("STEP ADAPTER", "step description is: " + recipeStep.getShortDescription());
        textView.setText(recipeStep.getShortDescription());
    }

    @Override
    public int getItemCount() {
        return (mRecipeSteps == null ? 0 : mRecipeSteps.size());
    }

    /**
     * ViewHolder class
     */
    public class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ButterKnife binding */
        @BindView(R.id.tvRecipeStep) TextView tvRecipeStep;

        public RecipeStepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            RecipeStep recipeStep = mRecipeSteps.get(adapterPosition);
            mClickHandler.onClick(recipeStep);
        }
    }

    public void setRecipeStepData(/*Array*/List<RecipeStep> RecipeStepData) {
        mRecipeSteps.clear();
        mRecipeSteps.addAll(RecipeStepData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

}
