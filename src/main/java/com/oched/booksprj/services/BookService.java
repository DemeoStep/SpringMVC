package com.oched.booksprj.services;

import com.oched.booksprj.entities.AuthorEntity;
import com.oched.booksprj.entities.BookDescriptionEntity;
import com.oched.booksprj.requests.AddBookRequest;
import com.oched.booksprj.repositories.AuthorRepository;
import com.oched.booksprj.repositories.BookRepository;
import com.oched.booksprj.requests.DeleteOrEditBookRequest;
import com.oched.booksprj.responses.BookResponse;
import lombok.RequiredArgsConstructor;
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

            //Это ж специально было пропущено, да? ;)
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

    public void deleteBook(DeleteOrEditBookRequest request) {
        bookRepository.getBooks().remove(request.getId());
    }

    public void editBook(AddBookRequest request) {
        BookDescriptionEntity book = bookRepository.getBooks().get(request.getId());
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

        book.setTitle(request.getTitle());
        book.setYear(request.getYear());
        book.setAuthorEntity(authorEntity);

    }

    public BookResponse getById(int id) {
        BookDescriptionEntity book = bookRepository.getBooks().get(id);
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getYear(),
                book.getAuthorEntity().getFirstName(),
                book.getAuthorEntity().getLastName()
        );
    }

    public List<BookResponse> getAll() {
        List<BookDescriptionEntity> bookList = bookRepository.getBooks();

        return bookList.stream().map(
                book -> new BookResponse(
                        book.getId(),
                        book.getTitle(),
                        book.getYear(),
                        book.getAuthorEntity().getFirstName(),
                        book.getAuthorEntity().getLastName()
                )).collect(Collectors.toList());
    }
}
