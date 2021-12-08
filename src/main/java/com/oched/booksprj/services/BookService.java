package com.oched.booksprj.services;

import com.oched.booksprj.entities.AuthorEntity;
import com.oched.booksprj.entities.BookContentEntity;
import com.oched.booksprj.entities.BookDescriptionEntity;
import com.oched.booksprj.repositories.BookContentRepository;
import com.oched.booksprj.requests.AddBookRequest;
import com.oched.booksprj.repositories.AuthorRepository;
import com.oched.booksprj.repositories.BookRepository;
import com.oched.booksprj.requests.DeleteOrEditBookRequest;
import com.oched.booksprj.responses.BookResponseNoContent;
import com.oched.booksprj.responses.BookResponseWithContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookContentRepository contentRepository;

    public void addBook(AddBookRequest request) {
        Optional<AuthorEntity> optionalAuthor = authorRepository.findByFirstNameAndLastName(
                request.getAuthorFirstName(),
                request.getAuthorLastName()
        );

        AuthorEntity author;

        if (optionalAuthor.isPresent()) {
            author = optionalAuthor.get();
        } else {
            author = new AuthorEntity(
                    request.getAuthorFirstName(),
                    request.getAuthorLastName()
            );

            authorRepository.save(author);
        }

        BookContentEntity bookContent = new BookContentEntity(request.getContent());

        contentRepository.save(bookContent);

        BookDescriptionEntity newBook = new BookDescriptionEntity(
                request.getTitle(),
                request.getYear(),
                author,
                bookContent
        );

        bookRepository.save(newBook);
    }

    public void deleteBook(DeleteOrEditBookRequest request) {
        BookDescriptionEntity book = bookRepository.getById(request.getId());
        bookRepository.delete(book);
        contentRepository.delete(book.getContent());
    }

    public void editBook(AddBookRequest request) {
        BookDescriptionEntity book = bookRepository.getById(request.getId());

        Optional<AuthorEntity> optionalAuthor = authorRepository.findByFirstNameAndLastName(
                request.getAuthorFirstName(),
                request.getAuthorLastName()
        );

        AuthorEntity author;

        if (optionalAuthor.isEmpty()) {
            author = new AuthorEntity(
                    request.getAuthorFirstName(),
                    request.getAuthorLastName());
            authorRepository.save(author);
        } else {
            author = optionalAuthor.get();
        }

        if (!book.getAuthor().equals(author)) {
            book.setAuthor(author);
        }

        BookContentEntity content = contentRepository.getById(book.getContent().getId());

        if (!content.getContent().equals(request.getContent())) {
            content.setContent(request.getContent());
            contentRepository.save(content);
        }

        if (!book.getTitle().equals(request.getTitle())) {
            book.setTitle(request.getTitle());
        }

        if (book.getYear() != request.getYear()) {
            book.setYear(request.getYear());
        }
    }

    public List<BookResponseNoContent> getAll() {
        List<BookDescriptionEntity> list = bookRepository.findAll();

        return list.stream().map(book -> new BookResponseNoContent(
                book.getId(),
                book.getTitle(),
                book.getYear(),
                book.getAuthor().getFirstName(),
                book.getAuthor().getLastName()
        )).collect(Collectors.toList());
    }

    public BookResponseWithContent getById(long id) {
        BookDescriptionEntity book = bookRepository.getById(id);

        return new BookResponseWithContent(
                book.getId(),
                book.getTitle(),
                book.getYear(),
                book.getAuthor().getFirstName(),
                book.getAuthor().getLastName(),
                book.getContent().getContent()
        );
    }
}
