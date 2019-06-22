package test.engineering.com.gourmetsearchjune.View.Launch;

import android.content.Context;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.engineering.com.gourmetsearchjune.BuildConfig;
import test.engineering.com.gourmetsearchjune.Model.API.APIInterface;
import test.engineering.com.gourmetsearchjune.Model.API.APIService;
import test.engineering.com.gourmetsearchjune.Model.Dao.GenreEntityDao;
import test.engineering.com.gourmetsearchjune.Model.Response.ErrorResults;
import test.engineering.com.gourmetsearchjune.Model.Response.GenreResponse;
import test.engineering.com.gourmetsearchjune.Model.Response.HotPepperObject;
import test.engineering.com.gourmetsearchjune.R;

public class MainImplement implements MainContract.Model {
    private MainContract.Presenter presenter;

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initRealm(Context context) {
        Realm.init(context);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name(context.getString(R.string.REALM_DATABASE))
                .schemaVersion(context.getResources().getInteger(R.integer.REALM_DATABASE_VERSION))
                .deleteRealmIfMigrationNeeded()
                .build();
        //Now set this config as the default config for your app
        //This way you can call Realm.getDefaultInstance elsewhere
        Realm.setDefaultConfiguration(configuration);
    }

    @Override
    public void getGenreMaster() {
        APIInterface apiInterface = APIService.createService(APIInterface.class);
        Call<HotPepperObject> call = apiInterface.getGenreMaster(
                BuildConfig.hotpepperAPIKey,
                "json"
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
                        List<GenreResponse> genreResponseList = response.body().getResults().getGenre();
                        presenter.receivedGenreList(genreResponseList);

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
    public boolean saveGenreList(List<GenreResponse> genreList) {
        return GenreEntityDao.getInstance().add(genreList, true);
    }
}
