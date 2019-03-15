package com.simple.bets.modular.sys.model;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author wangdingfeng
 * @Description //定时任务日志
 * @Date 15:15 2019/2/2
 **/

@Table(name = "sys_job_log")
public class JobLogModel implements Serializable {

    private static final long serialVersionUID = -7114915445674333148L;

    public static final String JOB_LOG_STATUS_SUCCESS = "0";//成功

    public static final String JOB_LOG_STATUS_ERROR = "1";//失败

    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "log_id")
    private Long logId;

    @Column(name = "job_id")
    private Long jobId;

    @Column(name = "bean_name")
    private String beanName;

    @Column(name = "method_name")
    private String methodName;

    @Column(name = "params")
    private String params;

    @Column(name = "status")
    private String status;

    @Column(name = "error")
    private String error;

    @Column(name = "times")
    private Long times;

    @Column(name = "create_time")
    private Date createTime;

    /**
     * @return LOG_ID
     */
    public Long getLogId() {
        return logId;
    }

    /**
     * @param logId
     */
    public void setLogId(Long logId) {
        this.logId = logId;
    }

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
     * @return STATUS
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * @return ERROR
     */
    public String getError() {
        return error;
    }

    /**
     * @param error
     */
    public void setError(String error) {
        this.error = error == null ? null : error.trim();
    }

    /**
     * @return TIMES
     */
    public Long getTimes() {
        return times;
    }

    /**
     * @param times
     */
    public void setTimes(Long times) {
        this.times = times;
    }

    /**
     * @return CREATE_TIME
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}