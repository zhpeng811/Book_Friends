package com.cmput301f20t21.bookfriends.ui.library.owned;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301f20t21.bookfriends.R;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.enums.BOOK_ACTION;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.ui.component.BaseDetailActivity;
import com.cmput301f20t21.bookfriends.ui.library.add.AddEditActivity;

public class OwnedDetailActivity extends BaseDetailActivity {

    private Book oldBook;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BOOK_STATUS status = book.getStatus();
        if (status == BOOK_STATUS.AVAILABLE) {
            button.setVisibility(View.INVISIBLE);
        }
        else if (status == BOOK_STATUS.REQUESTED) {
            button.setText(R.string.click_to_see_requests);
        }
        else if (status == BOOK_STATUS.ACCEPTED) {
            button.setText(R.string.scan_to_hand_over);
        }
        else if (status == BOOK_STATUS.BORROWED) {
            button.setText(R.string.scan_to_receive);
        }
    }

    private void openAddEditActivity(Book book) {
        Intent intent = new Intent(OwnedDetailActivity.this, AddEditActivity.class);
        intent.putExtra(BaseDetailActivity.BOOK_ACTION_KEY, BOOK_ACTION.EDIT);
        intent.putExtra(BaseDetailActivity.BOOK_DATA_KEY, book);
        startActivityForResult(intent, BOOK_ACTION.EDIT.getCode());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (oldBook != null) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(AddEditActivity.OLD_BOOK_INTENT_KEY, oldBook);
                    resultIntent.putExtra(AddEditActivity.UPDATED_BOOK_INTENT_KEY, book);
                    setResult(RESULT_OK, resultIntent);
                }
                finish();
                return true;
            case R.id.edit_button:
                openAddEditActivity(book);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (book.getStatus() == BOOK_STATUS.AVAILABLE) {
            getMenuInflater().inflate(R.menu.top_detail_menu, menu);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == BOOK_ACTION.EDIT.getCode()) {
                oldBook = data.getParcelableExtra(AddEditActivity.OLD_BOOK_INTENT_KEY);
                Book updatedBook = data.getParcelableExtra(AddEditActivity.UPDATED_BOOK_INTENT_KEY);
                updateBook(updatedBook);
                Toast.makeText(this, getString(R.string.edit_book_successful), Toast.LENGTH_SHORT).show();
            }
        }
    }
}