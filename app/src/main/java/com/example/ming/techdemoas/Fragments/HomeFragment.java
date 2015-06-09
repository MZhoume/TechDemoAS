package com.example.ming.techdemoas.Fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;

import com.example.ming.techdemoas.R;
import com.example.ming.techdemoas.Services.DataModel;
import com.example.ming.techdemoas.Services.DataService;
import com.example.ming.techdemoas.Services.ServiceLocator;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment implements IUserFragment {

    XYSeries[] series;
    XYMultipleSeriesDataset dataset;
    XYMultipleSeriesRenderer mrenderer;
    int index;
    Random mRandom = new Random(Calendar.getInstance().getTimeInMillis());
    private List<DataModel> dataModels = new ArrayList<>();
    private List<List<DataModel>> mDataModels = new ArrayList<>();
    private DataModelAdapter mDataModelAdapter;
    private GraphicalView lineView;
    private onDataReceivedListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        final TabHost tab = (TabHost) view.findViewById(R.id.tabHost);
        tab.setup();
        tab.addTab(tab.newTabSpec("tabMonitor").setIndicator("Monitor").setContent(R.id.tabMonitor));
        tab.addTab(tab.newTabSpec("tabChart").setIndicator("Chart").setContent(R.id.tabChart));

        final LinearLayout tabMonitor = (LinearLayout) view.findViewById(R.id.tabMonitor);
        final LinearLayout tabChart = (LinearLayout) view.findViewById(R.id.tabChart);

        final ListView listView = new ListView(getActivity());
        mDataModelAdapter = new DataModelAdapter(getActivity(), dataModels);
        listView.setAdapter(mDataModelAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tab.setCurrentTab(1);

                series = new XYSeries[dataModels.get(0).getNames().length];
                mrenderer = new XYMultipleSeriesRenderer();
                mrenderer.setApplyBackgroundColor(true);
                mrenderer.setBackgroundColor(((ColorDrawable) tabChart.getBackground()).getColor());
                mrenderer.setMarginsColor(((ColorDrawable) tabChart.getBackground()).getColor());
                for (int i = 0; i < dataModels.get(0).getNames().length; i++) {
                    series[i] = new XYSeries(dataModels.get(0).getNames()[i]);

                    XYSeriesRenderer renderer = new XYSeriesRenderer();
                    renderer.setDisplayChartValues(true);
                    renderer.setColor(Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255)));
                    mrenderer.addSeriesRenderer(renderer);
                }

                for (index = 0; index < mDataModels.get(position).size(); index++) {
                    for (int j = 0; j < dataModels.get(0).getNames().length; j++) {
                        series[j].add(index, mDataModels.get(position).get(index).getValues()[j]);
                    }
                }

                dataset = new XYMultipleSeriesDataset();
                for (XYSeries sery : series) {
                    dataset.addSeries(sery);
                }
                lineView = ChartFactory.getLineChartView(getActivity(), dataset, mrenderer);
                tabChart.addView(lineView);

                listener = new onDataReceivedListener(position);
                ServiceLocator.getDataService().addOnDataReceivedListener(listener);
            }
        });

        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("tabMonitor")) {
                    ServiceLocator.getDataService().removeOnDataReceivedListener(listener);

                    if (lineView != null) {
                        tabChart.removeView(lineView);
                    }
                }
            }
        });

        tabMonitor.addView(listView);

        ServiceLocator.getDataService().addOnDataReceivedListener(new DataService.onDataReceivedListener() {
            @Override
            public void onDataReceived(DataModel[] models) {
                int i;

                for (i = 0; i < dataModels.size(); i++) {
                    dataModels.set(i, models[i]);

                    mDataModels.get(i).add(models[i]);
                }

                for (; i < models.length; i++) {
                    dataModels.add(models[i]);

                    int len = mDataModels.size();
                    mDataModels.add(new ArrayList<DataModel>());
                    mDataModels.get(len).add(models[i]);
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDataModelAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        return view;
    }

    @Override
    public String GetTitle() {
        return "Home";
    }

    private class onDataReceivedListener implements DataService.onDataReceivedListener {
        int drawingIndex;

        public onDataReceivedListener(int index) {
            drawingIndex = index;
        }

        @Override
        public void onDataReceived(DataModel[] models) {
            DataModel m = models[drawingIndex];
            for (int i = 0; i < m.getNames().length; i++) {
                series[i].add(index, m.getValues()[i]);
            }
            index++;

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lineView.invalidate();
                }
            });
        }
    }
}
