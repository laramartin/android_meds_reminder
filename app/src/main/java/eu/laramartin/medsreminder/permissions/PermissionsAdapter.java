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
import eu.laramartin.medsreminder.common.DialogsUtility;
import eu.laramartin.medsreminder.model.Permission;

import static eu.laramartin.medsreminder.common.Utils.setVibration;

public class PermissionsAdapter extends RecyclerView.Adapter<PermissionsAdapter.PermissionsViewHolder> {

    private List<Permission> permissions = new ArrayList<>();

    public void add(Permission permission) {
        permissions.add(permission);
        notifyItemInserted(permissions.size() - 1);
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
        holder.bind(permissions.get(position));
    }

    @Override
    public int getItemCount() {
        return permissions.size();
    }

    public void remove(Permission permission) {
        for (int i = 0; i < permissions.size(); i++) {
            if (permission.getKey().equals(permissions.get(i).getKey())) {
                permissions.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public class PermissionsViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        @BindView(R.id.permissions_email_text)
        TextView emailTextView;
        private Permission permission;

        public PermissionsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Permission permission) {
            this.permission = permission;

            emailTextView.setText(permission.getEmail());
        }

        @Override
        public boolean onLongClick(View view) {
            DialogsUtility.showRevokePermissionDialog(view.getContext(), permission);
            setVibration(view);
            return false;
        }
    }
}
