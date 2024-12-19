package com.onlinelearning.online_learning_platform.service.enrollment;

import com.onlinelearning.online_learning_platform.exception.EnrollmentException;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.Student;
import com.onlinelearning.online_learning_platform.model.enrollment.Enrollment;
import com.onlinelearning.online_learning_platform.model.enrollment.EnrollmentID;
import com.onlinelearning.online_learning_platform.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnrollmentHandlerService {

    private EnrollmentRepository enrollmentRepository;

    public EnrollmentHandlerService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public void checkStudentEnrolledInOrNot(Student student, Course course){
        EnrollmentID enrollmentID = new EnrollmentID(student, course);
        Optional<Enrollment> optionalEnrollment = enrollmentRepository.findById(enrollmentID);
        if(optionalEnrollment.isEmpty()){
            throw new EnrollmentException("You are not enroll in this course");
        }
    }
}
