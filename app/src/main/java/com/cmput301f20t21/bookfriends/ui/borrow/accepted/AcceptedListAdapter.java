package com.cmput301f20t21.bookfriends.ui.borrow.accepted;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.REQUEST_STATUS;
import com.cmput301f20t21.bookfriends.ui.component.BaseBookListAdapter;

import java.util.List;

public class AcceptedListAdapter extends BaseBookListAdapter {
    private OnItemClickListener itemClickListener;
    public interface OnItemClickListener {
        void run(int position);
    }
    public AcceptedListAdapter(List<Book> books, OnItemClickListener itemClickListener) {
        super(books);
        this.itemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public AcceptedListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_list, parent, false);
        return new AcceptedListAdapter.ViewHolder(itemView, itemClickListener);
    }

    public static class ViewHolder extends BaseBookListAdapter.ViewHolder {
        final ImageButton moreBtn;
        final TextView owner;

        public ViewHolder(View v, OnItemClickListener itemClickListener) {
            super(v);
            owner = v.findViewById(R.id.item_book_owner);
            moreBtn = v.findViewById(R.id.item_book_more_btn);
            moreBtn.setVisibility(View.GONE);
            v.setOnClickListener(view -> itemClickListener.run(getAdapterPosition()));
        }

        @Override
        public void onBind(Book book) {
            super.onBind(book);
            this.owner.setText(this.itemView.getResources().getString(R.string.book_list_item_owner, book.getOwner()));
            this.status.setText(REQUEST_STATUS.ACCEPTED.toString().toLowerCase());
        }
    }
}
