package eu.laramartin.medsreminder.friends;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.model.Report;
import eu.laramartin.medsreminder.model.User;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    private List<User> friends = new ArrayList<>();

    public void add(User friend) {
        friends.add(friend);
        notifyItemInserted(friends.size() - 1);
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

        @BindView(R.id.friend_email_text)
        TextView friendEmailTextView;
//        @BindView(R.id.friend_take_text_1)
//        TextView friendTakeOne;
//        @BindView(R.id.friend_take_text_2)
//        TextView friendTakeTwo;
//        @BindView(R.id.friend_take_text_3)
//        TextView friendTakeThree;
//        @BindView(R.id.friend_take_text_4)
//        TextView friendTakeFour;
//        @BindView(R.id.friend_take_text_5)
//        TextView friendTakeFive;
        @BindViews({R.id.friend_take_text_1, R.id.friend_take_text_2, R.id.friend_take_text_3, R.id.friend_take_text_4, R.id.friend_take_text_5})
        List<TextView> takesTextViews;

        public FriendsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(User friendUser) {
            friendEmailTextView.setText(friendUser.getEmail());

            // TODO: 17.09.17 Lara: assert if friend has reports
            // TODO: 17.09.17 Lara: display reports if available
            if (friendUser.getReports() != null) {
                displayFriendReports(friendUser.getReports());
            } else {
                takesTextViews.get(0).setText(R.string.friends_takes_empty);
            }
        }

        private void displayFriendReports(Map<String, Report> reports) {
            // Convert Firebase Map to a list to order it
            List<Report> reportsCol = new ArrayList<>(reports.values());
            // Sort using a comparator that compares the timeTaken property
            Collections.sort(reportsCol, new Comparator<Report>() {
                @Override
                public int compare(Report r1, Report r2) {
                    // Sort alphabetically reversed
                    return r2.getTimeTaken().compareTo(r1.getTimeTaken());
                }
            });

            // Take 5 reports maximum because we have 5 TextViews
            int limit = Math.min(reportsCol.size(), 5);
            for (int i = 0; i < limit; i++) {
                String text = reportsCol.get(i).getMedName().concat("\n").concat(reportsCol.get(i).getTimeTaken());
                takesTextViews.get(i).setText(text);
                takesTextViews.get(i).setVisibility(View.VISIBLE);
            }
        }
    }
}
