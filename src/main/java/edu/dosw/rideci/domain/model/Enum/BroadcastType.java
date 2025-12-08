package edu.dosw.rideci.domain.model.Enum;

/**
 * Tipos de broadcast extraordinarios que pueden ser enviados por administradores.
 * Estos eventos están orientados a comunicación masiva en situaciones críticas
 * o mensajes institucionales.
 */
public enum BroadcastType {

    /** Emergencia inmediata: amenazas, incidentes graves, seguridad, etc. */
    EMERGENCY,

    /** Alertas importantes que requieren atención rápida pero no inmediata. */
    HIGH_PRIORITY_ALERT,

    /** Avisos generales del sistema o la plataforma. */
    SYSTEM_ANNOUNCEMENT,

    /** Mantenimiento programado o interrupciones del servicio. */
    MAINTENANCE_NOTICE,

    /** Mensajes institucionales: políticas, comunicados oficiales, etc. */
    ADMIN_ANNOUNCEMENT,

    TEST_BROADCAST

}
