package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "hostel_mess_menus", schema = "operations", indexes = {
        @Index(name = "idx_mess_day_meal", columnList = "tenant_id, day_of_week, meal_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostelMessMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "day_of_week", nullable = false)
    private String dayOfWeek; // MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY

    @Column(name = "meal_type", nullable = false)
    private String mealType; // BREAKFAST, LUNCH, EVENING_SNACK, DINNER

    @Column(name = "menu_items", length = 1500, nullable = false)
    private String menuItems;

    @Column(name = "dietary_type", nullable = false)
    private String dietaryType; // VEGETARIAN, NON_VEGETARIAN, JAIN, VEGAN

    @Column(name = "allergens_contained")
    private String allergensContained; // e.g. Dairy, Gluten, Peanuts

    @Column(name = "special_diet_note")
    private String specialDietNote;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
