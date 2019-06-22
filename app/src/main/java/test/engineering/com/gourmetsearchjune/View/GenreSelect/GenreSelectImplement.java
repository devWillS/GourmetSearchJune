package test.engineering.com.gourmetsearchjune.View.GenreSelect;

import java.util.List;

import test.engineering.com.gourmetsearchjune.Entities.GenreEntity;
import test.engineering.com.gourmetsearchjune.Model.Dao.GenreEntityDao;

public class GenreSelectImplement implements GenreSelectContract.Model {
    private GenreSelectContract.Presenter presenter;

    @Override
    public void setPresenter(GenreSelectContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public List<GenreEntity> getGenreList() {
        return GenreEntityDao.getInstance().findAll();
    }
}
