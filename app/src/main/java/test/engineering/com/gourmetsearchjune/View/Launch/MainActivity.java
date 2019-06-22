package test.engineering.com.gourmetsearchjune.View.Launch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import test.engineering.com.gourmetsearchjune.Model.Response.ErrorResults;
import test.engineering.com.gourmetsearchjune.R;
import test.engineering.com.gourmetsearchjune.Util.AlertUtil;
import test.engineering.com.gourmetsearchjune.View.GourmetSearch.GourmetSearchActivity;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private MainContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setup();
    }

    private void setup() {
        presenter = new MainPresenter(new MainImplement(), this);
        presenter.initRealm(this);
        presenter.fetchGenreMaster();
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
    }

    @Override
    public void fetchSuccess() {
        Intent gourmetSearchIntent = new Intent(getApplicationContext(), GourmetSearchActivity.class);
        startActivity(gourmetSearchIntent);
        finish();
    }

    @Override
    public void receivedError(ErrorResults error) {
        AlertUtil.showAlertWithOK(this, R.string.errorTitle, error.getMessage());
    }

    @Override
    public void receivedUnknownError() {
        AlertUtil.showAlertWithOK(this, R.string.errorTitle, R.string.apiErrorMessage);
    }

    @Override
    public void saveFailed() {
        AlertUtil.showAlertWithOK(this, R.string.errorTitle, R.string.dbSaveErrorMessage);
    }
}
