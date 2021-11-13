package com.lukman.people;

import com.github.javafaker.Faker;
import com.lukman.people.model.PeopleWorkbook;
import com.lukman.people.model.Person;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.InternalServerErrorException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@ApplicationScoped
public class PersonService {

    private List<Person> people;
    private PeopleWorkbook workbook;

    public byte[] exportToExcel() {
        workbook = new PeopleWorkbook(dummyColumnNames());
        people = dummyCustomers();
        people.forEach(workbook::addTableData);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.writeTo(outputStream);
            return outputStream.toByteArray();
        } catch (IOException ex) {
            log.error("An IO error occurred: {}", ex.getMessage());
            throw new InternalServerErrorException("Unable to generate report");
        }
    }

    private List<Person> dummyCustomers() {
        List<Person> people = new ArrayList<>();
        Faker faker = new Faker();
        for (int i = 1; i <= 1000; i++) {
            people.add(new Person(
                    faker.idNumber().ssnValid(),
                    faker.name().firstName(),
                    faker.name().lastName(),
                    faker.internet().emailAddress(),
                    ThreadLocalRandom.current().nextInt(18, 61), //ages 18 to 60
                    ThreadLocalRandom.current().nextInt(1, 10_001), //min of 1 item, max 10K
                    ThreadLocalRandom.current().nextDouble(2, 1_000_001), //min $2, max $1M
                    faker.bool().bool()));
        }
        return people;
    }

    private List<String> dummyColumnNames() {
        return List.of("SSN", "First Name", "Last Name", "E-mail",
                        "Age", "No. of Items", "Total Spent", "Verified");
    }
}
