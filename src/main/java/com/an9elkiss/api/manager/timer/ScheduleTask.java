package com.an9elkiss.api.manager.timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.an9elkiss.api.manager.command.SystemToken;
import com.an9elkiss.api.manager.command.TokenCmd;
import com.an9elkiss.api.manager.command.UserPersonCmd;
import com.an9elkiss.api.manager.constant.GroupManager;
import com.an9elkiss.api.manager.service.CodeReviewService;
import com.an9elkiss.api.manager.service.FileTreeService;
import com.an9elkiss.api.manager.service.ShareService;
import com.an9elkiss.api.manager.service.TaskService;
import com.an9elkiss.api.manager.wechart.model.WeChartMessage;
import com.an9elkiss.api.manager.wechart.service.SendTextMessageService;
import com.an9elkiss.commons.auth.model.User;
import com.an9elkiss.commons.command.ApiResponseCmd;
import com.an9elkiss.commons.util.JsonUtils;

/**
 * 定时任务，用于定时统计系统各部分信息， 对于不达标情况进行对人员消息的通知功能
 * 
 * @author ysh10321
 *
 */
@Configuration
@Component
@EnableScheduling // 该注解必须要加
public class ScheduleTask {

	private final Logger LOGGER = LoggerFactory.getLogger(ScheduleTask.class);

	@Autowired
	private CodeReviewService codeReviewService;

	@Autowired
	private ShareService shareService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private FileTreeService fileTreeService;

	// 企业微信业务层注入
	@Autowired
	SendTextMessageService sendTextMessageService;

	// 创建radis中token的用户名
	@Value("${system.name}")
	private String name;

	// 创建radis中token的id
	@Value("${system.id}")
	private Integer id;

	// codereviewtask 每月的 计划数量
	@Value("${scheduletask.codereview.planNum}")
	private Integer codereviewPlanNum;

	// makebetter 每月的 计划数量
	@Value("${scheduletask.makebetter.planNum}")
	private Integer makebetterPlanNum;

	// share 每月的 计划数量
	@Value("${scheduletask.share.planNum}")
	private Integer sharePlanNum;

	// performance 每月的 计划数量
	@Value("${scheduletask.performance.planNum}")
	private Integer performancePlanNum;

	// conversation 每月的 计划数量
	@Value("${scheduletask.conversation.planNum}")
	private Integer conversationPlanNum;

	/**
	 * 对系统中每个小组进行统计每组CodeReview的数量 达标标准：每组中/每人/每月/一次 定时： 通知：组长
	 */
	// @Scheduled(cron = "0/2 * * * * ?")
	@Scheduled(cron = "${scheduletask.codereview.cron}")
	public void codereviewtask() {
		LOGGER.info("系统系统定时任务统计codeReview数量开始");
		ApiResponseCmd<Map<String, List<Integer>>> apiResponseCmd = codeReviewService
				.statisticalCodeReviewByGroup(getSystemToken());
		String taskName = "codeReview";
		taskInterchangeable(apiResponseCmd, codereviewPlanNum, taskName);
		LOGGER.info("系统系统定时任务统计codeReview数量结束");

	}

	/**
	 * 对系统中每个小组进行统计每组任务持续改进的数量 达标标准：每组/每月/四次 定时： 通知：组长
	 */
	// @Scheduled(cron = "0/2 * * * * ?")
	@Scheduled(cron = "${scheduletask.makebetter.cron}")
	public void makebettertask() {
		LOGGER.info("系统系统定时任务统计makebetter数量开始");
		ApiResponseCmd<Map<String, List<Integer>>> apiResponseCmd = taskService
				.statisticalTaskMakeBetterByGroup(getSystemToken());
		String taskName = "makebetter(持续改进)";
		taskInterchangeable(apiResponseCmd, makebetterPlanNum, taskName);
		LOGGER.info("系统系统定时任务统计makebetter数量结束");
	}

	/**
	 * 对系统中每个小组进行统计每组分享会的数量 达标标准：每组/每月/俩次 定时： 通知：组长
	 */
	// @Scheduled(cron = "0/2 * * * * ?")
	@Scheduled(cron = "${scheduletask.share.cron}")
	public void sharetask() {
		String taskName = "share(分享会)";
		LOGGER.info("系统系统定时任务统计{}数量开始", taskName);
		ApiResponseCmd<Map<String, List<Integer>>> apiResponseCmd = shareService
				.statisticalShareByGroup(getSystemToken());
		taskInterchangeable(apiResponseCmd, sharePlanNum, taskName);
		LOGGER.info("系统系统定时任务统计{}数量结束", taskName);
	}

