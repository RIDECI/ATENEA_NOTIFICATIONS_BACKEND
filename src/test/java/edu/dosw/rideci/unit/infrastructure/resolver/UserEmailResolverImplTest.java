package edu.dosw.rideci.unit.infrastructure.resolver;

import edu.dosw.rideci.infrastructure.resolver.UserEmailResolverImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserEmailResolverImplTest {

    private UserEmailResolverImpl resolver;

    @BeforeEach
    void setUp() {
        resolver = new UserEmailResolverImpl();
    }

    @Test
    void resolveEmail_ShouldReturnRaquelEmailForRaquel() {
        String email = resolver.resolveEmail("Raquel");

        assertEquals("raquel.selma-a@mail.escuelaing.edu.co", email);
    }

    @Test
    void resolveEmail_ShouldReturnRaquelEmailForRaquelCaseInsensitive() {
        String email = resolver.resolveEmail("raquel");

        assertEquals("raquel.selma-a@mail.escuelaing.edu.co", email);
    }

    @Test
    void resolveEmail_ShouldReturnJuanEmailForJuan() {
        String email = resolver.resolveEmail("Juan");

        assertEquals("juan.nieto.co@mail.escuelaing.edu.co", email);
    }

    @Test
    void resolveEmail_ShouldReturnNestorEmailForNestor() {
        String email = resolver.resolveEmail("Néstor");

        assertEquals("Nestor.lopez-c@mail.escuelaing.edu.co", email);
    }

    @Test
    void resolveEmail_ShouldReturnRobinsonEmailForRobinson() {
        String email = resolver.resolveEmail("Robinson");

        assertEquals("Robinson.nunez-p@mail.escuelaing.edu.co", email);
    }

    @Test
    void resolveEmail_ShouldReturnDefaultEmailForUnknownUser() {
        String email = resolver.resolveEmail("UnknownUser");

        assertEquals("raquel.selma-a@mail.escuelaing.edu.co", email);
    }

    @Test
    void resolveEmail_ShouldReturnDefaultEmailForNull() {
        String email = resolver.resolveEmail(null);

        assertEquals("raquel.selma-a@mail.escuelaing.edu.co", email);
    }

    @Test
    void resolveEmail_ShouldReturnDefaultEmailForEmptyString() {
        String email = resolver.resolveEmail("");

        assertEquals("raquel.selma-a@mail.escuelaing.edu.co", email);
    }
}

