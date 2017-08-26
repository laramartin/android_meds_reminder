package eu.laramartin.medsreminder;

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
import eu.laramartin.medsreminder.model.Med;

public class MedsAdapter extends RecyclerView.Adapter<MedsAdapter.MedViewHolder>{

    private List<Med> meds = new ArrayList<>();

    public void add(Med med) {
        meds.add(med);
        notifyItemInserted(meds.size() -1);
    }

    public class MedViewHolder extends RecyclerView.ViewHolder {

        // TODO: 20.08.17 Lara: remove this TextView from layout
        @BindView(R.id.meds_text)
        TextView medsText;

        public MedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public MedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_med_item, parent, false);
        return new MedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MedViewHolder holder, int position) {
        Med med = meds.get(position);
        holder.medsText.setText(med.toString());
    }

    @Override
    public int getItemCount() {
        if (meds != null) {
            return meds.size();
        }
        return 0;
    }
}
