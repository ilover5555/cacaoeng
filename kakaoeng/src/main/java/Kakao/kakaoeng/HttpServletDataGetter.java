package Kakao.kakaoeng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.activation.FileDataSource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;

import Kakao.kakaoeng.FileSaver.FileLimitExceed;
import Kakao.kakaoeng.domain.model.ClassTime;
import Kakao.kakaoeng.domain.model.ClassTimeUsageLog;
import Kakao.kakaoeng.domain.model.Lecture;
import Kakao.kakaoeng.domain.model.Teacher.Quality;
import Kakao.kakaoeng.domain.model.Teacher.Rate;
import Kakao.kakaoeng.field.ExceptionField;
import Kakao.kakaoeng.field.Field;

public abstract class HttpServletDataGetter implements DataGetter {
	Logger logger = Logger.getLogger(HttpServletDataGetter.class);
	HttpServletRequest req = null;
	protected List<String> messages = new ArrayList<>();

	@Override
	public <T> T get(String key, Class<T> klass) throws InvalidKeyException {
		if(!req.getParameterMap().containsKey(key))
			throw new InvalidKeyException("HttpServletDataGetter get Invalid Key : " + key);
		if(klass.equals(String.class)){
			return klass.cast(req.getParameter(key));
		}
		else if (klass.equals(Boolean.class)){
			return klass.cast(new Boolean(req.getParameter(key)));
		}
		
		return null;
	}
	
	public List<Field<?>> getFields(){
		List<Field<?>> result = new ArrayList<>();
		
		Field<Date> lastLogin = new Field<Date>("lastLogin", new Date());
		Field<Date> registerDate = new Field<Date>("registerDate", new Date());
		Field<Boolean> confirm = new Field<Boolean>("confirm", false);
		Field<Rate> rate = new Field<Rate>("rate", Rate.Wait);
		Field<Boolean> representitve = new Field<Boolean>("representitive", false);
		Field<Integer> salary = new Field<Integer>("salary", -1);
		Field<Quality> pronunciation = new Field<Quality>("pronunciation", Quality.Normal);
		Field<Quality> accent = new Field<Quality>("accent", Quality.Normal);
		Field<String> comment = new Field<String>("comment", "No Comment");
		Field<Boolean> retirement = new Field<Boolean>("retirement", false);
		
		result.addAll(makePartField("primaryProfile", "primaryProfile"));
		result.addAll(makePartField("primaryVoice", "primaryVoice"));
		result.addAll(makePartField("specImage", "specImage"));
		result.add(lastLogin);
		result.add(registerDate);
		result.add(confirm);
		result.add(rate);
		result.add(representitve);
		result.add(salary);
		result.add(pronunciation);
		result.add(accent);
		result.add(comment);
		result.add(retirement);
		
		return result;
	}
	
	@Override
	public boolean getToefl() {
		String toeflString = this.get("toefl", String.class);
		return toeflString.equals("Able") ? true : false;
	}

	@Override
	public boolean getIlets() {
		String iletsString = this.get("ilets", String.class);
		return iletsString.equals("Able") ? true : false;
	}

	public HttpServletDataGetter() {
		super();
	}

	public List<String> getMessages() {
		if(messages == null){
			return new ArrayList<>();
		}		
		return messages;
	}

	protected List<Field<?>> makePartField(String partName, String fieldName) {
		Part part = null;
		try {
			part = this.getPart(fieldName);
		} catch (IOException | ServletException e) {
			e.printStackTrace();
		} catch(RuntimeException e){
			List<Field<?>> result = new ArrayList<>();
			result.add(new ExceptionField<>(fieldName, e.getMessage()));
			return result;
		}
		
		Date now = new Date();
		long timestamp = now.getTime();
		
		String path = "";
		if(part.getSize()>0)
			path = FileSaver.makeLinkPath(Util.getPartFileName(part), timestamp);
		
		Field<Part> partField = new Field<Part>(partName+"_Part", part);
		partField.addParam("time", timestamp);
		
		Field<String> pathField = new Field<String>(partName, path);
		
		List<Field<?>> result = new ArrayList<>();
		result.add(partField);
		result.add(pathField);
		
		return result;
	}

	abstract public Part getPart(String key) throws IOException, ServletException;
	
	protected List<String> fileSave(String fieldName, FieldSet fieldSet, int limit){
		List<String> result = new ArrayList<>();
		
		try {
			Field<?> partField = fieldSet.getField(fieldName);
			Part part = (Part) partField.getValue();
			long timeStamp = (long) partField.getParam("time");
			FileSaver.fileSave(part, Util.getPartFileName(part), "resource", timeStamp, true, limit);
		} catch (IOException e) {
			result.add(fieldName +" upload Fail");
		} catch (FileLimitExceed e){
			result.add("FileSize cannot exceed "+ limit+"bytes");
		} catch (RuntimeException e){
			logger.error(e.getMessage(), e);
		}
		
		return result;
	}
	
	@Override
	public String getBirth() {
		return this.get("birth", String.class);
	}
	
	@Override
	public ClassTime makeClassTimeInstance() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ClassTimeUsageLog makeClassTimeUsageLogInstance() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Lecture makeLectureInstance() {
		throw new UnsupportedOperationException();
	}
}