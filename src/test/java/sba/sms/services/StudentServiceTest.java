package sba.sms.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.CommandLine;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

@FieldDefaults(level = AccessLevel.PRIVATE)
class StudentServiceTest {

	static StudentService studentService;
	static CourseService courseService;

	@BeforeAll
	static void beforeAll() {
		studentService = new StudentService();
		courseService = new CourseService();
		CommandLine.addData();
	}

	@Test
	@Order(1)
	void getAllStudents() {

		List<Student> expected = new ArrayList<>(Arrays.asList(
				new Student("reema@gmail.com", "reema brown", "password"),
				new Student("annette@gmail.com", "annette allen", "password"),
				new Student("anthony@gmail.com", "anthony gallegos", "password"),
				new Student("ariadna@gmail.com", "ariadna ramirez", "password"),
				new Student("bolaji@gmail.com", "bolaji saibu", "password")
				));

		assertThat(expected).hasSameElementsAs(studentService.getAllStudents());

	}

	@Test
	void getStudentByEmail() {
		Student expected = new Student("reema@gmail.com", "reema brown", "password");
		Student getResult = studentService.getStudentByEmail("reema@gmail.com");
		assertThat(expected).isEqualTo(getResult);

	}


	@Test
	void createStudent() {
		Student expected = new Student("123@gmail.com", "Li Chen", "password");
		studentService.createStudent(expected);
		Student getResult = studentService.getStudentByEmail("123@gmail.com");
		assertThat(expected.equals(getResult));

	}

	@Test
	void validateStudent() {
		boolean getResult = studentService.validateStudent("reema@gmail.com", "password");
		assertThat(getResult).isTrue();
	}

	@Test
	void registerStudentToCourse() {
		studentService.registerStudentToCourse("reema@gmail.com", 3);
		Course addedCourse = courseService.getCourseById(3);
		List<Course> expected = studentService.getStudentCourses("reema@gmail.com");
		assertThat(expected.get(0)).isEqualTo(addedCourse);
	}

}