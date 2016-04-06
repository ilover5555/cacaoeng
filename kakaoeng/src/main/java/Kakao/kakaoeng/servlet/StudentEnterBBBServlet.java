package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.LoginChecker;
import Kakao.kakaoeng.LoginRequiredException;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.Lecture.Status;
import Kakao.kakaoeng.domain.model.OneClass;
import Kakao.kakaoeng.domain.model.RegisterTime.DayOfWeek;
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.domain.model.Teacher;

@WebServlet("/main/studentEnterBBB.do")
public class StudentEnterBBBServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginChecker lc = new LoginChecker(req);
		if(!lc.getStudentLogin()){
			Util.sendError(resp, "로그인후 다시시도해주세요");
			return;
		}
		
		Date date = new Date();
		
		Student student = lc.getLoginStudentObject();
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		OneClassDAO oneClassDAO = (OneClassDAO) applicationContext.getBean("oneClassDAO");
		
		List<Lecture> lectureList = lectureDAO.getLectureListByStudentId(student.getId(), date, date);
		
		List<OneClass> oneClassList = new ArrayList<>();
		for(Lecture lecture : lectureList){
			if(!lecture.getStatus().equals(Status.OnGoing))
				continue;
			
			List<OneClass> list = oneClassDAO.getOneClassListByDayOfWeek(lecture.getId(), DayOfWeek.getInstanceFromDate(date));
			oneClassList.addAll(list);
		}
		
		OneClass result = null;
		Calendar cal = Calendar.getInstance();
		for(OneClass oneClass : oneClassList){
			cal.setTime(oneClass.getDuration().getStartDateObject());
			cal.add(Calendar.SECOND, -(60*5+30));
			Date start = cal.getTime();
			Date end = oneClass.getDuration().getEndDateObject();
			
			if((date.equals(start) || date.after(start)) &&
					(date.equals(end) || date.before(end))){
				result = oneClass;
			}
		}
		
		if(result == null){
			Util.sendError(resp, "진행될 수업이 없습니다.");
			return;
		}
		else{
			TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
			Teacher teacher = teacherDAO.getTeacherWithId(result.getTeacherId());
			String url ="http://bbb.kakaoenglish.kr/demo/create.jsp?username="+student.getClassName().replace(' ', '+')+"&meetingID="+teacher.getClassName().replace(' ', '+')+"%27s+meeting&action=enter";
			Util.sendSuccess(resp, url);
		}
	}

}
