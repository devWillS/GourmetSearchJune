package test.engineering.com.gourmetsearchjune.View.GenreSelect;

import java.util.List;

import test.engineering.com.gourmetsearchjune.Entities.GenreEntity;
import test.engineering.com.gourmetsearchjune.Util.MVP.BaseModel;
import test.engineering.com.gourmetsearchjune.Util.MVP.BasePresenter;
import test.engineering.com.gourmetsearchjune.Util.MVP.BaseView;

public interface GenreSelectContract {


    interface Model extends BaseModel<Presenter> {
        List<GenreEntity> getGenreList();
    }

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        List<GenreEntity> getGenreList();
    }
}
