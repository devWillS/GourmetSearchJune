package test.engineering.com.gourmetsearchjune.View.GourmetSearch;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import test.engineering.com.gourmetsearchjune.R;
import test.engineering.com.gourmetsearchjune.Util.BitmapUtil;
import test.engineering.com.gourmetsearchjune.Util.NetworkStateReceiver;
import test.engineering.com.gourmetsearchjune.View.GenreSelect.GenreSelectActivity;
import test.engineering.com.gourmetsearchjune.View.StoreDetail.WebActivity;

import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;

public class GourmetSearchActivity extends FragmentActivity implements OnMapReadyCallback, NetworkStateReceiver.NetworkStateReceiverListener, GoogleMap.OnMarkerClickListener, StoreDetailViewHolder.StoreDetailVIewHolderListener {
    public static final int LOCATION_REQUEST = 111;
    public static final int GENRE_SELECT = 222;

    private NetworkStateReceiver networkStateReceiver;
    private GoogleMap mMap;
    private Marker myLocation;
    private List<StoreResponse> storeList = new ArrayList<>();
    private List<Marker> storeMarkerList = new ArrayList<>();
    private Circle circle;

    private LocationManager mLocationManager;

    private ConstraintLayout genreSelectBar;
    private TextView genreNameTextView;
    private ConstraintLayout searchConstraintLayout;
    private ImageView searchImageView;

    private ConstraintLayout moveConstraintLayout;

    private ConstraintLayout currentPositionConstraintLayout;

    private ConstraintLayout storeDetailConstraintLayout;
    private RecyclerView storeDetailRecyclerView;
    private StoreDetailAdapter storeDetailAdapter;

    private Point point;
    private int position;

    private GenreEntity selectedGenre;
    private StoreResponse selectedStore;