	/**
	 * 对系统中每个小组进行统计每组绩效的数量 达标标准：每组/每月/一次 定时： 通知：组长
	 */
	// @Scheduled(cron = "0/2 * * * * ?")
	@Scheduled(cron = "${scheduletask.performance.cron}")
	public void performancetask() {
		String taskName = "performance(绩效)";
		LOGGER.info("系统系统定时任务统计{}数量开始", taskName);
		ApiResponseCmd<Map<String, List<Integer>>> achievements = fileTreeService.checkoutPreMonthAchievements();
		taskInterchangeable(achievements, performancePlanNum, taskName);

		LOGGER.info("系统系统定时任务统计{}数量结束", taskName);
	}

	/**
	 * 对系统中每个小组进行统计每组心声的数量 达标标准：每组中/每人/每月/一次 定时： 通知：组长
	 */
	// @Scheduled(cron = "0/20 * * * * ?")
	@Scheduled(cron = "${scheduletask.conversation.cron}")
	public void conversationtask() {
		String taskName = "conversation(心声)";
		LOGGER.info("系统系统定时任务统计{}数量开始", taskName);
		ApiResponseCmd<Map<String, List<Integer>>> heartSound = fileTreeService
				.checkoutPreMonthHeartSound(getSystemToken());
		taskInterchangeable(heartSound, conversationPlanNum, taskName);

		LOGGER.info("系统系统定时任务统计{}数量结束", taskName);

	}

	/**
	 * 获取当月的天数
	 * 
	 * @return
	 */
	public static int getCurrentMonthLastDay() {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	private String getSystemToken() {
		User user = new User();
		user.setId(id);
		user.setName(name);
		TokenCmd tokenCmd = SystemToken.getTokenCmd(user);
		SystemToken.setTokenCmd(tokenCmd);
		String token = tokenCmd.getToken();
		return token;
	}

	/**
	 * 用于各个统计定时任务的公共部分
	 * 
	 * @param apiResponseCmd
	 *            统计结果
	 * @param planNum
	 *            计划数量
	 * @param taskName
	 *            任务名称
	 */
	private void taskInterchangeable(ApiResponseCmd<Map<String, List<Integer>>> apiResponseCmd, Integer planNum,
			String taskName) {
		Map<String, List<Integer>> data = apiResponseCmd.getData();
		if (null == planNum) {
			LOGGER.error("统计参数{}错误", planNum);
		}
		WeChartMessage weChartMessage = new WeChartMessage();

		for (GroupManager groupManager : GroupManager.values()) {

			for (String str : data.keySet()) {

				List<Integer> list = data.get(str);
				UserPersonCmd userPersonCmd = JsonUtils.parse(str, UserPersonCmd.class);
				if (groupManager.getId().equals(userPersonCmd.getUserId())) {
					if (!list.isEmpty()) {
						Integer actualNum = list.get(list.size() - 1);

						if (null == actualNum) {
							LOGGER.error("{}定时任务统计数据库中无数据", taskName);
							break;
						}

						String[] persons = { groupManager.getWechartId() };
						String message = null;
						// 月底
						if (new SimpleDateFormat("dd").format(new Date()).equals("25")) {
							if (taskName.equals("codeReview") || taskName.equals("conversation(心声)")) {
								planNum = userPersonCmd.getUserNumber();
							}
							if (actualNum >= planNum) {
								LOGGER.info("系统定时任务。月底：{}的{}任务达标  无消息提示", userPersonCmd.getName(), taskName);
								break;
							}
							message = String.format("%1s，亲，月底了，本月的%2s任务未达标，应该达标数量为：%3s。  实际达标数量为%4s！",
									userPersonCmd.getName(), taskName, planNum, actualNum);
							weChartMessage.setContent(message);
							weChartMessage.setPersons(persons);
							sendTextMessageService.sendMessage(weChartMessage);
							LOGGER.info("系统定时任务。月底：{}的{}任务不达标  消息提示:{}", userPersonCmd.getName(), taskName,
									weChartMessage.getContent());
							break;
						}
						// 月中
						if (new SimpleDateFormat("dd").format(new Date()).equals(getCurrentMonthLastDay() / 2)) {
							if (actualNum >= planNum / 2) {
								LOGGER.info("系统定时任务。月中：{}的{}任务达标  无消息提示", userPersonCmd.getName(), taskName);
								break;
							}
							message = String.format("%1s，亲，月中了，本月的%2s任务的一半未达标，应该达标数量为：%3s实际达标数量为%4s！",
									userPersonCmd.getName(), taskName, planNum, actualNum);

							weChartMessage.setContent(message);
							weChartMessage.setPersons(persons);
							sendTextMessageService.sendMessage(weChartMessage);
							LOGGER.info("系统定时任务。月中：{}的{}任务不达标  消息提示:{}", userPersonCmd.getName(), taskName);
							break;
						}

					} else {
						LOGGER.error("系统定时任务。{}任务统计数量错误", taskName);

					}
					LOGGER.error("系统定时任务。{}任务,{}组", taskName, taskName, groupManager.getName());
				}

			}

		}
	}
}
