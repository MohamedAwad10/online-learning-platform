package com.onlinelearning.online_learning_platform.service.enrollment;

import com.onlinelearning.online_learning_platform.exception.EnrollmentException;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.Student;
import com.onlinelearning.online_learning_platform.model.enrollment.Enrollment;
import com.onlinelearning.online_learning_platform.model.enrollment.EnrollmentID;
import com.onlinelearning.online_learning_platform.repository.EnrollmentRepository;
import com.onlinelearning.online_learning_platform.service.course.CourseHandlerService;
import com.onlinelearning.online_learning_platform.service.user.StudentValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EnrollmentService {

    private EnrollmentRepository enrollmentRepository;

    private CourseHandlerService courseHandlerService;

    private StudentValidator studentValidator;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, CourseHandlerService courseHandlerService
            , StudentValidator studentValidator) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseHandlerService = courseHandlerService;
        this.studentValidator = studentValidator;
    }

    @Transactional
    public String enrollIn(Integer courseId, Integer studentId) {

        Course course = courseHandlerService.checkApprovedCourseExist(courseId);
        Student student = studentValidator.checkStudentExist(studentId);

        EnrollmentID enrollmentID = new EnrollmentID(student, course);
        Optional<Enrollment> optionalEnrollment = enrollmentRepository.findById(enrollmentID);
        if(optionalEnrollment.isPresent()){
            throw new EnrollmentException("You are already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setStudent(student);

        enrollmentRepository.save(enrollment);
        return "Enrolled in successfully in Course with ID: "+ course.getId();
    }
}
