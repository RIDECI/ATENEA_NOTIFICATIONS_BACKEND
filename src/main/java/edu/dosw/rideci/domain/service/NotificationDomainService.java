package edu.dosw.rideci.domain.service;

import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import edu.dosw.rideci.domain.model.InAppNotification;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * Servicio de dominio para gestionar el ciclo de vida de las notificaciones in-app.
 * Se encarga de aplicar las reglas de negocio relacionadas con creación, lectura,
 * archivado y expiración.
 *
 * Este módulo no usa base de datos; los cambios se aplican directamente sobre
 * el objeto {@link InAppNotification}.
 *
 * @author RideECI
 * @version 1.1
 */
@Service
public class NotificationDomainService {

    /**
     * Inicializa una notificación con valores por defecto.
     * - Estado: UNREAD
     * - Fecha de creación: ahora (si no existe)
     *
     * @param notification Notificación a inicializar.
     * @return La notificación ya inicializada.
     */
    public InAppNotification initializeNotification(InAppNotification notification) {
        Objects.requireNonNull(notification, "notification must not be null");

        notification.setStatus(NotificationStatus.UNREAD);

        if (notification.getCreatedAt() == null) {
            notification.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        }

        return notification;
    }

    /**
     * Marca la notificación como leída.
     * - Estado: READ
     * - readAt: ahora
     *
     * @param notification Notificación a marcar como leída.
     * @return La notificación ya actualizada.
     */
    public InAppNotification markAsRead(InAppNotification notification) {
        Objects.requireNonNull(notification, "notification must not be null");

        notification.setStatus(NotificationStatus.READ);
        notification.setReadAt(OffsetDateTime.now(ZoneOffset.UTC));

        return notification;
    }

    /**
     * Archiva la notificación.
     * - Estado: ARCHIVED
     *
     * @param notification Notificación a archivar.
     * @return La notificación ya archivada.
     */
    public InAppNotification archive(InAppNotification notification) {
        Objects.requireNonNull(notification, "notification must not be null");

        notification.setStatus(NotificationStatus.ARCHIVED);
        return notification;
    }

    /**
     * Expira la notificación.
     * - Estado: EXPIRED
     * - expiresAt: ahora
     *
     * @param notification Notificación a expirar.
     * @return La notificación ya expirada.
     */
    public InAppNotification expire(InAppNotification notification) {
        Objects.requireNonNull(notification, "notification must not be null");

        notification.setStatus(NotificationStatus.EXPIRED);
        notification.setExpiresAt(OffsetDateTime.now(ZoneOffset.UTC));
        return notification;
    }

    /**
     * Indica si una notificación ya está expirada.
     *
     * @param notification Notificación a evaluar.
     * @return true si está expirada, false si no.
     */
    public boolean isExpired(InAppNotification notification) {
        return notification != null && notification.isExpired();
    }
}
