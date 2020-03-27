package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Person {
    private int id;
    private String name;
    private int age;
    private LocalDate birthday;
    // поле нужно для MySql: при сохранени поля birthday типа LocalDate
    // в бд сохранялась дата со значением на день раньше
    private String stringBirthday;

    public Person() {
    }

    public Person(String name, int age, LocalDate birthday) {
        this.name = name;
        this.age = age;
        setBirthday(birthday);
    }

    public Person(PersonDTO personDTO) {
        id = personDTO.getId();
        name = personDTO.getName();
        age = personDTO.getAge();
        setBirthday((LocalDate)personDTO.getBirthday());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        stringBirthday = dateTimeFormatter.format(birthday);
    }

    private void setStringBirthday(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        birthday = LocalDate.parse(date, dateTimeFormatter);
    }
}
