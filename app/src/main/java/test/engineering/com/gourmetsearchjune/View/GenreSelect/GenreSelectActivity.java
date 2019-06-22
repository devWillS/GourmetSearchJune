package test.engineering.com.gourmetsearchjune.View.GenreSelect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import test.engineering.com.gourmetsearchjune.Entities.GenreEntity;
import test.engineering.com.gourmetsearchjune.R;
import test.engineering.com.gourmetsearchjune.Util.RecyclerView.ClickListener;
import test.engineering.com.gourmetsearchjune.Util.RecyclerView.DividerItemDecoration;
import test.engineering.com.gourmetsearchjune.Util.RecyclerView.RecyclerTouchListener;

public class GenreSelectActivity extends AppCompatActivity implements GenreSelectContract.View {
    private GenreSelectPresenter presenter;

    private ImageView backImageView;
    private RecyclerView genreRecyclerView;

    private List<GenreEntity> genreEntityList;
    private GenreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_select);

        presenter = new GenreSelectPresenter(new GenreSelectImplement(), this);

        setupView();
    }

    private void setupView() {
        backImageView = findViewById(R.id.backImageView);
        genreRecyclerView = findViewById(R.id.genreRecyclerView);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenreEntity genreEntity = adapter.getSelectedGenre();
                if (genreEntity != null) {
                    Intent intent = new Intent();
                    intent.putExtra(getString(R.string.GENRE), genreEntity);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });

        genreEntityList = presenter.getGenreList();

        adapter = new GenreAdapter(genreEntityList);
        genreRecyclerView.setAdapter(adapter);
        genreRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        genreRecyclerView.addItemDecoration(new DividerItemDecoration(1));
        genreRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                genreRecyclerView,
                new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        adapter.setSelectedPosition(position);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }

                    @Override
                    public void onClickOutOfItem() {

                    }
                }));
    }

    @Override
    public void setPresenter(GenreSelectContract.Presenter presenter) {

    }
}
