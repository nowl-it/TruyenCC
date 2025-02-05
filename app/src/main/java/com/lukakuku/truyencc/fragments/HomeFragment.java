package com.lukakuku.truyencc.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lukakuku.truyencc.R;
import com.lukakuku.truyencc.adapters.GenreAdapter;
import com.lukakuku.truyencc.models.Genre;
import com.lukakuku.truyencc.models.TruyenCCAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AlertDialog dialog;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        SearchView searchView = view.findViewById(R.id.search_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        searchView.clearFocus();

        RecyclerView newNovelReadingLayout = view.findViewById(R.id.newNovelReadingLayout);

        newNovelReadingLayout.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://truyencc.onrender.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TruyenCCAPI truyenCCAPI = retrofit.create(TruyenCCAPI.class);

        Call<List<Genre>> call = truyenCCAPI.getGenres();

        call.enqueue(
                new Callback<List<Genre>>() {
                    @Override
                    public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                        if (!response.isSuccessful()) {
                            Log.d("ERROR FETCHING", "onResponse: ");
                            Toast.makeText(view.getContext(), response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        List<Genre> genres = response.body();

                        GenreAdapter genreAdapter = new GenreAdapter(view.getContext(), genres);
                        newNovelReadingLayout.setAdapter(genreAdapter);
                    }

                    @Override
                    public void onFailure(Call<List<Genre>> call, Throwable throwable) {

                    }
                }
        );
    }
}
