package Kakao.kakaoeng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pagination {
	int page;
	int viewPerPage;
	int count;
	int startIndex;
	
	public Pagination(int page, int viewPerPage, int count){
		this.page = page;
		if(viewPerPage == 0)
			viewPerPage = 1;
		this.viewPerPage = viewPerPage;
		this.count = count;
		this.startIndex = count - (viewPerPage * (page-1));
	}
	
	public int getStart(){
		return this.viewPerPage*(this.page-1);
	}
	
	public int getEnd(){
		return this.viewPerPage*this.page;
	}
	
	public int getPage() {
		return page;
	}

	public int getViewPerPage() {
		return viewPerPage;
	}

	public int getCount() {
		return count;
	}

	public int getMaxPage(){
		int maxPage = count/viewPerPage;
		if(count > maxPage*viewPerPage)
			maxPage++;
		return maxPage;
	}
	
	public int getMinPage(){
		return 1;
	}
	
	public int getCurrentPage(){
		return this.page;
	}
	
	public List<Integer> getPrevList(int size){
		List<Integer> result = new ArrayList<>();
		int currentPage = this.getCurrentPage();
		for(int i=currentPage-1; i>=this.getMinPage() && size > 0; i--, size--){
			result.add(i);
		}
		Collections.sort(result);
		return result;
	}
	public List<Integer> getNextList(int size){
		List<Integer> result = new ArrayList<>();
		int currentPage = this.getCurrentPage();
		for(int i=currentPage+1; i<=this.getMaxPage() && size > 0; i++, size--){
			result.add(i);
		}
		Collections.sort(result);
		return result;
	}
	public int getPrevSize(){
		return this.getCurrentPage() - this.getMinPage();
	}
	
	public int getNextSize(){
		return this.getMaxPage() - this.getCurrentPage();
	}
	
	public List<Integer> getWings(int size){
		int prevSize = this.getPrevSize();
		int nextSize = this.getNextSize();
		if(prevSize >= size && nextSize < size){
			size += (size - nextSize);
		}
		else if(prevSize < size && nextSize >= size){
			size += (size - prevSize);
		}
		List<Integer> result = new ArrayList<>();
		result.addAll(this.getPrevList(size));
		result.add(this.getCurrentPage());
		result.addAll(this.getNextList(size));
		return result;
	}
	public int getIndex(){
		return this.startIndex--;
	}
}
