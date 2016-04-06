package Kakao.kakaoeng;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.management.RuntimeErrorException;
import javax.sql.DataSource;

import org.apache.taglibs.standard.lang.jstl.test.beans.PublicInterface2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;

import Kakao.kakaoeng.dao.ClassLogDAO;
import Kakao.kakaoeng.dao.ClassTimeDAO;
import Kakao.kakaoeng.dao.ClassTimeUsageLogDAO;
import Kakao.kakaoeng.dao.LectureDAO;
import Kakao.kakaoeng.dao.OneClassDAO;
import Kakao.kakaoeng.dao.PurchaseDAO;
import Kakao.kakaoeng.domain.model.ClassLog;
import Kakao.kakaoeng.domain.model.ClassTime;
import Kakao.kakaoeng.domain.model.ClassTimeUsageLog;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.OneClass;
import Kakao.kakaoeng.domain.model.Purchase;
import Kakao.kakaoeng.domain.model.Lecture.Status;

public class RemoveAllPurchaseManager {

	@Autowired DataSource dataSource;
	@Autowired PurchaseDAO purchaseDAO;
	@Autowired LectureDAO lectureDAO;
	@Autowired OneClassDAO oneClassDAO;
	@Autowired ClassLogDAO classLogDAO;
	@Autowired ClassTimeDAO classTimeDAO;
	@Autowired ClassTimeUsageLogDAO classTimeUsageLogDAO;
	@Autowired BookManager bookManager;
	
	public void removeTransaction(int purchaseNumber){
		Connection connection = null;
		
		connection = DataSourceUtils.getConnection(dataSource);
		
		List<ClassLog> classLogList = classLogDAO.getAllClassLogListByPurchase(purchaseNumber);
		
		purchaseDAO.deleteByIdTransaction(purchaseNumber);
		bookManager.cancelTransaction(purchaseNumber, Status.Cancel);
		
		List<Lecture> lectureList = lectureDAO.getLectureListByPurchase(purchaseNumber);
		for(Lecture lecture : lectureList){
			lectureDAO.deleteByIdTransaction(lecture.getId());
		}
		
		List<OneClass> oneClassList = oneClassDAO.getOneClassListGroupedByPurchaseNumber(purchaseNumber);
		for(OneClass oneClass : oneClassList){
			oneClassDAO.deleteByIdTransaction(oneClass.getId());
		}
		
		for(ClassLog classLog : classLogList){
			classLogDAO.deleteByClassLogIdTransaction(classLog.getId());
			
		}
		
		

		
		
		
		
		try {DataSourceUtils.doReleaseConnection(connection, dataSource);} catch (SQLException e) {}
	}
}
