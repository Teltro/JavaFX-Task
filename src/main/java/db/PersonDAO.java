package db;

import model.Person;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class PersonDAO implements IPersonDAO {
    public List<Person> getList() {
        List<Person> persons;
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            persons = mapper.selectList();
        }
        return persons;
    }

    // используется для получения новых данных(с большим значение id чем передаваемое в параметре):
    public List<Person> getItemsAfterId(int id) {
        List<Person> persons;
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            persons = mapper.selectItemsAfterId(id);
        }
        return persons;
    }

    public Person get(int id) {
        Person person;
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            person = mapper.select(id);
        }
        return person;
    }

    public void add(Person person) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            mapper.insert(person);
            session.commit();
        }
    }

    public void update(Person person) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            mapper.update(person);
            session.commit();
        }
    }

    public void delete(int id) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            mapper.delete(id);
            session.commit();
        }
    }
}