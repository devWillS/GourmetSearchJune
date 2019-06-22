package test.engineering.com.gourmetsearchjune.View.GourmetSearch;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import test.engineering.com.gourmetsearchjune.Entities.GenreEntity;
import test.engineering.com.gourmetsearchjune.Model.Response.ErrorResults;
import test.engineering.com.gourmetsearchjune.Model.Response.StoreResponse;
import test.engineering.com.gourmetsearchjune.Util.MVP.BaseModel;
import test.engineering.com.gourmetsearchjune.Util.MVP.BasePresenter;
import test.engineering.com.gourmetsearchjune.Util.MVP.BaseView;

public interface GourmetSearchContract {


    interface Model extends BaseModel<Presenter> {
        void getStoreList(LatLng position, GenreEntity selectedGenre);
    }

    interface View extends BaseView<Presenter> {
        void receivedStoreList(List<StoreResponse> storeList);

        void receivedError(ErrorResults error);

        void receivedUnknownError();
    }

    interface Presenter extends BasePresenter {
        void getStoreList(LatLng position, GenreEntity selectedGenre);

        void receivedStoreList(List<StoreResponse> storeList);

        void receivedError(ErrorResults error);

        void receivedUnknownError();
    }
}
