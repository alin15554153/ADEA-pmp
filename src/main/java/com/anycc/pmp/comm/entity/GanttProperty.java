package com.anycc.pmp.comm.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GanttProperty {
	private String id;
	private String name;
	private String code;
	private int level=0;
	private String status="STATUS_ACTIVE";
	private boolean canWrite=false;
	private long start;
	private long end;
	private long duration;
	private boolean startIsMilestone=true;
	private boolean endIsMilestone=true;
	private boolean collapsed=false;
	private List<String> assigs=new ArrayList<String>();
	private boolean hasChild=false;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isCanWrite() {
		return canWrite;
	}
	public void setCanWrite(boolean canWrite) {
		this.canWrite = canWrite;
	}
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public long getEnd() {
		return end;
	}
	public void setEnd(long end) {
		this.end = end;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public boolean isStartIsMilestone() {
		return startIsMilestone;
	}
	public void setStartIsMilestone(boolean startIsMilestone) {
		this.startIsMilestone = startIsMilestone;
	}
	public boolean isEndIsMilestone() {
		return endIsMilestone;
	}
	public void setEndIsMilestone(boolean endIsMilestone) {
		this.endIsMilestone = endIsMilestone;
	}
	public boolean isCollapsed() {
		return collapsed;
	}
	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}
	public List<String> getAssigs() {
		return assigs;
	}
	public void setAssigs(List<String> assigs) {
		this.assigs = assigs;
	}
	public boolean isHasChild() {
		return hasChild;
	}
	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}
	
}
