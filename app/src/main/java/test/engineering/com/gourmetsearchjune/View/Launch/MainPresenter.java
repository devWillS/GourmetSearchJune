package test.engineering.com.gourmetsearchjune.View.Launch;

import android.content.Context;

import java.util.List;

import test.engineering.com.gourmetsearchjune.Model.Response.ErrorResults;
import test.engineering.com.gourmetsearchjune.Model.Response.GenreResponse;

public class MainPresenter implements MainContract.Presenter {
    private MainContract.Model model;
    private MainContract.View view;

    public MainPresenter(MainContract.Model model, MainContract.View view) {
        this.model = model;
        this.view = view;

        this.model.setPresenter(this);
        this.view.setPresenter(this);
    }

    @Override
    public void initRealm(Context context) {
        model.initRealm(context);
    }

    @Override
    public void fetchGenreMaster() {
        model.getGenreMaster();
    }

    @Override
    public void receivedGenreList(List<GenreResponse> genreList) {
        boolean result = model.saveGenreList(genreList);
        if (result) {
            view.fetchSuccess();
        } else {
            view.saveFailed();
        }
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
