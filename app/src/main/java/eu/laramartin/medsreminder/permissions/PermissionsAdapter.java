package eu.laramartin.medsreminder.permissions;

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

public class PermissionsAdapter extends RecyclerView.Adapter<PermissionsAdapter.PermissionsViewHolder> {

    private List<String> emailsGivenPermission = new ArrayList<>();

    public void add(String emailGivenPermission) {
        emailsGivenPermission.add(emailGivenPermission);
        notifyItemInserted(emailsGivenPermission.size() - 1);
    }

    @Override
    public PermissionsAdapter.PermissionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_permissions_item, parent, false);
        return new PermissionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PermissionsAdapter.PermissionsViewHolder holder, int position) {
        holder.bind(emailsGivenPermission.get(position));
    }

    @Override
    public int getItemCount() {
        return emailsGivenPermission.size();
    }

    public class PermissionsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.permissions_email_text)
        TextView emailTextView;

        public PermissionsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(String emailGivenPermission) {
            emailTextView.setText(emailGivenPermission);
        }
    }
}
