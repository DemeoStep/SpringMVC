package com.oched.booksprj.services;

import com.oched.booksprj.entities.AuthorEntity;
import com.oched.booksprj.entities.BookDescriptionEntity;
import com.oched.booksprj.requests.AddBookRequest;
import com.oched.booksprj.repositories.AuthorRepository;
import com.oched.booksprj.repositories.BookRepository;
import com.oched.booksprj.requests.DelBookRequest;
import com.oched.booksprj.responses.BookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public void addBook(AddBookRequest request) {
        List<BookDescriptionEntity> bookList = bookRepository.getBooks();
        List<AuthorEntity> authorEntities = authorRepository.getAuthorEntities();
        AuthorEntity authorEntity = null;

        for (AuthorEntity listAuthorEntity : authorEntities) {
            if (listAuthorEntity.getFirstName().equals(request.getAuthorFirstName()) &&
                    listAuthorEntity.getLastName().equals(request.getAuthorLastName())) {
                authorEntity = listAuthorEntity;
            }
        }

        if (authorEntity == null) {
            authorEntity = new AuthorEntity(
                    authorEntities.size(),
                    request.getAuthorFirstName(),
                    request.getAuthorLastName(),
                    null
            );
            authorRepository.getAuthorEntities().add(authorEntity);
        }

        bookList.add(
                new BookDescriptionEntity(
                        bookList.size(),
                        request.getTitle(),
                        request.getYear(),
                        authorEntity,
                        null
                )
        );
    }

    public void delBook(DelBookRequest request) {
        List<BookDescriptionEntity> bookList = bookRepository.getBooks();
        List<AuthorEntity> authorEntities = authorRepository.getAuthorEntities();
        AuthorEntity authorEntity = null;

        for (AuthorEntity listAuthorEntity : authorEntities) {
            if (listAuthorEntity.getFirstName().equals(request.getAuthorFirstName()) &&
                    listAuthorEntity.getLastName().equals(request.getAuthorLastName())) {
                authorEntity = listAuthorEntity;
            }
        }

        for (BookDescriptionEntity listBookEntity : bookList) {
            if (listBookEntity.getAuthorEntity() == authorEntity &&
                    listBookEntity.getYear() == request.getYear() &&
                    listBookEntity.getTitle().equals(request.getTitle())) {
                bookRepository.getBooks().remove(listBookEntity);
                break;
            }
        }
    }

    public List<BookResponse> getAll() {
        List<BookDescriptionEntity> bookList = bookRepository.getBooks();

        return bookList.stream().map(
                book -> new BookResponse(
                        book.getTitle(),
                        book.getYear(),
                        book.getAuthorEntity().getFirstName(),
                        book.getAuthorEntity().getLastName()
                )).collect(Collectors.toList());
    }
}
