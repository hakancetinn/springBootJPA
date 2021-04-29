package com.example.springbootjpa;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@SpringBootApplication
public class SpringbootjpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootjpaApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository,
                                        StudentIdCardRepository studentIdCardRepository) {
        return args -> {
            Faker faker = new Faker();

            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@gmail.com", firstName, lastName);

            Student student = new Student(
                    firstName,
                    lastName,
                    email,
                    faker.number().numberBetween(17, 55));

            student.addBook(
                    new Book("Clean Code", LocalDateTime.now().minusDays(4)));

            student.addBook(
                    new Book("Think and Grow Rich", LocalDateTime.now()));

            student.addBook(
                    new Book("Spring Data JPA", LocalDateTime.now().minusYears(1)));

            StudentIdCard studentIdCard = new StudentIdCard(
                    "123456789",
                    student
            );

            student.setStudentIdCard(studentIdCard);

            student.enrolToCourse(
                    new Course("Computer Science", "IT"));

            student.enrolToCourse(
                    new Course("Spring Data JPA", "IT"));

            studentRepository.save(student);

            studentRepository.findById(1L)
                    .ifPresent(s -> {
                        System.out.println("fetch book lazy...");
                        List<Book> books = student.getBooks();
                        books.forEach(book -> {
                            System.out.println(s.getFirstName() + " borrowed " + book.getBookName());
                        });
                    });
        };
    }
}
