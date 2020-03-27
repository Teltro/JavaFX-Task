package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

// класс для связи объекта данных с элементами интерфейса
public class PersonDTO{
    private int id;

    private SimpleStringProperty name;
    private SimpleIntegerProperty age;
    private SimpleObjectProperty<LocalDate> birthday;

    public PersonDTO(int id, String name, int age, LocalDate birthday) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.age = new SimpleIntegerProperty(age);
        this.birthday = new SimpleObjectProperty(birthday);
    }

    public PersonDTO(Person person) {
        id = person.getId();
        name = new SimpleStringProperty(person.getName());
        age = new SimpleIntegerProperty(person.getAge());
        birthday = new SimpleObjectProperty(person.getBirthday());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getAge() {
        return age.get();
    }

    public SimpleIntegerProperty ageProperty() {
        return age;
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    public Object getBirthday() {
        return birthday.get();
    }

    public SimpleObjectProperty<LocalDate> birthdayProperty() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday.set(birthday);
    }
}