package test.engineering.com.gourmetsearchjune.View.GourmetSearch;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import test.engineering.com.gourmetsearchjune.Entities.GenreEntity;
import test.engineering.com.gourmetsearchjune.Model.Response.ErrorResults;
import test.engineering.com.gourmetsearchjune.Model.Response.StoreResponse;
import test.engineering.com.gourmetsearchjune.View.GourmetSearch.GourmetSearchContract.Model;
import test.engineering.com.gourmetsearchjune.View.GourmetSearch.GourmetSearchContract.View;

public class GourmetSearchPresenter implements GourmetSearchContract.Presenter {
    private GourmetSearchContract.Model model;
    private GourmetSearchContract.View view;

    public GourmetSearchPresenter(Model model, View view) {
        this.model = model;
        this.view = view;

        this.model.setPresenter(this);
        this.view.setPresenter(this);
    }

    @Override
    public void getStoreList(LatLng position, GenreEntity selectedGenre) {
        model.getStoreList(position, selectedGenre);
    }

    @Override
    public void receivedStoreList(List<StoreResponse> storeList) {
        view.receivedStoreList(storeList);
    }

    @Override
    public void receivedError(ErrorResults error) {
        view.receivedError(error);
    }

    @Override
    public void receivedUnknownError() {
        view.receivedUnknownError();
    }
}
