package com.simple.bets.modular.sys.model;

import com.simple.bets.core.base.model.BaseModel;
import com.simple.bets.modular.sys.utils.UserUtils;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author wangdingfeng
 * @Description //定时任务
 * @Date 15:15 2019/2/2
 **/

@Table(name = "sys_job")
public class JobModel extends BaseModel {

    /**
     * 任务调度参数key
     */
    public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";

    public enum ScheduleStatus {
        /**
         * 正常
         */
        NORMAL("0"),
        /**
         * 暂停
         */
        PAUSE("1");

        private String value;

        private ScheduleStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "job_id")
    private Long jobId;

    @Column(name = "bean_name")
    private String beanName;

    @Column(name = "method_name")
    private String methodName;

    @Column(name = "params")
    private String params;

    @Column(name = "cron_expression")
    private String cronExpression;

    @Column(name = "job_status")
    private String jobStatus;

    /**
     * @return JOB_ID
     */
    public Long getJobId() {
        return jobId;
    }

    /**
     * @param jobId
     */
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    /**
     * @return BEAN_NAME
     */
    public String getBeanName() {
        return beanName;
    }

    /**
     * @param beanName
     */
    public void setBeanName(String beanName) {
        this.beanName = beanName == null ? null : beanName.trim();
    }

    /**
     * @return METHOD_NAME
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * @param methodName
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName == null ? null : methodName.trim();
    }

    /**
     * @return PARAMS
     */
    public String getParams() {
        return params;
    }

    /**
     * @param params
     */
    public void setParams(String params) {
        this.params = params == null ? null : params.trim();
    }

    /**
     * @return CRON_EXPRESSION
     */
    public String getCronExpression() {
        return cronExpression;
    }

    /**
     * @param cronExpression
     */
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression == null ? null : cronExpression.trim();
    }


    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public void setCreator(String creator) {
        this.creator = creator;
        this.setCreatorName((UserUtils.getUserInfo(creator).getName()));
    }
}