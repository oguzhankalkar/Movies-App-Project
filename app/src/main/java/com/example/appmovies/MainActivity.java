package com.example.appmovies;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmovies.adapter.MyAdapter;
import com.example.appmovies.model.Movie;
import com.example.appmovies.model.ResultMovieData;
import com.example.appmovies.network.GetDataService;
import com.example.appmovies.network.RetrofitClientInstance;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static com.example.appmovies.util.StringHandler.formatDate;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    TextInputEditText searchField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchField = findViewById(R.id.search_field);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading");
    }

    private void getPopularMovies() {
        progressDialog.show();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ResultMovieData> call = service.getPopularMovies(RetrofitClientInstance.getApiKey(), "en-US", 1);
        call.enqueue(new Callback<ResultMovieData>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ResultMovieData> call, Response<ResultMovieData> response) {
                progressDialog.dismiss();
                generateMovieList(response
                        .body()
                        .getResults()
                        .stream()
                        .peek(movie -> movie.setReleaseDate(formatDate(movie.getReleaseDate())))
                        .collect(Collectors.toList()));
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<ResultMovieData> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(
                        MainActivity.this,
                        "Something went wrong",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    public void searchMovies(View view) {
        progressDialog.show();
        String query = searchField.getText().toString();
        if (query == null || query.isEmpty()) {
            getPopularMovies();
            return;
        }
        GetDataService service = RetrofitClientInstance
                .getRetrofitInstance()
                .create(GetDataService.class);

        Call<ResultMovieData> call = service.searchMovies(
                RetrofitClientInstance.getApiKey(),
                "en-US",
                query,
                1,
                false
        );
        call.enqueue(new Callback<ResultMovieData>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ResultMovieData> call, Response<ResultMovieData> response) {
                progressDialog.dismiss();
                generateMovieList(response
                        .body()
                        .getResults()
                        .stream()
                        .peek(movie -> movie.setReleaseDate(formatDate(movie.getReleaseDate())))
                        .collect(Collectors.toList()));
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<ResultMovieData> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(
                        MainActivity.this,
                        "Something went wrong",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
    private void generateMovieList(List<Movie> movies) {
        RecyclerView recyclerView = findViewById(R.id.customRecyclerView);
        MyAdapter adapter = new MyAdapter(this, movies);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

}