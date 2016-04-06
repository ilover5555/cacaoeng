package Kakao.kakaoeng;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceUtils;

import Kakao.kakaoeng.dao.ClassLogDAO;
import Kakao.kakaoeng.dao.ClassTimeDAO;
import Kakao.kakaoeng.dao.ClassTimeUsageLogDAO;
import Kakao.kakaoeng.dao.HolidayDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.dao.PurchaseDAO;
import Kakao.kakaoeng.domain.model.ClassLog;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.OneClass;
import Kakao.kakaoeng.domain.model.Purchase;
import Kakao.kakaoeng.domain.model.Student;
import Kakao.kakaoeng.domain.model.Teacher;
import Kakao.kakaoeng.domain.model.ClassLog.ClassState;
import Kakao.kakaoeng.domain.model.Lecture.Status;

public class ClassLogManager {

	@Autowired PurchaseDAO purchaseDAO;
	@Autowired ApplicationContext applicationContext;
	@Autowired HolidayDAO holidayDAO;
	@Autowired OneClassDAO oneClassDAO;
	@Autowired DataSource dataSource;
	@Autowired ClassTimeDAO classTimeDAO;
	@Autowired LectureDAO lectureDAO;
	@Autowired ClassLogDAO classLogDAO;
	@Autowired ClassTimeUsageLogDAO classTimeUsageLogDAO;
	Logger logger = Logger.getLogger(ClassLogManager.class);

