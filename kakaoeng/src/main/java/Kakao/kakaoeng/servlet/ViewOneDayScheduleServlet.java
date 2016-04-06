package Kakao.kakaoeng.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.DateValueObject;
import Kakao.kakaoeng.Util;
import Kakao.kakaoeng.dao.ClassLogDAO;
import Kakao.kakaoeng.dao.HolidayDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.dao.PurchaseDAO;
import Kakao.kakaoeng.dao.StudentDAO;
import Kakao.kakaoeng.domain.model.ClassLog;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.OneClass;
import Kakao.kakaoeng.domain.model.Teacher;
import Kakao.kakaoeng.domain.model.RegisterTime.DayOfWeek;

@SuppressWarnings("serial")
@WebServlet("/teacher/viewOneDaySchedule.view")
public class ViewOneDayScheduleServlet extends HttpServlet {

	class DummyDate implements List<Date>{

		Date data = null;
		
		@Override
		public boolean add(Date e) {
			data = e;
			return true;
		}

		@Override
		public void add(int index, Date element) {
			data = element;
		}

		@Override
		public boolean addAll(Collection<? extends Date> c) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean addAll(int index, Collection<? extends Date> c) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void clear() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean contains(Object o) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Date get(int index) {
			return data;
		}

		@Override
		public int indexOf(Object o) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Iterator<Date> iterator() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int lastIndexOf(Object o) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public ListIterator<Date> listIterator() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ListIterator<Date> listIterator(int index) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean remove(Object o) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Date remove(int index) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Date set(int index, Date element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public List<Date> subList(int fromIndex, int toIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object[] toArray() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> T[] toArray(T[] a) {
			return null;
		}
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ApplicationContext applicationContext = (ApplicationContext) req.getServletContext().getAttribute("applicationContext");
		Teacher teacher = (Teacher) req.getSession().getAttribute("teacher");
		if(teacher == null)
		{
			req.getRequestDispatcher("./log_in.jsp").forward(req, resp);
			return;
		}
		
		LectureDAO lectureDAO = (LectureDAO) applicationContext.getBean("lectureDAO");
		OneClassDAO oneClassDAO = (OneClassDAO) applicationContext.getBean("oneClassDAO");
		ClassLogDAO classLogDAO = (ClassLogDAO) applicationContext.getBean("classLogDAO");
		StudentDAO studentDAO = (StudentDAO) applicationContext.getBean("studentDAO");
		HolidayDAO holidayDAO = (HolidayDAO) applicationContext.getBean("holidayDAO");
		PurchaseDAO purchaseDAO = (PurchaseDAO) applicationContext.getBean("purchaseDAO");
		
		Date baseDate = Util.parseDateIgnoreNull((String) req.getParameter("baseDate"));
		List<Date> dummyList = new ArrayList<>();
		dummyList.add(baseDate);
		List<Date> dummyBaseDate = new DummyDate();
		dummyBaseDate.add(baseDate);
		List<Lecture> lectureList = Util.getLectureList(dummyList, lectureDAO, teacher.getId());
		Map<Lecture, List<OneClass>> oneClassMap = Util.getReservedOneClass(oneClassDAO, lectureList);
		List<OneClass> oneClassResultList = new ArrayList<>();
		for(Lecture lecture : oneClassMap.keySet()){
			List<OneClass> list = oneClassMap.get(lecture);
			for(Iterator<OneClass> iter = list.iterator(); iter.hasNext(); ){
				OneClass oc = iter.next();
				oc.setStartDate(lecture);
				if((oc.getDuration().getRt().getDayOfWeek().equals(DayOfWeek.getInstanceFromDate(baseDate))) &&
						oc.isAfterStartDate(baseDate, lecture) &&
						oc.isBeforeEndDate(lectureDAO, baseDate, classLogDAO, holidayDAO, oneClassDAO) &&
						!holidayDAO.checkHoliday(oc.getClassDate(baseDate)))
				{
					oc.loadLecture(lectureDAO);
					oc.getLecture().loadPurchase(purchaseDAO);
					oc.setOrder(baseDate, holidayDAO, classLogDAO, oneClassDAO, lectureDAO);
					oc.loadStudent(studentDAO);
					oneClassResultList.add(oc);
				}
			}
		}
		Map<OneClass, ClassLog> result = new HashMap<>();
		for(OneClass oc : oneClassResultList){
			oc.loadLecture(lectureDAO);
			List<ClassLog> classLogList = classLogDAO.getClassLogListByOneClassTransaction(oc.getId(), baseDate, baseDate);
			if(classLogList.size() == 0)
				result.put(oc, null);
			else if(classLogList.size() == 1)
				result.put(oc, classLogList.get(0));
			else
				throw new IllegalStateException("OneClass's " + baseDate + " Class Log is found " + classLogList.size() + "items.");
		}
		
		DateValueObject dvo = new DateValueObject(baseDate);
		
		req.setAttribute("dvo", dvo.getDateForm());
		req.setAttribute("teacher", teacher.getClassName());
		req.setAttribute("classStateMap", result);
		
		req.getRequestDispatcher("./one_day_schedule_viewer.jsp").include(req, resp);;
	}

}