package test.engineering.com.gourmetsearchjune.View.Launch;

import android.content.Context;

import java.util.List;

import test.engineering.com.gourmetsearchjune.Model.Response.ErrorResults;
import test.engineering.com.gourmetsearchjune.Model.Response.GenreResponse;
import test.engineering.com.gourmetsearchjune.Util.MVP.BaseModel;
import test.engineering.com.gourmetsearchjune.Util.MVP.BasePresenter;
import test.engineering.com.gourmetsearchjune.Util.MVP.BaseView;

public interface MainContract {


    interface Model extends BaseModel<Presenter> {
        void initRealm(Context context);

        void getGenreMaster();

        boolean saveGenreList(List<GenreResponse> genreList);
    }

    interface View extends BaseView<Presenter> {
        void fetchSuccess();

        void receivedError(ErrorResults error);

        void receivedUnknownError();

        void saveFailed();
    }

    interface Presenter extends BasePresenter {
        void initRealm(Context context);

        void fetchGenreMaster();

        void receivedGenreList(List<GenreResponse> genreList);

        void receivedError(ErrorResults error);

        void receivedUnknownError();
    }
}
