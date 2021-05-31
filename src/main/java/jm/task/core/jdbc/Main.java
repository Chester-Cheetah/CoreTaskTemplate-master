package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserService service = new UserServiceImpl();
        service.createUsersTable();

        service.saveUser("Евгений", "Дорджиев", (byte)30);


        service.saveUser("Герман", "Севастьянов", (byte)30);


        service.saveUser("Семён", "Фомичев", (byte)30);


        service.saveUser("Михаил", "Клянскис", (byte)30);

        service.getAllUsers().forEach(System.out :: println);
        service.cleanUsersTable();
        service.dropUsersTable();
    }
}
