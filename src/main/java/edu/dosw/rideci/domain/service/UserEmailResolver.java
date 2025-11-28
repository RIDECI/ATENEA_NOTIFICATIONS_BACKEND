package edu.dosw.rideci.domain.service;

/**
 * Puerto de dominio responsable de resolver la dirección de correo electrónico
 * de un usuario a partir de su identificador dentro del sistema.
 *
 * Este componente permite que la capa de dominio dependa únicamente de una
 * abstracción, mientras que la implementación concreta puede residir en la
 * capa de infraestructura (por ejemplo, consumiendo el microservicio de
 * administración o una base de datos local).
 *
 * @author RideECI
 * @version 1.0
 */
public interface UserEmailResolver {

    /**
     * Retorna el correo electrónico asociado al usuario con el ID dado.
     *
     * @param userId Identificador único del usuario dentro del sistema.
     * @return Correo electrónico del usuario, o {@code null} si no existe.
     */
    String resolveEmail(String userId);
}
