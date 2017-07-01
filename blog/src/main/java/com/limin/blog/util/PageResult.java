package com.limin.blog.util;

import java.util.List;

public class PageResult<T> {
	private long totalRow;		//总记录数
	private int totalPage;		//总页数
	private int pageNum = 1;	//当前页
	private int pageSize = 5;		//每页记录数
	private List<T> pageItems;	//当前页记录
	private int pageBarSize = 10;//分页栏大小
	private int startIndex;
	private int endIndex;
	
	public PageResult() {

	}
	
	public PageResult(int pageNum, int pageSize, List<T> pageItems, long totalRow) {
		this.totalRow = totalRow;
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.pageItems = pageItems;
		
		totalPage = (int) (totalRow / pageSize);
		if(totalRow % pageSize != 0) {
			totalPage++;
		}
		
		if(totalPage < pageBarSize) {
			startIndex = 1;
			endIndex = totalPage;
		} else {
			if(pageBarSize % 2 ==0) {
				startIndex = pageNum - pageBarSize / 2 + 1;
				endIndex = pageNum + pageBarSize / 2;
			} else {
				startIndex = pageNum - pageBarSize / 2;
				endIndex = pageNum + pageBarSize / 2 + 1;
			}
			if(startIndex < 1) {
				startIndex = 1;
				endIndex = pageBarSize;
			}
			if(endIndex > totalPage) {
				endIndex = totalPage;
				startIndex = totalPage - pageBarSize + 1;
			}
		}
	}
	
	public long getTotalRow() {
		return totalRow;
	}
	
	public int getTotalPage() {
		
		return totalPage;
	}
	
	public int getPageNum() {
		return pageNum;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public List<T> getPageItems() {
		return pageItems;
	}
	
	public int getPageBarSize() {
		return pageBarSize;
	}
	public void setPageBarSize(int pageBarSize) {
		this.pageBarSize = pageBarSize;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public int getEndIndex() {
		return endIndex;
	}

	@Override
	public String toString() {
		return "PageResult [totalRow=" + totalRow + ", totalPage=" + totalPage
				+ ", pageNum=" + pageNum + ", pageSize=" + pageSize
				+ ", pageBarSize=" + pageBarSize + ", startIndex=" + startIndex
				+ ", endIndex=" + endIndex + "]";
	}
	
	
}
