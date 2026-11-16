package com.schoolerp.modules.operations.service;

import com.schoolerp.modules.operations.dto.AllergenCheckRequest;
import com.schoolerp.modules.operations.dto.AllergenCheckResultDTO;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.entity.StudentHealthPassport;
import com.schoolerp.modules.students.repository.StudentHealthPassportRepository;
import com.schoolerp.modules.students.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CafeteriaAllergenService {

    private final StudentHealthPassportRepository healthPassportRepository;
    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public AllergenCheckResultDTO checkAllergens(AllergenCheckRequest req) {
        Student student = studentRepository.findById(req.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + req.getStudentId()));

        Optional<StudentHealthPassport> passportOpt = healthPassportRepository.findByStudentId(req.getStudentId());

        AllergenCheckResultDTO result = new AllergenCheckResultDTO();
        result.setStudentId(student.getId());
        result.setStudentName(student.getFullName());

        if (passportOpt.isEmpty()) {
            result.setAllowed(true);
            result.setMatchedAllergens(Collections.emptyList());
            result.setWarningMessage("No medical allergen profile on file. Transaction permitted.");
            return result;
        }

        StudentHealthPassport passport = passportOpt.get();
        result.setBloodGroup(passport.getBloodGroup());
        result.setEpiPenRequired(passport.isEpipenPrescribed());
        result.setEmergencyProtocol(passport.getParamedicEmergencyProtocol());

        Set<String> restrictedSet = new HashSet<>();
        if (passport.getAllergenList() != null) {
            for (String a : passport.getAllergenList().split(",")) {
                if (!a.trim().isEmpty()) {
                    restrictedSet.add(a.trim().toLowerCase());
                }
            }
        }
        if (passport.getChronicConditions() != null) {
            for (String c : passport.getChronicConditions().split(",")) {
                if (!c.trim().isEmpty()) {
                    restrictedSet.add(c.trim().toLowerCase());
                }
            }
        }

        List<String> matched = new ArrayList<>();
        if (req.getItemIngredients() != null) {
            for (String ingredient : req.getItemIngredients()) {
                String ingClean = ingredient.trim().toLowerCase();
                for (String restricted : restrictedSet) {
                    if (ingClean.contains(restricted) || restricted.contains(ingClean)) {
                        matched.add(ingredient);
                        break;
                    }
                }
            }
        }

        result.setMatchedAllergens(matched);
        if (!matched.isEmpty()) {
            result.setAllowed(false);
            result.setWarningMessage("CRITICAL ALLERGEN HAZARD: " + student.getFullName() +
                    " has documented severe reaction to " + matched + ". Purchase intercepted and blocked!");
            log.warn("Cafeteria POS allergen hazard intercepted: Student {}, Item {}, Allergens {}",
                    student.getFullName(), req.getItemName(), matched);
        } else {
            result.setAllowed(true);
            result.setWarningMessage("Safe to dispense: No ingredient matches student allergen profile.");
        }

        return result;
    }
}
