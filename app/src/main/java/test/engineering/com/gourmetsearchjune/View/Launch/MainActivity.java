package test.engineering.com.gourmetsearchjune.View.Launch;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
import test.engineering.com.gourmetsearchjune.View.GourmetSearch.GourmetSearchActivity;

public class MainActivity extends AppCompatActivity {
    private static final String REALM_DATABASE = "gourmet_search.realm";
    private static final int REALM_DATABASE_VERSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRealm();

        getGenreMaster();
    }

    private void getGenreMaster() {
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
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(getString(R.string.errorTitle) + "：" + error.getCode())
                                .setMessage(error.getMessage())
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        List<GenreResponse> genreResponseList = response.body().getResults().getGenre();
                        GenreEntityDao.getInstance().add(genreResponseList, true);
                        Log.d("GenreMaster", String.valueOf(genreResponseList));

                        Intent gourmetSearchIntent = new Intent(getApplicationContext(), GourmetSearchActivity.class);
                        startActivity(gourmetSearchIntent);
                    }
                }
            }

            @Override
            public void onFailure(Call<HotPepperObject> call, Throwable t) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getString(R.string.errorTitle))
                        .setMessage(getString(R.string.errorMessage))
                        .setPositiveButton("OK", null)
                        .show();

            }
        });
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration configuration  = new RealmConfiguration.Builder()
                .name(REALM_DATABASE)
                .schemaVersion(REALM_DATABASE_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build();
        //Now set this config as the default config for your app
        //This way you can call Realm.getDefaultInstance elsewhere
        Realm.setDefaultConfiguration(configuration);
    }
}
