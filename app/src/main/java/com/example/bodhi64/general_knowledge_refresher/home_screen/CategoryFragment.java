package com.example.bodhi64.general_knowledge_refresher.home_screen;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.bodhi64.general_knowledge_refresher.Common;
import com.example.bodhi64.general_knowledge_refresher.R;
import com.example.bodhi64.general_knowledge_refresher.model.Category;
import com.example.bodhi64.general_knowledge_refresher.play_screen.SetupActivity;
import com.example.bodhi64.general_knowledge_refresher.viewholders.CategoryViewHolder;
import com.example.bodhi64.general_knowledge_refresher.interfaces.ItemClickListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    RecyclerView categoriesList;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category,CategoryViewHolder> adapter;

    FirebaseDatabase db;
    DatabaseReference categories;


    public CategoryFragment() {
        // Required empty public constructor
    }

    public static CategoryFragment newInstance(){
        CategoryFragment categoryFragment= new CategoryFragment();
        return categoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db=FirebaseDatabase.getInstance();
        categories=db.getReference("Category");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_category, container, false);
        categoriesList=(RecyclerView)v.findViewById(R.id.categories_list);
        categoriesList.setHasFixedSize(true);
        layoutManager=new GridLayoutManager(container.getContext(), 2);
        categoriesList.setLayoutManager(layoutManager);

        loadCategories();

        return v;
    }

    private void loadCategories() {
        adapter= new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(
                Category.class,
                R.layout.category_item,
                CategoryViewHolder.class,
                categories) {
            @Override
            protected void populateViewHolder(final CategoryViewHolder viewHolder, final Category model, int position) {
                viewHolder.category_name.setText(model.getName());

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .priority(Priority.HIGH);

                Glide.with(getContext())
                        .asBitmap()
                        .load(model.getImage())
                        .apply(options).into(new BitmapImageViewTarget(viewHolder.category_image) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(bitmap, transition);
                        Palette.from(bitmap).generate(palette -> viewHolder.setBackgroundColor(palette, viewHolder));
                    }
                });

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        startActivity(new Intent(getActivity(), SetupActivity.class));
                        Common.categoryId= adapter.getRef(position).getKey();
                        Common.categoryName=model.getName();

                    }

                });
            }
        };
        adapter.notifyDataSetChanged();
        categoriesList.setAdapter(adapter);
    }


}


