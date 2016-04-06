package Kakao.kakaoeng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.springframework.context.ApplicationContext;

import Kakao.kakaoeng.dao.ClassTimeDAO;
import Kakao.kakaoeng.dao.ClassTimeUsageLogDAO;
import Kakao.kakaoeng.dao.TeacherDAO;
import Kakao.kakaoeng.domain.model.ClassTime;
import Kakao.kakaoeng.domain.model.Duration;
import Kakao.kakaoeng.domain.model.RegisterTime;
import Kakao.kakaoeng.domain.model.Teacher.Rate;

public class MatchLibrary {
	public Map<TeacherWithCount, List<RegisterTime>> parseResult(List<ClassTime> classTimesWithinMatching) {
		Map<TeacherWithCount, List<RegisterTime>> result = new HashMap<>();

		for (ClassTime classTime : classTimesWithinMatching) {
			TeacherWithCount twc = new TeacherWithCount(classTime.getTeacherId());
			if (!result.containsKey(twc))
				result.put(twc, new ArrayList<RegisterTime>());
			result.get(twc).add(classTime.getRegisterTime());
		}

		return result;
	}

	/**
	 * Parse teacher's Register Time into Duration It will ignore RegisterTime
	 * which can't satisfy client's need
	 * 
	 * @param groupedByTeacher
	 *            : teacher's registerTime list
	 * @param requiredDuration
	 *            : client's matching class time length(duration)
	 * @return return map grouped RegisterTime into Duration by Teacher
	 */
	public Map<TeacherWithCount, List<Duration>> durationGrouping(
			Map<TeacherWithCount, List<RegisterTime>> groupedByTeacher, int requiredDuration) {
		Map<TeacherWithCount, List<Duration>> result = new HashMap<>();
		for (TeacherWithCount twc : groupedByTeacher.keySet()) {
			List<RegisterTime> teacherReigsterTimeList = groupedByTeacher.get(twc);
			List<Duration> parsedDuration = Duration.parseRegisterTime(teacherReigsterTimeList, requiredDuration);
			result.put(twc, parsedDuration);
		}
		return result;
	}

	

	/**
	 * return grouped durationList by teacher. if duration list empty , it will
	 * removed.
	 * 
	 * @param classTimesWithinMatching
	 *            : RegisterTimeList Grouped By Teacher
	 * @param item
	 *            : request sortclass
	 * @return
	 */
	public Map<TeacherWithCount, List<Duration>> getDurationListGroupedByTeacher(
			List<ClassTime> classTimesWithinMatching, ClassSearchUnit item) {
		// Grouping by teacher
		Map<TeacherWithCount, List<RegisterTime>> groupedByTeacher = parseResult(classTimesWithinMatching);
		Map<TeacherWithCount, List<Duration>> groupedWithDuration = durationGrouping(groupedByTeacher,
				item.getDuration().getDuration());
		// Empty Result Delete
		for (Iterator<Map.Entry<TeacherWithCount, List<Duration>>> iter = groupedWithDuration.entrySet()
				.iterator(); iter.hasNext();) {
			Map.Entry<TeacherWithCount, List<Duration>> entry = iter.next();
			if (entry.getValue().isEmpty())
				iter.remove();
		}

		return groupedWithDuration;
	}

	public List<ClassSearchUnit> getMinDifferSortClassFromDuration(List<Duration> splitedDuration, ClassSearchUnit item) {
		List<ClassSearchUnit> result = null;
		Map<Integer, ClassSearchUnit> pcontainer = new TreeMap<>();
		Map<Integer, ClassSearchUnit> ncontainer = new TreeMap<>();
		for (Duration element : splitedDuration) {
			int differ = item.getTimeDiffer(element);
			boolean bExact = false;
			if (differ == 0)
				bExact = true;
			if(differ > 0){
				if (pcontainer.containsKey(differ))
					throw new IllegalStateException("getMinDifferSortClassFromDuration get Duplicate differ(which is not absolute value.)");
				pcontainer.put(differ, new ClassSearchUnit(element, item.getStartDate(), item.getWeeks(), bExact));
			}
			else{
				if (ncontainer.containsKey(Math.abs(differ)))
					throw new IllegalStateException("getMinDifferSortClassFromDuration get Duplicate differ(which is not absolute value.)");
				ncontainer.put(Math.abs(differ), new ClassSearchUnit(element, item.getStartDate(), item.getWeeks(), bExact));
			}
		}

		result = new ArrayList<>();
		for(Integer key : ncontainer.keySet()){
			result.add(ncontainer.get(key));
			if(key == 0){
				return result;
			}
			break;
		}
		for(Integer key : pcontainer.keySet()){
			result.add(pcontainer.get(key));
			if(key == 0){
				return result;
			}
			break;
		}
		
		
		return result;
	}
	
	public static void removeInvalidData(List<ClassSearchUnit> findList,
			Map<TeacherWithCount, Map<ClassSearchUnit, List<ClassSearchUnit>>> result) {
		for (Iterator<Map.Entry<TeacherWithCount, Map<ClassSearchUnit, List<ClassSearchUnit>>>> iter = result.entrySet()
				.iterator(); iter.hasNext();) {
			Map.Entry<TeacherWithCount, Map<ClassSearchUnit, List<ClassSearchUnit>>> entry = iter.next();
			int size = 0;
			for(ClassSearchUnit key : entry.getValue().keySet()){
				if(key.getExact())
					size++;
				else
					size++;
			}
			if (size != findList.size())
				iter.remove();
		}
	}

