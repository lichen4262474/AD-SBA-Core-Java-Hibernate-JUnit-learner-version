package sba.sms.services;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

public class StudentService implements StudentI {

	@Override
	public List<Student> getAllStudents() {
		// persist student from database, also handle commit,rollback, and exceptions
		List<Student> studentList = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			studentList = session.createNativeQuery("select * from Student", Student.class).list();
			tx.commit();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return studentList;
	}

	@Override
	public void createStudent(Student student) {
		// persist student to database, also handle commit,rollback, and exceptions |
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			session.persist(student);
			tx.commit();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public Student getStudentByEmail(String email) {
		// return student if exists, also handle commit,rollback, and exceptions |
		Student student = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			student = session.get(Student.class, email);
			tx.commit();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return student;
	}

	@Override
	public boolean validateStudent(String email, String password) {
		// String email, String password | match email and password to database to
		// gain access to courses, also handle commit,rollback, and exceptions |
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		Boolean validation = false;
		try {
			Student student = session.get(Student.class, email);
			try {
				if (student.getPassword().equals(password)) {
					validation = true;
				}
			} catch (NullPointerException ex) {
				ex.printStackTrace();
			}

			tx.commit();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return validation;
	}

	@Override
	public void registerStudentToCourse(String email, int courseId) {
		// String email, int courseId | register a course to a student (collection
		// to prevent duplication), also handle commit,rollback, and exceptions |
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			Student student = session.get(Student.class, email);
			Course newCourse = session.get(Course.class, courseId);
			if (!student.getCourses().contains(newCourse)) {
				student.addCourse(newCourse);
			}
			session.merge(student);
			tx.commit();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public List<Course> getStudentCourses(String email) {
		// String email | get all the student courses list (use native query),
		// also handle commit,rollback, and exceptions
		List<Course> courseList = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			courseList = session.get(Student.class, email).getCourses();
			tx.commit();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return courseList;
	}

}
