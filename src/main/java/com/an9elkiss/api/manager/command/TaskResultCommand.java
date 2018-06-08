package com.an9elkiss.api.manager.command;

import java.util.List;

public class TaskResultCommand {
	
	/** 计划贡献值统计 */
	private Integer planScoreTotal;
	
	/** 计划工时数统计 */
	private Integer planHoursTotal;
	
	/** 实际工时数统计 */
	private Integer actualHoursTotal;
	
	/** 折算工时统计 */
	private Integer percentHoursTotal;
	
	/** 实际贡献值统计 */
	private Integer actualScoreTotal;
	
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

	public Integer getPercentHoursTotal() {
		return percentHoursTotal;
	}

	public void setPercentHoursTotal(Integer percentHoursTotal) {
		this.percentHoursTotal = percentHoursTotal;
	}

	public Integer getActualScoreTotal() {
		return actualScoreTotal;
	}

	public void setActualScoreTotal(Integer actualScoreTotal) {
		this.actualScoreTotal = actualScoreTotal;
	}
	
}
