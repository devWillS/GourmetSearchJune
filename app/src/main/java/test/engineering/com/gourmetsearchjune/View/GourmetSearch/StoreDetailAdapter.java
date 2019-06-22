package test.engineering.com.gourmetsearchjune.View.GourmetSearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import test.engineering.com.gourmetsearchjune.Model.Response.StoreResponse;
import test.engineering.com.gourmetsearchjune.R;
import test.engineering.com.gourmetsearchjune.View.GourmetSearch.StoreDetailViewHolder.StoreDetailVIewHolderListener;

public class StoreDetailAdapter extends RecyclerView.Adapter<StoreDetailViewHolder> {
    private StoreDetailViewHolder.StoreDetailVIewHolderListener listener;
    private Context context;

    private List<StoreResponse> storeList;

    public StoreDetailAdapter(StoreDetailVIewHolderListener listener, Context context, List<StoreResponse> storeList) {
        this.listener = listener;
        this.context = context;
        this.storeList = storeList;
    }

    @NonNull
    @Override
    public StoreDetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.store_detail_cell, viewGroup, false);
        return new StoreDetailViewHolder(inflate, listener, context);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreDetailViewHolder storeDetailViewHolder, int i) {
        storeDetailViewHolder.setStore(storeList.get(i), i);
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }
}
