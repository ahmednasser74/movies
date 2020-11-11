package com.example.moviesapp.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.R;
import com.example.moviesapp.data.model.castDetails.CastDetails;
import com.example.moviesapp.data.model.movieCast.MovieCastData;
import com.example.moviesapp.databinding.ItemCastBinding;
import com.example.moviesapp.view.acitivty.base.BaseActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.moviesapp.data.api.ApiClient.getClient;
import static com.example.moviesapp.helper.Constant.API_KEY;
import static com.example.moviesapp.helper.Constant.BIRTH_DATE;
import static com.example.moviesapp.helper.Constant.IMAGE_URL;
import static com.example.moviesapp.helper.Constant.IMDB_LINK;
import static com.example.moviesapp.helper.Constant.PLACE_OF_BIRTH;
import static com.example.moviesapp.helper.HelperMethod.onLoadImageFromUrl;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {

    private BaseActivity activity;
    private List<MovieCastData> movieCastDataList = new ArrayList<>();
    MovieCastData movieCastData;


    public CastAdapter(BaseActivity activity, List<MovieCastData> movieCastDataList) {
        this.activity = activity;
        this.movieCastDataList = movieCastDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCastBinding itemCastBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_cast,
                parent, false);

        return new ViewHolder(itemCastBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        movieCastData = movieCastDataList.get(position);

        holder.itemCastBinding.itemMovieCastTvName.setText(movieCastData.getName());
        onLoadImageFromUrl(holder.itemCastBinding.itemMovieCastImgActor,
                IMAGE_URL + movieCastData.getProfilePath(), activity);
    }

    private void setAction(ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog(position);
            }
        });
    }

    private void showChangeLanguageDialog(int position) {
        movieCastData = movieCastDataList.get(position);

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheet);
        View bottomSheet = LayoutInflater.from(activity).inflate(R.layout.bottom_sheet_cast_details,
                (LinearLayout) activity.findViewById(R.id.dialog_sheet_language_container));

        TextView actorName = bottomSheet.findViewById(R.id.bottom_sheet_cast_details_actor_name);
        TextView actorBirthDate = bottomSheet.findViewById(R.id.bottom_sheet_cast_details_actor_birth_date);
        TextView actorImdb = bottomSheet.findViewById(R.id.bottom_sheet_cast_details_actor_imdb);
        TextView actorPlaceOfBirth = bottomSheet.findViewById(R.id.bottom_sheet_cast_details_actor_place_of_birth);
        TextView actorBio = bottomSheet.findViewById(R.id.bottom_sheet_cast_details_actor_bio);
        ImageView actorImage = bottomSheet.findViewById(R.id.bottom_sheet_cast_details_actor_img);

        onLoadImageFromUrl(actorImage, IMAGE_URL + movieCastData.getProfilePath(), activity);
        actorName.setText("Name : " + movieCastData.getName());

        getClient().getMovieCastDetails(movieCastData.getId(), API_KEY).enqueue(new Callback<CastDetails>() {
            @Override
            public void onResponse(Call<CastDetails> call, Response<CastDetails> response) {
                Log.wtf("onResponseCastDetailsId ", response.body().getId().toString());

                actorBio.setText(response.body().getBiography());

                if (response.body().getBirthday() == null || response.body().getPlaceOfBirth() == null
                        || response.body().getImdbId() == null) {
                    actorBirthDate.setVisibility(View.GONE);
                    actorPlaceOfBirth.setVisibility(View.GONE);
                    actorImdb.setVisibility(View.GONE);
                } else {
                    actorImdb.setText(IMDB_LINK + response.body().getImdbId());
                    actorBirthDate.setText(BIRTH_DATE + response.body().getBirthday());
                    actorPlaceOfBirth.setText(PLACE_OF_BIRTH + response.body().getPlaceOfBirth());
                    actorImdb.setTextColor(Color.BLUE);
                    actorImdb.setTextColor(activity.getResources().getColor(R.color.blue));
                }
            }

            @Override
            public void onFailure(Call<CastDetails> call, Throwable t) {

            }
        });


        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();
    }

    public void updateList(List list) {
        movieCastDataList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return movieCastDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCastBinding itemCastBinding;

        public ViewHolder(ItemCastBinding itemView) {
            super(itemView.getRoot());
            itemCastBinding = itemView;
        }
    }
}
