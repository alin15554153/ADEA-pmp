package com.anycc.pmp.comm.entity;

import java.util.List;

public class Gantt {
	private List<GanttProperty> tasks;
	private int selectedRow=0;
	private boolean canWrite=false;
	private boolean canWriteOnParent=false;
	public List<GanttProperty> getTasks() {
		return tasks;
	}
	public void setTasks(List<GanttProperty> tasks) {
		this.tasks = tasks;
	}
	public int getSelectedRow() {
		return selectedRow;
	}
	public void setSelectedRow(int selectedRow) {
		this.selectedRow = selectedRow;
	}
	public boolean isCanWrite() {
		return canWrite;
	}
	public void setCanWrite(boolean canWrite) {
		this.canWrite = canWrite;
	}
	public boolean isCanWriteOnParent() {
		return canWriteOnParent;
	}
	public void setCanWriteOnParent(boolean canWriteOnParent) {
		this.canWriteOnParent = canWriteOnParent;
	}
	
}
