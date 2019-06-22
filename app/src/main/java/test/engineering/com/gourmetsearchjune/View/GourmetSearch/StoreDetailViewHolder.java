package test.engineering.com.gourmetsearchjune.View.GourmetSearch;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import test.engineering.com.gourmetsearchjune.Model.Response.StoreResponse;
import test.engineering.com.gourmetsearchjune.R;

public class StoreDetailViewHolder extends RecyclerView.ViewHolder {
    public interface StoreDetailVIewHolderListener {
        void clickedStoreName(StoreResponse store);

        Marker getMyLocation();
    }

    private TextView alphabetTextView;
    private TextView storeNameTextView;
    private TextView openTextView;
    private TextView minToGoTextView;

    private StoreDetailVIewHolderListener listener;
    private Context context;

    private StoreResponse selectedStore;

    public StoreDetailViewHolder(@NonNull View itemView, final StoreDetailVIewHolderListener listener, Context context) {
        super(itemView);
        this.listener = listener;
        this.context = context;

        alphabetTextView = itemView.findViewById(R.id.alphabetTextView);
        storeNameTextView = itemView.findViewById(R.id.storeNameTextView);
        openTextView = itemView.findViewById(R.id.openTextView);
        minToGoTextView = itemView.findViewById(R.id.minToGoTextView);

        storeNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedStore == null) {
                    return;
                }
                listener.clickedStoreName(selectedStore);

            }
        });
    }

    public void setStore(StoreResponse storeResponse, int position) {
        selectedStore = storeResponse;
        storeNameTextView.setText(selectedStore.getName());
        alphabetTextView.setText(String.format("%C", 'A' + position));
        if (selectedStore.getOpen() == null || selectedStore.getOpen().isEmpty()) {
            openTextView.setText(context.getResources().getString(R.string.unknownOpen));
        } else {
            openTextView.setText(selectedStore.getOpen());
        }
        float[] results = new float[3];
        Marker myLocation = listener.getMyLocation();
        Location.distanceBetween(myLocation.getPosition().latitude, myLocation.getPosition().longitude, selectedStore.getLat(), selectedStore.getLng(), results);
        float distance = results[0];
        minToGoTextView.setText(context.getResources().getString(R.string.to_minutes, Math.round(distance / 80.0f)));
    }
}
