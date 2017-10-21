/*
 * PROJECT LICENSE
 *
 * This project was submitted by Lara Martín as part of the Nanodegree At Udacity.
 *
 * As part of Udacity Honor code, your submissions must be your own work, hence
 * submitting this project as yours will cause you to break the Udacity Honor Code
 * and the suspension of your account.
 *
 * Me, the author of the project, allow you to check the code as a reference, but if
 * you submit it, it's your own responsibility if you get expelled.
 *
 * Copyright (c) 2017 Lara Martín
 *
 * Besides the above notice, the following license applies and this license notice
 * must be included in all works derived from this project.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

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
        @BindViews({R.id.friend_take_text_1, R.id.friend_take_text_2, R.id.friend_take_text_3, R.id.friend_take_text_4, R.id.friend_take_text_5})
        List<TextView> takesTextViews;

        public FriendsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(User friendUser) {
            hideTakesTextViews();
            friendEmailTextView.setText(friendUser.getEmail());

            if (friendUser.getReports() != null) {
                displayFriendReports(friendUser.getReports());
            } else {
                takesTextViews.get(0).setVisibility(View.VISIBLE);
                takesTextViews.get(0).setText(R.string.friends_takes_empty);
            }
        }

        private void hideTakesTextViews() {
            for (TextView takeTextView : takesTextViews) {
                takeTextView.setVisibility(View.GONE);
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
