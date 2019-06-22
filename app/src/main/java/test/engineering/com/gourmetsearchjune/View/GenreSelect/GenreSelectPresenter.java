package test.engineering.com.gourmetsearchjune.View.GenreSelect;

import java.util.List;

import test.engineering.com.gourmetsearchjune.Entities.GenreEntity;
import test.engineering.com.gourmetsearchjune.View.GenreSelect.GenreSelectContract.Model;
import test.engineering.com.gourmetsearchjune.View.GenreSelect.GenreSelectContract.View;

public class GenreSelectPresenter implements GenreSelectContract.Presenter {
    private GenreSelectContract.Model model;
    private GenreSelectContract.View view;

    public GenreSelectPresenter(Model model, View view) {
        this.model = model;
        this.view = view;

        this.model.setPresenter(this);
        this.view.setPresenter(this);
    }

    @Override
    public List<GenreEntity> getGenreList() {
        return model.getGenreList();
    }
}
