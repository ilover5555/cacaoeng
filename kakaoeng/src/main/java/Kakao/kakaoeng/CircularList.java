package Kakao.kakaoeng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CircularList<T> {
	private List<T> data = new ArrayList<>();
	int i=0;
	int circle=0;
	boolean used=false;
	int term=0;
	
	public void set(int i){
		if(used)
			throw new IllegalStateException("Moderating after reading is prohibited.");
		this.i = i;
	}
	
	public void add(T t){
		if(used)
			throw new IllegalStateException("Moderating after reading is prohibited.");
		data.add(t);
	}
	
	public void addAll(Collection<? extends T> c){
		if(used)
			throw new IllegalStateException("Moderating after reading is prohibited.");
		data.addAll(c);
	}
	
	public T getNext(){
		if(data.size() == 0)
			return null;
		if(i>=data.size()){
			i=0;	
		}
		if(term>=data.size()){
			circle++;
			term=0;
		}
		term++;
		return data.get(i++);
	}
	
	public int getSize(){
		return data.size();
	}
	
	public List<T> getRawData(){
		return new ArrayList<>(data);
	}
	
	public int getCircle(){
		return this.circle;
	}
}
