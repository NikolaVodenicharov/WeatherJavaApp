package com.example.weath.ui.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weath.R;
import com.example.weath.ui.activities.WeatherFragment;
import com.example.weath.ui.models.ForecastDayUi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastDayViewHolder> {
    private List<ForecastDayUi> forecastDays;

    public ForecastAdapter(List<ForecastDayUi> forecastDays) {
        this.forecastDays = forecastDays;
    }

    @NonNull
    @Override
    public ForecastDayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View viewItem =inflater.inflate(R.layout.forecast_day, parent, false);
        ForecastDayViewHolder holder = new ForecastDayViewHolder(viewItem);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastDayViewHolder holder, int position) {
        ForecastDayUi forecastDay = forecastDays.get(position);

        Date date = forecastDay.getDate();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        String dayOfWeek = formatter.format(date);
        holder.weekDay.setText(dayOfWeek);

        String minMaxTemperature = forecastDay.getMinimumTemperatureInCelsius() + "/" +  forecastDay.getMaximumTemperatureInCelsius();
        holder.minMaxTemperature.setText(minMaxTemperature);

        int drawableId = WeatherFragment.findSkyConditionDrawableId(forecastDay.getSkyCondition());
        holder.skyCondition.setImageResource(drawableId);
    }

    @Override
    public int getItemCount() {
        int count = forecastDays != null ? forecastDays.size() : 0;
        return count;
    }

    public class ForecastDayViewHolder extends RecyclerView.ViewHolder{
        public TextView weekDay;
        public TextView minMaxTemperature;
        public ImageView skyCondition;

        public ForecastDayViewHolder(@NonNull View itemView) {
            super(itemView);

            weekDay = itemView.findViewById(R.id.week_day);
            minMaxTemperature = itemView.findViewById(R.id.min_max_temperature);
            skyCondition = itemView.findViewById(R.id.day_sky_condition);
        }
    }
}
