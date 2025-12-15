package edu.dosw.rideci.unit.domain.repository;

import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.repository.InMemoryNotificationStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryNotificationStoreTest {

    private InMemoryNotificationStore notificationStore;
    private InAppNotification testNotification;
    private String testId;
    private UUID testUuid;

    @BeforeEach
    void setUp() {
        notificationStore = new InMemoryNotificationStore();
        testUuid = UUID.randomUUID();
        testId = testUuid.toString();
        testNotification = new InAppNotification();
        testNotification.setNotificationId(testUuid);
        testNotification.setMessage("Test message");
        testNotification.setTitle("Test Title");
    }

    @Test
    @DisplayName("Debería guardar una notificación con ID existente")
    void save_withExistingId_shouldSaveNotification() {
        // When
        InAppNotification saved = notificationStore.save(testNotification);

        // Then
        assertNotNull(saved);
        assertEquals(testId, saved.getNotificationId().toString());
        assertEquals("Test message", saved.getMessage());
    }

    @Test
    @DisplayName("Debería generar ID automáticamente cuando es null")
    void save_withNullId_shouldGenerateId() {
        // Given
        InAppNotification notification = new InAppNotification();
        notification.setMessage("Auto-generated ID test");
        notification.setTitle("Test");

        // When
        InAppNotification saved = notificationStore.save(notification);

        // Then
        assertNotNull(saved);
        assertNotNull(saved.getNotificationId());
        assertEquals("Auto-generated ID test", saved.getMessage());
    }

    @Test
    @DisplayName("Debería encontrar notificación por ID existente")
    void findById_withExistingId_shouldReturnNotification() {
        // Given
        notificationStore.save(testNotification);

        // When
        Optional<InAppNotification> found = notificationStore.findById(testId);

        // Then
        assertTrue(found.isPresent());
        assertEquals(testId, found.get().getNotificationId().toString());
    }

    @Test
    @DisplayName("Debería retornar Optional vacío para ID no existente")
    void findById_withNonExistingId_shouldReturnEmpty() {
        // When
        Optional<InAppNotification> found = notificationStore.findById("non-existing-id");

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Debería retornar todas las notificaciones")
    void findAll_withMultipleNotifications_shouldReturnAll() {
        // Given
        InAppNotification notification1 = new InAppNotification();
        notification1.setNotificationId(UUID.randomUUID());
        notification1.setTitle("Notif 1");

        InAppNotification notification2 = new InAppNotification();
        notification2.setNotificationId(UUID.randomUUID());
        notification2.setTitle("Notif 2");

        notificationStore.save(notification1);
        notificationStore.save(notification2);

        // When
        List<InAppNotification> allNotifications = notificationStore.findAll();

        // Then
        assertEquals(2, allNotifications.size());
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no hay notificaciones")
    void findAll_whenEmpty_shouldReturnEmptyList() {
        // When
        List<InAppNotification> allNotifications = notificationStore.findAll();

        // Then
        assertTrue(allNotifications.isEmpty());
    }

    @Test
    @DisplayName("Debería eliminar notificación existente")
    void delete_withExistingId_shouldRemoveNotification() {
        // Given
        notificationStore.save(testNotification);
        assertEquals(1, notificationStore.count());

        // When
        notificationStore.delete(testId);

        // Then
        assertEquals(0, notificationStore.count());
        assertFalse(notificationStore.findById(testId).isPresent());
    }

    @Test
    @DisplayName("Debería manejar eliminación de ID no existente sin errores")
    void delete_withNonExistingId_shouldDoNothing() {
        // Given
        notificationStore.save(testNotification);
        int initialCount = (int) notificationStore.count();

        // When
        notificationStore.delete("non-existing-id");

        // Then
        assertEquals(initialCount, notificationStore.count());
    }

    @Test
    @DisplayName("Debería contar correctamente las notificaciones")
    void count_withMultipleNotifications_shouldReturnCorrectCount() {
        // When/Then
        assertEquals(0, notificationStore.count());

        notificationStore.save(testNotification);
        assertEquals(1, notificationStore.count());

        InAppNotification anotherNotification = new InAppNotification();
        anotherNotification.setNotificationId(UUID.randomUUID());
        anotherNotification.setTitle("Another");
        notificationStore.save(anotherNotification);

        assertEquals(2, notificationStore.count());
    }

    @Test
    @DisplayName("Debería sobrescribir notificación con mismo ID")
    void save_withSameId_shouldOverwrite() {
        // Given
        notificationStore.save(testNotification);

        // When
        InAppNotification updatedNotification = new InAppNotification();
        updatedNotification.setNotificationId(testUuid);
        updatedNotification.setMessage("Updated message");
        updatedNotification.setTitle("Updated");
        notificationStore.save(updatedNotification);

        // Then
        assertEquals(1, notificationStore.count());
        Optional<InAppNotification> found = notificationStore.findById(testId);
        assertTrue(found.isPresent());
        assertEquals("Updated message", found.get().getMessage());
    }
}