	public String registerClassLogTransaction(int oneClassId, Date classDate,
			ClassState classState, String reason ) throws RuntimeException {
		String msg = "연기 신청이 완료되었습니다.";
		
		OneClass oneClass = oneClassDAO.getOneClassByIdTransaction(oneClassId);
		ClassLog classLog = new ClassLog(-1, oneClassId, oneClass.getTeacherId(), oneClass.getStudentId(), classDate, classState, reason);
		
		Connection connection = DataSourceUtils.getConnection(dataSource);
		try{
			List<ClassLog> checkList = classLogDAO.getClassLogListByOneClassTransaction(oneClassId, classDate, classDate);
			if(checkList.size() == 0){
				classLogDAO.registerTransaction(classLog);
			}
			else if(checkList.size() == 1){
				classLogDAO.updateTransaction(checkList.get(0).getId(), classLog.getClassState(), classLog.getReason());
			}
			else{
				throw new IllegalStateException("duplicate classLog is inserted already.");
			}
			
		}catch(RuntimeException e){
			e.printStackTrace();
			msg = "DB기록중 에러가 있었습니다.";
			throw new RuntimeException(msg);
		}

		if(Util.doneClassStateList.contains(classState) && !classState.equals(ClassState.LevelTestReserved)){
			Lecture lecture = lectureDAO.getLectureByIdTransaction(oneClass.getLectureId());
			List<OneClass> oneClassList = oneClassDAO.getOneClassListGroupedByLectureIdTransaction(lecture.getId());
			for(OneClass oc : oneClassList){
				List<ClassLog> classLogList = classLogDAO.getAllClassLogListByOneClassTransaction(oc.getId());
				oc.setDone(classLogList);
			}
			lecture.setDone(oneClassList);
			if(lecture.getFullClass() == lecture.getDone()){
				lectureDAO.finishLectureTransaction(lecture.getId(), Status.Done, classLog.getClassDate());
				logger.info("Lecture " + lecture.getId() +" is finished normally!");
			}
			logger.info(lecture.getDone());
		}else{
			//ClassTimeUsageLog를 미루어야함 조약없음
		}

		try {
			DataSourceUtils.doReleaseConnection(connection, dataSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return msg;
	}
	
	//학생은 연기밖에 안함
	public String registerClassLogTransaction(Student student, int oneClassId, Date classDate,
			ClassState classState, String reason ) throws RuntimeException {
		String msg = "연기 신청이 완료되었습니다.";

		
		Connection connection = DataSourceUtils.getConnection(dataSource);
		try{
			
			OneClass oneClass = oneClassDAO.getOneClassByIdTransaction(oneClassId);
			ClassLog classLog = new ClassLog(-1, oneClassId, oneClass.getTeacherId(), oneClass.getStudentId(), classDate, classState, reason);
			if (!oneClass.getStudentId().equals(student.getId())) {
				throw new IllegalStateException("Class Report must be requested by responsible student.");
			}
			
			if(classLog.getClassState().equals(ClassState.PostponeStudent)){
				Purchase purchase = purchaseDAO.getPurchaseById(oneClass.getPurchaseNumber());
				int procrastinate = purchase.getProcrastinate();
				if(procrastinate == 0){
					throw new IllegalStateException("연기가능회수 "+(purchase.getFullClass()/8)+"회를 초과하여 연기가 되지않습니다.\n불가피한 상황이 생기셨다면 학습게시판을 통해 관리자에게 요청 바랍니다.");
				}
				purchaseDAO.updateProcrastinateTransaction(purchase.getId(), procrastinate-1);
			}
			
		
			
			List<ClassLog> checkList = classLogDAO.getClassLogListByOneClassTransaction(oneClassId, classDate, classDate);
			if(checkList.size() == 0){
				classLogDAO.registerTransaction(classLog);
			}
			else if(checkList.size() == 1){
				classLogDAO.updateTransaction(checkList.get(0).getId(), classLog.getClassState(), classLog.getReason());
			}
			else{
				throw new IllegalStateException("duplicate classLog is inserted already.");
			}
			
			if(Util.doneClassStateList.contains(classState)){

			}else{
				
			}
		}finally{
			try {
				DataSourceUtils.doReleaseConnection(connection, dataSource);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		

		return msg;
	}
	
	public String registerClassLogTransaction(Teacher teacher, int oneClassId, Date classDate,
			ClassState classState, String reason ) throws RuntimeException {
		String msg = "Success";
		
		OneClass oneClass = oneClassDAO.getOneClassByIdTransaction(oneClassId);

		if (!oneClass.getTeacherId().equals(teacher.getId())) {
			throw new IllegalStateException("Class Report must be requested by responsible teacher.");
		}

		ClassLog classLog = new ClassLog(-1, oneClassId, oneClass.getTeacherId(), oneClass.getStudentId(), classDate, classState, reason);
		
		Connection connection = DataSourceUtils.getConnection(dataSource);
		try{
			List<ClassLog> checkList = classLogDAO.getClassLogListByOneClassTransaction(oneClassId, classDate, classDate);
			if(checkList.size() == 0){
				classLogDAO.registerTransaction(classLog);
			}
			else if(checkList.size() == 1){
				classLogDAO.updateTransaction(checkList.get(0).getId(), classLog.getClassState(), classLog.getReason());
			}
			else{
				throw new IllegalStateException("duplicate classLog is inserted already.");
			}
			
		}catch(RuntimeException e){
			e.printStackTrace();
			msg = "ClassLog Register Error.";
			throw new RuntimeException(msg);
		}

		if(Util.doneClassStateList.contains(classState)){
			Lecture lecture = lectureDAO.getLectureByIdTransaction(oneClass.getLectureId());
			List<OneClass> oneClassList = oneClassDAO.getOneClassListGroupedByLectureIdTransaction(lecture.getId());
			for(OneClass oc : oneClassList){
				List<ClassLog> classLogList = classLogDAO.getAllClassLogListByOneClassTransaction(oc.getId());
				oc.setDone(classLogList);
			}
			lecture.setDone(oneClassList);
			if(lecture.getFullClass() == lecture.getDone()){
				lectureDAO.finishLectureTransaction(lecture.getId(), Status.Done, classLog.getClassDate());
				logger.info("Lecture " + lecture.getId() +" is finished normally!");
			}
			logger.info(lecture.getDone());
		}else{
			/*
			List<ClassTime> classTimeList = oneClass.getClassTimeList();
			for(ClassTime ct : classTimeList){
				int classTimeId = classTimeDAO.findIdFromClassTimeInstance(ct);
				classTimeUsageLogDAO.procrastinateTransaction(classTimeId, 1);
			}
			*/
		}

		try {
			DataSourceUtils.doReleaseConnection(connection, dataSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return msg;
	}
}
