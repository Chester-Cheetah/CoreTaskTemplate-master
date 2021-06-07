package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.function.Function;


public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        String SQL = "CREATE TABLE IF NOT EXISTS pre_project.Users (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(20), lastName VARCHAR(20), age TINYINT)";
        execute(session -> {
            session.createSQLQuery(SQL).executeUpdate();
            return null;
        });
    }

    @Override
    public void dropUsersTable() {
        String SQL = "DROP TABLE IF EXISTS pre_project.Users";
        execute(session -> {
            session.createSQLQuery(SQL).executeUpdate();
            return null;
        });
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        execute(session -> {
            session.save(user);
            System.out.printf("User с именем %s добавлен в базу данных\n", name);
            return null;
        });
    }

    @Override
    public void removeUserById(long id) {
        execute(session -> {
            Query query = session.createQuery("DELETE User where id = :userID").setParameter("userID", id);
            query.executeUpdate();
            return null;
        });
    }

    @Override
    public List<User> getAllUsers() {
        return execute(session -> session.createQuery("from User").getResultList());
    }

    @Override
    public void cleanUsersTable() {
        execute(session -> {
            session.createQuery("DELETE User").executeUpdate();
            return null;
        });
    }

    private List execute (Function<Session, List> executor) {
        List userList;
        try (SessionFactory factory = Util.getSessionFactory(); Session session = factory.getCurrentSession()){
            session.beginTransaction();
            userList = executor.apply(session);
            session.getTransaction().commit();
        }
        return userList;
    }
}
