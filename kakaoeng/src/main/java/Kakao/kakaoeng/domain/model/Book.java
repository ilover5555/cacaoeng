package Kakao.kakaoeng.domain.model;

import Kakao.kakaoeng.domain.model.Student.Level;

public class Book implements Comparable<Book>{
	public enum Course{
		TypeEasy("초급/주니어"), TypeMiddle("중급/시니어"), TypeBussiness("Business"), TypeExam("시험대비"), TypeFreeTalk("Free Talking"), TypeElementBook("미국초등교과서"), LevelTest("레벨테스트");
		
		private String displayName;
		
		Course(String displayName){
			this.displayName = displayName;
		}
		
		public String getDisplayName(){
			return this.displayName;
		}
		
		public String getClassType(){
			return "class"+this.name();
		}
	}
	
	int id;
	Course course;
	Level level;
	String title;
	boolean disabled;
	String bookLink;
	String bookPicture;
	UploadedFile bookPictureFile;
	public Book(int id, Course course, Level level, String title, boolean disabled, String bookLink,
			String bookPicture) {
		super();
		this.id = id;
		this.course = course;
		this.level = level;
		this.title = title;
		this.disabled = disabled;
		this.bookLink = bookLink;
		this.bookPicture = bookPicture;
	}
	public int getId() {
		return id;
	}
	public Course getCourse() {
		return course;
	}
	public Level getLevel() {
		return level;
	}
	public String getTitle() {
		return title;
	}
	public boolean getDisabled() {
		return disabled;
	}
	public String getBookLink() {
		return bookLink;
	}
	public String getBookPicture() {
		return bookPicture;
	}
	public UploadedFile getBookPictureFile(){
		if(bookPictureFile == null)
			bookPictureFile = new UploadedFile(bookPicture);
		return bookPictureFile;
	}
	@Override
	public int compareTo(Book o) {
		return this.getCourse().compareTo(o.getCourse());
	}
	
}
