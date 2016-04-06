package Kakao.kakaoeng;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.dao.ClassLogDAO;
import Kakao.kakaoeng.dao.ClassTimeDAO;
import Kakao.kakaoeng.dao.HolidayDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.domain.model.ClassLog;
import Kakao.kakaoeng.domain.model.ClassTime;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.OneClass;
import Kakao.kakaoeng.domain.model.Teacher;

public class TeacherScheduleStateReader {

	public void process(ApplicationContext applicationContext, HttpServletRequest req, HttpServletResponse resp, Teacher teacher){
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		OneClassDAO oneClassDAO = (OneClassDAO) applicationContext.getBean("oneClassDAO");
		ClassLogDAO classLogDAO = (ClassLogDAO) applicationContext.getBean("classLogDAO");
		HolidayDAO holidayDAO = (HolidayDAO) applicationContext.getBean("holidayDAO");
		
		Date baseDate = Util.parseDateIgnoreNull((String) req.getParameter("baseDate"));
		List<Date> dateList = Util.getWeekDateList(baseDate);
		List<Lecture> lectureList = Util.getLectureList(dateList, lectureDAO, teacher.getId());
		Map<Lecture, List<OneClass>> oneClassMap = Util.getReservedOneClass(oneClassDAO, lectureList);
		List<OneClass> oneClassList = new ArrayList<>();
		Map<OneClass, ClassLog> map = new HashMap<>();
		for(Lecture lecture : oneClassMap.keySet()){
			List<OneClass> oneClass = oneClassMap.get(lecture);
			for(OneClass oc : oneClass){
				oc.setStartDate(lecture);
			}
			List<OneClass> afterStartDateList = Util.removeBeforeStartDate(lecture, oneClass, baseDate);
			for(Iterator<OneClass> iter = afterStartDateList.iterator(); iter.hasNext(); ){
				OneClass afterStartDate = iter.next();
				
				if(!afterStartDate.isBeforeEndDate(lectureDAO, baseDate, classLogDAO, holidayDAO, oneClassDAO) ||
						(holidayDAO.checkHoliday(afterStartDate.getClassDate(baseDate)))){
					iter.remove();
				}
			}
			oneClassList.addAll(afterStartDateList);
		}
		
		for(OneClass oc : oneClassList){
			
			Date date = oc.getClassDate(baseDate);
			List<ClassLog> classLogList = classLogDAO.getClassLogListByOneClassTransaction(oc.getId(), date, date);
			if(classLogList.size() == 0)
				map.put(oc, null);
			else if(classLogList.size() == 1)
				map.put(oc, classLogList.get(0));
			else
				throw new IllegalStateException("OneClass's " + date + " Class Log is found " + classLogList.size() + "items.");
		}
		List<OneClass> reservedOneClassList = new ArrayList<>();
		List<OneClass> completedList =new ArrayList<>();
		List<OneClass> uncompletedList = new ArrayList<>();
		List<OneClass> postStudent = new ArrayList<>();
		List<OneClass> postTeacher = new ArrayList<>();
		List<OneClass> absentStudent = new ArrayList<>();
		List<OneClass> absentTeacher = new ArrayList<>();
		List<OneClass> levelTestReserved = new ArrayList<>();
		List<OneClass> levelTestCompleted = new ArrayList<>();
		List<OneClass> levelTestUncompleted = new ArrayList<>();
		List<OneClass> holiday = new ArrayList<>();
		List<DateValueObject> DVOList = new ArrayList<>();
		for(Date d : dateList){
			DateValueObject dvo = null;
			if(holidayDAO.checkHoliday(d))
				dvo = new DateValueObject(d, true);
			else
				dvo = new DateValueObject(d, false);
			DVOList.add(dvo);
		}
		for(OneClass oc : map.keySet()){
			ClassLog cl = map.get(oc);
			int code = oc.getDuration().getRt().getDayOfWeek().getCustomCalndarCode();
			DVOList.get(code).addCount();
			if(cl == null){
				reservedOneClassList.add(oc);
				continue;
			}
			switch(cl.getClassState()){
			case Completed:
				completedList.add(oc);
				break;
			case Uncompleted:
			case Uncompleted_0:
			case Uncompleted_100:
			case Uncompleted_30:
			case Uncompleted_50:
				uncompletedList.add(oc);
				break;
			case AbsentStudent:
				absentStudent.add(oc);
				break;
			case AbsentTeacher:
				absentTeacher.add(oc);
				break;
			case PostponeStudent:
				postStudent.add(oc);
				break;
			case PostponeTeacher:
				postTeacher.add(oc);
				break;
			case Holiday:
				holiday.add(oc);
				break;
			case LevelTestReserved:
				levelTestReserved.add(oc);
				break;
			case LevelTestCompleted:
				levelTestCompleted.add(oc);
				break;
			case LevelTestUncompleted:
				levelTestUncompleted.add(oc);
				break;
			default:
				break;
			}
		}
		
		List<ClassTime> reservedClassTime = Util.getClassListFromOneClassList(reservedOneClassList);
		List<ClassTime> completedClassTime = Util.getClassListFromOneClassList(completedList);
		List<ClassTime> uncompletedClassTime = Util.getClassListFromOneClassList(uncompletedList);
		List<ClassTime> postStudentClassTime = Util.getClassListFromOneClassList(postStudent);
		List<ClassTime> postTeacherClassTime = Util.getClassListFromOneClassList(postTeacher);
		List<ClassTime> absentStudentClassTime = Util.getClassListFromOneClassList(absentStudent);
		List<ClassTime> absentTeacherClassTime = Util.getClassListFromOneClassList(absentTeacher);
		List<ClassTime> levelTestReservedClassTime = Util.getClassListFromOneClassList(levelTestReserved);
		List<ClassTime> levelTestCompletedClassTime = Util.getClassListFromOneClassList(levelTestCompleted);
		List<ClassTime> levelTestUncompletedClassTime = Util.getClassListFromOneClassList(levelTestUncompleted);
		ClassTimeDAO classTimeDAO = (ClassTimeDAO) applicationContext.getBean("classTimeDAO");
		List<ClassTime> result = classTimeDAO.getClassTimeListByTeacherId(teacher.getId());
		for(ClassTime ct : reservedClassTime){
			result.remove(ct);
		}
		
		
		req.setAttribute("DVOList", DVOList);
		req.setAttribute("reservedList", reservedClassTime);
		req.setAttribute("completedList", completedClassTime);
		req.setAttribute("uncompletedList", uncompletedClassTime);
		req.setAttribute("postStudentList", postStudentClassTime);
		req.setAttribute("postTeacherList", postTeacherClassTime);
		req.setAttribute("absentStudentList", absentStudentClassTime);
		req.setAttribute("absentTeacherList", absentTeacherClassTime);
		req.setAttribute("levelTestReservedClassTime", levelTestReservedClassTime);
		req.setAttribute("levelTestCompletedClassTime", levelTestCompletedClassTime);
		req.setAttribute("levelTestUncompletedClassTime", levelTestUncompletedClassTime);
	}
}
