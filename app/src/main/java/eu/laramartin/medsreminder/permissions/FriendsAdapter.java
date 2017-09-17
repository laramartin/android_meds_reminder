package eu.laramartin.medsreminder.permissions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.model.User;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    private List<User> friends = new ArrayList<>();

    public void add(User friend) {
        friends.add(friend);
        notifyItemInserted(friends.size() -1);
    }

    @Override
    public FriendsAdapter.FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_friend_item, parent, false);
        return new FriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendsAdapter.FriendsViewHolder holder, int position) {
        holder.bind(friends.get(position));
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class FriendsViewHolder extends RecyclerView.ViewHolder {
        public FriendsViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(User user) {

        }
    }
}
