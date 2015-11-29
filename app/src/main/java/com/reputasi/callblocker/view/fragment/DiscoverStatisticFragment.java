package com.reputasi.callblocker.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.reputasi.callblocker.R;
import com.reputasi.callblocker.view.adapter.StatisticAdapter;
import com.reputasi.library.rest.response.Statistic;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vikraa on 2/11/2015.
 */
public class DiscoverStatisticFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_discover_statistic, null);
        View vwStatisticNumber = v.findViewById(R.id.vw_top_blocked_number);
        LinearLayout layoutChartNumber = (LinearLayout)vwStatisticNumber.findViewById(R.id.ll_chart);
        layoutChartNumber.addView(createPieChart());
        ListView lvStatisticNumber = (ListView)vwStatisticNumber.findViewById(R.id.lv_chart_details);
        StatisticAdapter adapter1 = new StatisticAdapter(getActivity(), createDummy());
        lvStatisticNumber.setAdapter(adapter1);

        View vwStatisticCategory = v.findViewById(R.id.vw_top_blocked_category);
        LinearLayout layoutChartCategory = (LinearLayout)vwStatisticCategory.findViewById(R.id.ll_chart);
        layoutChartCategory.addView(createPieChart());
        ListView lvStatisticCategory = (ListView)vwStatisticCategory.findViewById(R.id.lv_chart_details);
        StatisticAdapter adapter2 = new StatisticAdapter(getActivity(), createDummy());
        lvStatisticCategory.setAdapter(adapter2);
        return v;
    }

    private List<Statistic> createDummy() {
        List<Statistic> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Statistic model = new Statistic();
            model.setLegendName("Dummy");
            model.setValue(100);
            result.add(model);
        }
        return result;
    }

    private View createPieChart() {
        String[] code = new String[] { "eclair", "froyo"};
        double[] distribution = { 20.0, 40.0 };
        int[] color = {Color.WHITE, getResources().getColor(R.color.button_signup_color)};
        CategorySeries distributionSeries = new CategorySeries("Top Blocked Number");
        for (int i = 0; i < distribution.length; i++) {
            distributionSeries.add(code[i], distribution[i]);
        }
        DefaultRenderer defaultRenderer = new DefaultRenderer();
        for (int i = 0; i < distribution.length; i++) {
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(color[i]);
            seriesRenderer.setShowLegendItem(false);
            defaultRenderer.addSeriesRenderer(seriesRenderer);

        }
        defaultRenderer.setDisplayValues(false);
        defaultRenderer.setShowLabels(false);
        defaultRenderer.setPanEnabled(false);
        defaultRenderer.setZoomEnabled(false);
        /*defaultRenderer.setChartTitle("Top Blocked Number");
        defaultRenderer.setChartTitleTextSize(20F);
        defaultRenderer.setDisplayValues(true);
        */
        return ChartFactory.getPieChartView(getActivity().getApplicationContext(), distributionSeries, defaultRenderer);
    }

}