    private boolean isUserClick;
    private boolean isStoreDetailShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gourmet_search);

        setupView();

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(this),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,}, LOCATION_REQUEST);
            return;
        }
        setup();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Display display = getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);

        setupGenreSelectBar();
        setupCurrentPositionConstraintLayout();
    }

    private void setupView() {
        genreSelectBar = findViewById(R.id.genreSelectBar);
        genreNameTextView = findViewById(R.id.genreNameTextView);
        searchConstraintLayout = findViewById(R.id.searchConstraintLayout);
        searchImageView = findViewById(R.id.searchImageView);


        moveConstraintLayout = findViewById(R.id.moveConstraintLayout);

        currentPositionConstraintLayout = findViewById(R.id.currentPositionConstraintLayout);

        storeDetailConstraintLayout = findViewById(R.id.storeDetailConstraintLayout);
        storeDetailRecyclerView = findViewById(R.id.storeDetailRecyclerView);

        storeDetailAdapter = new StoreDetailAdapter(this, this, storeList);
        storeDetailRecyclerView.setAdapter(storeDetailAdapter);
        storeDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        storeDetailRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case SCROLL_STATE_IDLE:
                        isUserClick = false;
                        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        int position = layoutManager.findFirstVisibleItemPosition();
                        Marker marker = storeMarkerList.get(position);
                        marker.showInfoWindow();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isUserClick) {
                    return;
                }
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int position;
                if (dx > 0) {
                    position = layoutManager.findLastVisibleItemPosition();
                } else {
                    position = layoutManager.findFirstVisibleItemPosition();
                }
                Marker marker = storeMarkerList.get(position);
                marker.showInfoWindow();
            }
        });
        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(storeDetailRecyclerView);
    }

    private void setupGenreSelectBar() {
        genreSelectBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GourmetSearchActivity.this, GenreSelectActivity.class);
                startActivityForResult(intent, GENRE_SELECT);
            }
        });

        searchConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStoreInfo();
            }
        });
    }

    private void setupCurrentPositionConstraintLayout() {
        currentPositionConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myLocation == null) {
                    return;
                }
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(myLocation.getPosition());
                mMap.moveCamera(cameraUpdate);
            }
        });
    }

    private void setup() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                10, // 通知のための最小時間間隔（ミリ秒）
                10, // 通知のための最小距離間隔（メートル）
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        String msg = "Lat=" + location.getLatitude()
                                + "\nLng=" + location.getLongitude();
                        Log.d("GPS", msg);
                        moveMap2Location(location, mMap.getCameraPosition().zoom);
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }
                }
        );

    }

    public void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(this);

        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        for (String provider : providers) {

            Location l = mLocationManager.getLastKnownLocation
                    (provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        Log.d("last", "getLastKnownLocation " + bestLocation);
        moveMap2Location(bestLocation, 15.0f);
    }

    private void moveMap2Location(Location location, float zoom) {
        drawCircle(location);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (myLocation != null) {
            myLocation.remove();
        }

        Bitmap current_pin = BitmapUtil.resizeMapIcons(getApplicationContext(), "current_pin", 100, 100);
        myLocation = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(current_pin))
                .anchor(0.5f, 0.5f)
        );

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.moveCamera(cameraUpdate);
    }

    private void drawCircle(Location location) {
        if (circle != null) {
            circle.remove();
        }
        circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                .radius(500)
                .strokeWidth(2)
                .strokeColor(getColor(R.color.colorPrimary))
                .fillColor(getColor(R.color.colorPrimaryTransparent)));
    }

    private void getStoreInfo() {
        if (myLocation == null) {
            return;
        }
        LatLng position = myLocation.getPosition();
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
                        new AlertDialog.Builder(GourmetSearchActivity.this)
                                .setTitle(getString(R.string.errorTitle) + "：" + error.getCode())
                                .setMessage(error.getMessage())
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        storeList.clear();
                        storeList.addAll(response.body().getResults().getShop());
                        receivedStoreDataList();
                    }
                }
            }

            @Override
            public void onFailure(Call<HotPepperObject> call, Throwable t) {
                new AlertDialog.Builder(GourmetSearchActivity.this)
                        .setTitle(getString(R.string.errorTitle))
                        .setMessage(getString(R.string.errorMessage))
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

    private void receivedStoreDataList() {
        resetStoreMarkerList();

        storeDetailAdapter.notifyDataSetChanged();
        if (storeList.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.emptyStoreList, Toast.LENGTH_LONG)
                    .show();
            hideStoreDetail();
            return;
        }
        for (int position = 0; position < storeList.size(); position++) {
            StoreResponse store = storeList.get(position);
            LatLng location = new LatLng(store.getLat(), store.getLng());
            storeMarkerList.add(mMap.addMarker(new MarkerOptions().position(location).title(String.format("%c", 'A' + position)).visible(true)));
        }

        showStoreDetail();
    }

    public void showStoreDetail() {
        if (!storeList.isEmpty()) {
            storeDetailRecyclerView.scrollToPosition(0);
            storeMarkerList.get(0).showInfoWindow();
        }
        if (isStoreDetailShown) {
            return;
        }
        isStoreDetailShown = true;
        TransitionSet transition = new TransitionSet();
        transition.setDuration(250);
        transition.addTransition(new Slide());
        TransitionManager.beginDelayedTransition(storeDetailConstraintLayout, transition);
    }

    public void hideStoreDetail() {
        isStoreDetailShown = false;
        TransitionSet transition = new TransitionSet();
        transition.setDuration(200);
        transition.addTransition(new Slide());
        TransitionManager.beginDelayedTransition(storeDetailConstraintLayout, transition);
    }


    private void resetStoreMarkerList() {
        if (storeMarkerList.isEmpty()) {
            return;
        }
        for (Marker storeMarker : storeMarkerList) {
            storeMarker.remove();
        }
        storeMarkerList.clear();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == LOCATION_REQUEST) {
            setup();
        }
    }

    @Override
    public void networkAvailable() {
        Log.d("GourmetSearchActivity", "Connected");
        searchConstraintLayout.setEnabled(true);
    }

    @Override
    public void networkUnavailable() {
        Log.d("GourmetSearchActivity", "Disonnected");
        searchConstraintLayout.setEnabled(false);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTitle() == null) {
            return false;
        }
        isUserClick = true;
        position = marker.getTitle().toCharArray()[0] - 'A';

        selectedStore = storeList.get(position);

        storeDetailRecyclerView.smoothScrollToPosition(position);
        marker.showInfoWindow();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GENRE_SELECT) {
            if (data == null) {
                selectedGenre = null;
                genreNameTextView.setText("");
                return;
            }
            selectedGenre = (GenreEntity) data.getSerializableExtra(getString(R.string.GENRE));
            genreNameTextView.setText(selectedGenre.getName());
            getStoreInfo();
        }
    }

    @Override
    public void clickedStoreName(StoreResponse store) {
        Intent intent = new Intent(getApplicationContext(), WebActivity.class);
        intent.putExtra(getString(R.string.STORE), store);
        startActivity(intent);
    }

    @Override
    public Marker getMyLocation() {
        return myLocation;
    }
}
