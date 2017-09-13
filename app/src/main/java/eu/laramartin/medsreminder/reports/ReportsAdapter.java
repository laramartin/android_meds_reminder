package eu.laramartin.medsreminder.reports;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.model.Report;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportsHolder> {

    private List<Report> reports = new ArrayList<>();

    public void add(Report report) {
        reports.add(report);
        notifyItemInserted(reports.size() - 1);
    }

    @Override
    public ReportsAdapter.ReportsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_report_item, parent, false);
        return new ReportsAdapter.ReportsHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportsAdapter.ReportsHolder holder, int position) {
        holder.bind(reports.get(position));
    }

    @Override
    public int getItemCount() {
        if (reports != null) {
            return reports.size();
        }
        return 0;
    }

    public class ReportsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.report_med_name)
        TextView medName;
        @BindView(R.id.report_time)
        TextView timeTaken;

        public ReportsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Report report) {
            medName.setText(report.getMedName());
            timeTaken.setText(report.getTimeTaken());
        }
    }
}
