package db;

import javafx.beans.NamedArg;
import model.Person;

import java.util.List;

public interface IPersonDAO {
    List<Person> getList();
    List<Person> getItemsAfterId (int id);
    Person get(int id);
    void add(Person person);
    void update(Person person);
    void delete(int id);
}
