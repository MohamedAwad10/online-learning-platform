package com.onlinelearning.online_learning_platform.service.user;

import com.onlinelearning.online_learning_platform.exception.UserNotFoundException;
import com.onlinelearning.online_learning_platform.model.Student;
import com.onlinelearning.online_learning_platform.repository.StudentRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StudentValidator {

    private StudentRepository studentRepository;

    public StudentValidator(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student checkStudentExist(Integer studId) {
        Optional<Student> optionalStudent = studentRepository.findById(studId);
        if(optionalStudent.isEmpty()){
            throw new UserNotFoundException("User not found");
        }

        return optionalStudent.get();
    }
}
