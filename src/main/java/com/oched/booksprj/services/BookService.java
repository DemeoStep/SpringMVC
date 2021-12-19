package com.oched.booksprj.services;

import com.oched.booksprj.entities.AuthorEntity;
import com.oched.booksprj.entities.BookContentEntity;
import com.oched.booksprj.entities.BookDescriptionEntity;
import com.oched.booksprj.exceptions.BadRequestException;
import com.oched.booksprj.repositories.BookContentRepository;
import com.oched.booksprj.requests.ActionRequest;
import com.oched.booksprj.requests.EditBookRequest;
import com.oched.booksprj.requests.NewBookRequest;
import com.oched.booksprj.repositories.AuthorRepository;
import com.oched.booksprj.repositories.BookRepository;
import com.oched.booksprj.responses.BookInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

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

    public void addBook(NewBookRequest request) {
        Optional<AuthorEntity> optionalAuthor = authorRepository.findByFirstNameAndLastName(
                request.getAuthorFirstName(),
                request.getAuthorLastName()
        );

        AuthorEntity author;

        if(optionalAuthor.isPresent()) {
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

    public List<BookInfoResponse> getAll() {
        List<BookDescriptionEntity> list = bookRepository.findAll();

        return list.stream().map(book -> new BookInfoResponse(
                book.getId(),
                book.getTitle(),
                book.getYear(),
                book.getAuthor().getFirstName(),
                book.getAuthor().getLastName()
        )).collect(Collectors.toList());
    }

    public void deleteBook(ActionRequest request) { // А так можно?
        try {
            this.bookRepository.deleteById(request.getId());
        } catch (Exception e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "No book with id = " + request.getId());
        }
    }

    public void editBook(EditBookRequest request) {
        Optional<BookDescriptionEntity> optional = this.bookRepository.findById(request.getId());

        BookDescriptionEntity book = optional.orElseThrow(
                () -> new BadRequestException("No book with id = " + request.getId(), HttpStatus.BAD_REQUEST)
        );

        Optional<AuthorEntity> optionalAuthor = this.authorRepository.findByFirstNameAndLastName(
                request.getAuthorFirstName(),
                request.getAuthorLastName()
        );

        AuthorEntity author;

        if(optionalAuthor.isPresent()) {
            author = optionalAuthor.get();
        } else {
            author = new AuthorEntity(
                    request.getAuthorFirstName(),
                    request.getAuthorLastName()
            );

            authorRepository.save(author);
        }

        book.setAuthor(author);
        book.setTitle(request.getTitle());
        book.setYear(request.getYear());

        this.bookRepository.save(book);
    }

    public BookInfoResponse getById(ActionRequest request) {
        Optional<BookDescriptionEntity> optional = this.bookRepository.findById(request.getId());

        BookDescriptionEntity book = optional.orElseThrow(
                () -> new BadRequestException("No book with id = " + request.getId(), HttpStatus.BAD_REQUEST)
        );

        return new BookInfoResponse(
                book.getId(),
                book.getTitle(),
                book.getYear(),
                book.getAuthor().getFirstName(),
                book.getAuthor().getLastName()
        );
    }
}
