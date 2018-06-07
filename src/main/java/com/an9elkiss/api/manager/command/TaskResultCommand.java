package com.an9elkiss.api.manager.command;

import java.util.List;

public class TaskResultCommand {
	
	/** 计划贡献值统计 */
	private Integer planScoreTotal;
	
	/** 计划工时数统计 */
	private Integer planHoursTotal;
	
	/** 实际工时数统计 */
	private Integer actualHoursTotal;
	
	private List<TaskViewCommand> taskCommands;

	public Integer getPlanScoreTotal() {
		return planScoreTotal;
	}

	public void setPlanScoreTotal(Integer planScoreTotal) {
		this.planScoreTotal = planScoreTotal;
	}

	public Integer getPlanHoursTotal() {
		return planHoursTotal;
	}

	public void setPlanHoursTotal(Integer planHoursTotal) {
		this.planHoursTotal = planHoursTotal;
	}

	public Integer getActualHoursTotal() {
		return actualHoursTotal;
	}

	public void setActualHoursTotal(Integer actualHoursTotal) {
		this.actualHoursTotal = actualHoursTotal;
	}

	public List<TaskViewCommand> getTaskCommands() {
		return taskCommands;
	}

	public void setTaskCommands(List<TaskViewCommand> taskCommands) {
		this.taskCommands = taskCommands;
	}
	
}
