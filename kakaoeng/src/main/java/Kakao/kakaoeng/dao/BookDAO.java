package Kakao.kakaoeng.dao;

import java.util.List;

import Kakao.kakaoeng.domain.model.Book;
import Kakao.kakaoeng.domain.model.Book.Course;
import Kakao.kakaoeng.domain.model.Student.Level;

public interface BookDAO {

	void register(Book book);

	List<Book> getAllBookList();

	void delete(int bookId);

	void updateDisabled(int bookId, boolean disabled);

	Book getBookById(int bookId);

	void updateBookPicture(int bookId, String DBPath);

	void updateBookLink(int bookId, String link);

	List<Book> getAllBookList(int start, int end);

	void update(int bookId, Course course, Level level, String title);

	List<Book> getEnabledBookList();

	List<Book> getBookListByCourse(Course course);

	Book getBookByTitle(String title);

}
