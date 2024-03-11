package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private UserRepository userRepository;
    User user = new User();

    @BeforeEach
    void setUp() {
        user.setEmail("test@mail.ru");
        user.setName("test");
        em.persist(user);
    }
}