package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for Recipe objects.
 */
public class RecipeArrayAdapter extends
        RecyclerView.Adapter<RecipeArrayAdapter.RecipeViewHolder> {

    public static final String TAG = RecipeArrayAdapter.class.getSimpleName();

    public interface RecipeArrayAdapterOnClickHandler {
        void onClick(Recipe m);
    }

    private Context mContext;

    private final RecipeArrayAdapterOnClickHandler mClickHandler;

    private List<Recipe> mRecipes = new ArrayList<>();

    public RecipeArrayAdapter(Context context, RecipeArrayAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View RecipeView = inflater.inflate(R.layout.item_recipe, parent, shouldAttachToParentImmediately);
        RecipeViewHolder viewHolder = new RecipeViewHolder(RecipeView);

        return viewHolder;
    }

    // Credit: https://www.publicdomainpictures.net/en/view-image.php?image=32346&picture=pink-chocolate-cupcake
    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        TextView textView = holder.tvRecipeTitle;
        textView.setText(recipe.getName());
        String imagePath = recipe.getImage();
        ImageView imageView = holder.ivImageView;
        if (TextUtils.isEmpty(imagePath)) {
            Picasso.with(mContext).load(imagePath)
                    .placeholder(R.drawable.cupcake)
                    .into(imageView);
        } else {
            Picasso.with(mContext).load(R.drawable.pink_chocolate_cupcake)
                    .into(imageView);
        }
    }

    @Override
    public int getItemCount() {
        return (mRecipes == null ? 0 : mRecipes.size());
    }

    /**
     * ViewHolder class
     */
    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ButterKnife binding */
        @BindView(R.id.tvRecipeTitle) TextView tvRecipeTitle;
        @BindView(R.id.ivRecipeImage) ImageView ivImageView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Recipe Recipe = mRecipes.get(adapterPosition);
            mClickHandler.onClick(Recipe);
        }
    }

    public void setRecipeData(List<Recipe> RecipeData) {
        mRecipes.clear();
        mRecipes.addAll(RecipeData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

}
