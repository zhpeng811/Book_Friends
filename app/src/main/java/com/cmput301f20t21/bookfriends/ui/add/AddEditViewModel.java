/*
 * AddEditViewModel.java
 * Version: 1.0
 * Date: November 4, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.add;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301f20t21.bookfriends.callbacks.OnFailCallbackWithMessage;
import com.cmput301f20t21.bookfriends.callbacks.OnSuccessCallbackWithMessage;
import com.cmput301f20t21.bookfriends.entities.Book;
import com.cmput301f20t21.bookfriends.enums.BOOK_ERROR;
import com.cmput301f20t21.bookfriends.enums.BOOK_STATUS;
import com.cmput301f20t21.bookfriends.repositories.AuthRepository;
import com.cmput301f20t21.bookfriends.repositories.BookRepository;
import com.cmput301f20t21.bookfriends.repositories.api.IAuthRepository;
import com.cmput301f20t21.bookfriends.repositories.api.IBookRepository;

/**
 * The ViewModel for AddEditActivity
 */
public class AddEditViewModel extends ViewModel {
    // data-binding attributes, two way bound with the xml
    public final MutableLiveData<String> bookIsbn = new MutableLiveData<>();
    public final MutableLiveData<String> bookTitle = new MutableLiveData<>();
    public final MutableLiveData<String> bookAuthor = new MutableLiveData<>();
    public final MutableLiveData<String> bookDescription = new MutableLiveData<>();
    private final IAuthRepository authRepository;
    private final IBookRepository bookRepository;
    // the local, updated image uri that might update after first remote image fetch
    private final MutableLiveData<Uri> localImageUri = new MutableLiveData<>();
    private Book oldBook;
    // the boolean indicating whether we should remove the image on save
    // also represents if there should be a cover image displayed
    private boolean hasImage = false;

    // production
    public AddEditViewModel() {
        this(AuthRepository.getInstance(), BookRepository.getInstance());
    }

    // test - allow us to inject repository dependecy in test
    public AddEditViewModel(IAuthRepository authRepository, IBookRepository bookRepository) {
        this.authRepository = authRepository;
        this.bookRepository = bookRepository;
    }

    // let the activity to bind received book data into the view model
    public void bindBook(Book book) {
        bookIsbn.setValue(book.getIsbn());
        bookTitle.setValue(book.getTitle());
        bookAuthor.setValue(book.getAuthor());
        bookDescription.setValue(book.getDescription());
        this.oldBook = book;
    }

    public Book getOldBook() {
        return oldBook;
    }

    public LiveData<Uri> getLocalImageUri() {
        return localImageUri;
    }

    public void setLocalImageUri(Uri uri) {
        localImageUri.setValue(uri);
        setHasImage(uri != null); // if the local uri is null, it means users deleted image
    }

    // exposed for Glide callback to tell if remote image is available on first load
    public void setHasImage(Boolean hasImage) {
        this.hasImage = hasImage;
    }

    // used to create dialog based on whether they have cover
    public boolean hasImage() {
        return hasImage;
    }

    /**
     * handles the add book functionality when user clicks the "Save" button in AddEditActivity
     * adds the book to the "book" collection and the image to FireBase Cloud Storage(if there is an image)
     *
     * @param successCallback async callback that is called upon successfully completing all operations
     * @param failCallback    async callback that is called if any operation failed
     */
    public void handleAddBook(
            OnSuccessCallbackWithMessage<Book> successCallback, OnFailCallbackWithMessage<BOOK_ERROR> failCallback
    ) {
        final String isbn = bookIsbn.getValue();
        final String title = bookTitle.getValue();
        final String author = bookAuthor.getValue();
        final String description = bookDescription.getValue();
        final Uri imageUri = localImageUri.getValue();

        String owner = authRepository.getCurrentUser().getUsername();
        bookRepository.add(isbn, title, author, description, owner).addOnSuccessListener(
                id -> {
                    Book book = new Book(id, isbn, title, author, description, owner, BOOK_STATUS.AVAILABLE);
                    if (imageUri != null) {
                        bookRepository.addImage(book.getCoverImageName(), imageUri).addOnCompleteListener(
                                addImageTask -> {
                                    if (addImageTask.isSuccessful()) {
                                        successCallback.run(book);
                                    } else {
                                        failCallback.run(BOOK_ERROR.FAIL_TO_ADD_IMAGE);
                                    }
                                }
                        );
                    } else {
                        successCallback.run(book);
                    }
                }
        ).addOnFailureListener(e -> {
            failCallback.run(BOOK_ERROR.FAIL_TO_ADD_BOOK);
        });
    }

    /**
     * handles the edit book functionality when user clicks the "Save" button in AddEditActivity
     * edit the book to the "book" collection and the image to FireBase Cloud Storage(if there is an image)
     *
     * @param successCallback async callback that is called upon successfully completing all operations
     * @param failCallback    async callback that is called if any operation failed
     */
    public void handleEditBook(
            OnSuccessCallbackWithMessage<Book> successCallback, OnFailCallbackWithMessage<BOOK_ERROR> failCallback
    ) {
        final String isbn = bookIsbn.getValue();
        final String title = bookTitle.getValue();
        final String author = bookAuthor.getValue();
        final String description = bookDescription.getValue();
        final Uri newUri = localImageUri.getValue();

        bookRepository.editBook(oldBook, isbn, title, author, description).addOnSuccessListener(
                newBook -> {
                    // addImage will also replace if file with imageName already exist
                    if (newUri != null) {
                        // when the image is updated
                        bookRepository.addImage(newBook.getCoverImageName(), newUri).addOnCompleteListener(
                                addImageTask -> {
                                    if (addImageTask.isSuccessful()) {
                                        successCallback.run(newBook);
                                    } else {
                                        failCallback.run(BOOK_ERROR.FAIL_TO_ADD_IMAGE);
                                    }
                                }
                        );
                    } else {
                        if (!hasImage) {
                            bookRepository.deleteImage(newBook.getCoverImageName())
                                    .addOnCompleteListener(Void -> {
                                        successCallback.run(newBook);
                                    });
                        } else {
                            successCallback.run(newBook);
                        }
                    }
                }
        ).addOnFailureListener(e -> {
            failCallback.run(BOOK_ERROR.FAIL_TO_EDIT_BOOK);
        });
    }
}
