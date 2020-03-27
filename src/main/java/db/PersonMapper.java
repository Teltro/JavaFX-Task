package db;

import model.Person;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PersonMapper {
    @Results(id = "Person", value = {
            @Result(property = "id", column = "person_id"),
            @Result(property = "name", column = "person_name"),
            @Result(property = "age", column = "person_age"),
            @Result(property = "stringBirthday", column = "person_birthday")
    })

    @Select("SELECT * from persons")
    List<Person> selectList();

    @ResultMap("Person")
    @Select("SELECT * from persons WHERE person_id = #{id}")
    Person select(int id);

    @ResultMap("Person")
    @Select("SELECT * from persons WHERE person_id > #{id}")
    List<Person> selectItemsAfterId(int id);

    @Insert("INSERT into persons(person_name, person_age, person_birthday) VALUES(#{name}, #{age}, #{stringBirthday})")
    void insert(Person person);

    @Update("UPDATE persons SET person_name=#{name}, person_age=#{age}, person_birthday=#{stringBirthday} WHERE person_id=#{id}")
    void update(Person person);

    @Delete("DELETE FROM persons WHERE person_id=#{id}")
    void delete(int id);

}
