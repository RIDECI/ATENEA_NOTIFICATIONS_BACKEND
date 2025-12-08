package edu.dosw.rideci.infrastructure.resolver;

import edu.dosw.rideci.domain.service.UserEmailResolver;
import org.springframework.stereotype.Component;

/**
 * Implementación temporal de UserEmailResolver para pruebas.

 * Por ahora, ignora el userId y siempre retorna el mismo correo
 * de destino para verificar el envío de notificaciones por email.

 * En una versión real, aquí se debería llamar al microservicio
 * de administración/usuarios para obtener el correo asociado al userId.
 */
@Component
public class UserEmailResolverImpl implements UserEmailResolver {

    @Override
    public String resolveEmail(String userId) {
        if ("Raquel".equalsIgnoreCase(userId)) {
            return "raquel.selma-a@mail.escuelaing.edu.co";
        }
        if ("Juan".equalsIgnoreCase(userId)) {
            return "juan.nieto.co@mail.escuelaing.edu.co";
        }
        if ("Néstor".equalsIgnoreCase(userId)) {
            return "Nestor.lopez-c@mail.escuelaing.edu.co";
        }
        if ("Robinson".equalsIgnoreCase(userId)) {
            return "Robinson.nunez-p@mail.escuelaing.edu.co";
        }

        return "raquel.selma-a@mail.escuelaing.edu.co";
    }
}