	public static void addTWCCountInMap(Map<TeacherWithCount, Map<ClassSearchUnit, List<ClassSearchUnit>>> result,
			TeacherWithCount twc) {
		for (Iterator<Map.Entry<TeacherWithCount, Map<ClassSearchUnit, List<ClassSearchUnit>>>> iter = result.entrySet()
				.iterator(); iter.hasNext();) {
			Map.Entry<TeacherWithCount, Map<ClassSearchUnit, List<ClassSearchUnit>>> entry = iter.next();
			if (entry.getKey().equals(twc))
				entry.getKey().addCount();
		}
	}

	public static void sliceMap(Map<TeacherWithCount, Map<ClassSearchUnit, List<ClassSearchUnit>>> map, int count) {
		int i = 0;
		count = Math.min(count, map.size());
		for (Iterator<Map.Entry<TeacherWithCount, Map<ClassSearchUnit, List<ClassSearchUnit>>>> iter = map.entrySet()
				.iterator(); iter.hasNext(); iter.next()) {
			if (i++ < count)
				continue;
			iter.remove();
		}
	}

	public void process(Map<TeacherWithCount, Map<ClassSearchUnit, List<ClassSearchUnit>>> exactResult, Map<TeacherWithCount, Map<ClassSearchUnit, List<ClassSearchUnit>>> similarResult, ApplicationContext applicationContext,List<ClassSearchUnit> findList ){
		ClassTimeUsageLogDAO classTimeUsageLogDAO = (ClassTimeUsageLogDAO) applicationContext.getBean("classTimeUsageLogDAO");
		final ClassTimeDAO classTimeDAO = (ClassTimeDAO) applicationContext.getBean("classTimeDAO");
		final TeacherDAO teacherDAO = (TeacherDAO) applicationContext.getBean("teacherDAO");
		Map<TeacherWithCount, Map<ClassSearchUnit, List<ClassSearchUnit>>> result = new HashMap<TeacherWithCount, Map<ClassSearchUnit, List<ClassSearchUnit>>>();

		Rate[] rates = { Rate.A, Rate.B, Rate.C };
		for (Rate rateElement : rates) {
			result.clear();
			List<String> exceptList = new ArrayList<>();
			for (int i = 0; i < findList.size(); i++) {
				ClassSearchUnit item = findList.get(i);
				List<ClassTime> classTimesWithinMatching = Util.getClassTimeCanBeUsedStateList(classTimeDAO,
						classTimeUsageLogDAO, item, rateElement);
				Map<TeacherWithCount, List<Duration>> groupedWithDuration = this
						.getDurationListGroupedByTeacher(classTimesWithinMatching, item);

				for (TeacherWithCount twc : groupedWithDuration.keySet()) {
					if (exceptList.contains(twc.getTeacherId()))
						continue;
					List<Duration> splitedDuration = new ArrayList<>();
					for (Duration rawDuration : groupedWithDuration.get(twc)) {
						splitedDuration.addAll(rawDuration.split(item.getDuration().getDuration()));
					}
					List<ClassSearchUnit> minResult = getMinDifferSortClassFromDuration(splitedDuration, item);
					if (minResult.size() == 0)
						continue;
					ClassSearchUnit citem = new ClassSearchUnit(item.getDuration(), item.getStartDate(), item.getWeeks());
					if(minResult.size() == 1){
						if(minResult.get(0).getExact())
							citem.exact = true;
						else
							citem.exact = false;
							
					}
					ClassSearchUnit one = minResult.get(0);

					if (!result.containsKey(twc))
						result.put(twc, new TreeMap<ClassSearchUnit, List<ClassSearchUnit>>());
					if (!result.get(twc).containsKey(citem))
						result.get(twc).put(citem, new ArrayList<ClassSearchUnit>());

					for (ClassSearchUnit added : minResult)
						result.get(twc).get(citem).add(added);

					if (one.getExact())
						addTWCCountInMap(result, twc);
				}
			}

			removeInvalidData(findList, result);
			
			for (TeacherWithCount key : result.keySet()) {
				key.loadTeacher(teacherDAO);
				if(key.getTeacher().getConfirm() == false)
					continue;
				if(key.getTeacher().getRetirement() == true)
					continue;
				if (key.getCount() == findList.size()){
					Map<ClassSearchUnit, List<ClassSearchUnit>> map = result.get(key);
					for(ClassSearchUnit csu : map.keySet())
						csu.thisIsExact();
					exactResult.put(key, result.get(key));
				}
				else if (key.getCount() >= (findList.size() * 0.32)){
					Map<ClassSearchUnit, List<ClassSearchUnit>> map = result.get(key);
					for(ClassSearchUnit csu : map.keySet()){
						List<ClassSearchUnit> list = map.get(csu);
						if(list.size()!=1){
							csu.exact = false;
						}else if(list.size() > 1){
							csu.exact = true;
							for(ClassSearchUnit e : list){
								if(e.exact == false){
									csu.exact = false;
									break;
								}
							}
						}
					}
					similarResult.put(key, result.get(key));
				}
			}
		}

		if (exactResult.size() >= 5) {
			sliceMap(exactResult, 5);
			similarResult.clear();
		} else {
			int eCount = exactResult.size();
			int sCount = 5 - eCount;
			sliceMap(similarResult, sCount);
		}
	}
}
