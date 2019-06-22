package test.engineering.com.gourmetsearchjune.View.GourmetSearch;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.engineering.com.gourmetsearchjune.BuildConfig;
import test.engineering.com.gourmetsearchjune.Entities.GenreEntity;
import test.engineering.com.gourmetsearchjune.Model.API.APIInterface;
import test.engineering.com.gourmetsearchjune.Model.API.APIService;
import test.engineering.com.gourmetsearchjune.Model.Response.ErrorResults;
import test.engineering.com.gourmetsearchjune.Model.Response.HotPepperObject;
import test.engineering.com.gourmetsearchjune.Model.Response.StoreResponse;

public class GourmetSearchImplement implements GourmetSearchContract.Model {
    private GourmetSearchContract.Presenter presenter;

    @Override
    public void getStoreList(LatLng position, GenreEntity selectedGenre) {
        APIInterface apiInterface = APIService.createService(APIInterface.class);
        Call<HotPepperObject> call = apiInterface.getOptionsHotPepperObjectNew(
                BuildConfig.hotpepperAPIKey,
                selectedGenre == null ? "" : selectedGenre.getCode(),
                "json",
                position.latitude,
                position.longitude,
                26
        );
        call.enqueue(new Callback<HotPepperObject>() {
            @Override
            public void onResponse(Call<HotPepperObject> call, Response<HotPepperObject> response) {
                if (response.isSuccessful()) {
                    List<ErrorResults> errorResults = response.body().getResults().getError();
                    if (errorResults != null && !errorResults.isEmpty()) {
                        ErrorResults error = errorResults.get(0);
                        presenter.receivedError(error);
                    } else {
                        List<StoreResponse> storeList = response.body().getResults().getShop();
                        presenter.receivedStoreList(storeList);
                    }
                }
            }

            @Override
            public void onFailure(Call<HotPepperObject> call, Throwable t) {
                presenter.receivedUnknownError();
            }
        });
    }

    @Override
    public void setPresenter(GourmetSearchContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
