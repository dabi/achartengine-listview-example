package org.daubit.charttest.app;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class MySampleAdapter extends BaseAdapter {
    private final Context context;
    private LayoutInflater inflater;
    private Random random;

    private String[] names;
    private XYMultipleSeriesDataset[] rates;
    private XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

    public MySampleAdapter(Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.random = new Random();
        generateDemoData();

        XYSeriesRenderer plotRenderer = new XYSeriesRenderer();
        plotRenderer.setDisplayChartValues(false);
        plotRenderer.setShowLegendItem(false);
        plotRenderer.setDisplayBoundingPoints(true);
        plotRenderer.setColor(Color.BLACK);
        plotRenderer.setLineWidth(2);
        renderer.addSeriesRenderer(plotRenderer);

        renderer.setShowAxes(false);
        renderer.setShowLabels(false);
        renderer.setBackgroundColor(Color.TRANSPARENT);
        renderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        renderer.setPanEnabled(false, false);
        renderer.setInScroll(false);
        renderer.setAntialiasing(true);

    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return rates[position];
    }

    @Override
    public long getItemId(int position) {
        return names[position].hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.rowlayout, parent, false);
            assert convertView != null;
            holder = new MyViewHolder();
            holder.numView = (TextView) convertView.findViewById(R.id.numView);
            holder.textView = (TextView) convertView.findViewById(R.id.textView);
            holder.chartView = (FrameLayout)convertView.findViewById(R.id.chart);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        holder.numView.setText(""+(position+1));
        holder.textView.setText(names[position]);
        holder.chartView.removeAllViews();
        GraphicalView chartView = ChartFactory.getLineChartView(context, rates[position], renderer);

        holder.chartView.addView(chartView);
        return convertView;
    }
    static class MyViewHolder {
        TextView textView;
        FrameLayout chartView;
        public TextView numView;
    }
    private void generateDemoData() {
        int NUM_SIZE  = 300;

        List<String> prefixes = loadPrefixes();
        List<String> suffixes = loadSuffixes();

        names = new String[NUM_SIZE];
        rates = new XYMultipleSeriesDataset[NUM_SIZE];

        for (int i = 0; i < NUM_SIZE; i++) {
            names[i] = choose(prefixes)+" "+choose(prefixes)+" "+choose(suffixes);
            rates[i] = createChartData();
        }
    }

    private String choose(List<String> list){
        return list.get(random.nextInt(list.size()));
    }

    private List<String> loadSuffixes() {
        return new ArrayList<String>(){{
            add("Holding");
            add("Funding");
            add("Systems");
            add("Solutions");
            add("Securities");
            add("Financial");
            add("Services");
            add("Technologies");
            add("Trust");
            add("Group");
            add("Insurance");
            add("Bank");
            add("Development");
        }};
    }

    private List<String> loadPrefixes() {
        BufferedReader bufferedReader = null;
        List<String> wordlist = new ArrayList<String>();
        try {
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open("wordlist")));
                String l;
                while( (l = bufferedReader.readLine()) != null) {
                    wordlist.add(l.substring(0,1).toUpperCase()+l.substring(1,l.length()));
                }
            } finally {
                if (bufferedReader != null)
                    bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
        return wordlist;
    }


    private XYMultipleSeriesDataset createChartData() {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        TimeSeries series = new TimeSeries("kurs");
//        Calendar calendar = Calendar.getInstance();
//        int currentYear = calendar.get(Calendar.YEAR);
//        calendar.setTimeInMillis(0);
//        calendar.set(Calendar.YEAR, currentYear);

        int value = 50;
        boolean upOrDown = false;
        for (int i = 0; i < 100; i++) {
            if (upOrDown) {
                value += random.nextDouble() * 8;
            } else {
                value -= random.nextDouble() * 7;
            }
            if(random.nextInt(50)<25)
                upOrDown = !upOrDown;
            series.add(i, value);

//            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        dataset.addSeries(series);

        return dataset;
    }

}
