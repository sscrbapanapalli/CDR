package com.cmacgm.cdrserver.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "batch_history_detail")
public class BatchHistoryDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;
    /*@OneToOne(targetEntity = BatchFileDetail.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "batch_id", foreignKey = @ForeignKey(name = "FK_VERIFY_BATCHFILE"))
    private BatchFileDetail batchFileDetail;*/
    
    
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn( name = "batch_history_id" , referencedColumnName = "id")
    private List<BatchFileDetail> batchFileDetailList=new ArrayList<>();
    
    @Column(name="app_id")
    private Long appId;
    
    @Column(name="batch_id")
    private String batchId;
    @Column(name="batch_upload_month",length = 100)
    private String batchUploadMonth;  
    @Column(name="batch_upload_user_name",length = 100)
    private String batchUploadUserName;
    @Column(name="batch_upload_status",length = 20)
    private String batchUploadStatus;
    
    @Column(name="etl_processed", length=10)
    private String etlProcessed;
    
    @Column(name="etl_action_status" , length=50)
    private String etlActionStatus;
    
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="batch_upload_cr_date", length = 100)
    private Date batchUploadCrDate;
    
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_date")
    private Date createdDate;
    
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="updated_date")
    private Date updatedDate;
    @Column(name="active_indicator")
    private boolean activeIndicator;
    @Column(name="created_by",length = 100)
    private String createdBy;
    @Column(name="updated_by",length = 100)
    private String updatedBy;    
  
    //


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/*public BatchFileDetail getBatchFileDetail() {
		return batchFileDetail;
	}

	public void setBatchFileDetail(BatchFileDetail batchFileDetail) {
		this.batchFileDetail = batchFileDetail;
	}*/
	
	

	public void setBatchFileDetailList(List<BatchFileDetail> batchFileDetailList) {
		this.batchFileDetailList = batchFileDetailList;
	}
	
	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public List<BatchFileDetail> getBatchFileDetailList() {
		return batchFileDetailList;
	}
	
	public String getBatchUploadMonth() {
		return batchUploadMonth;
	}


	public void setBatchUploadMonth(String batchUploadMonth) {
		this.batchUploadMonth = batchUploadMonth;
	}

	public String getBatchUploadUserName() {
		return batchUploadUserName;
	}

	public void setBatchUploadUserName(String batchUploadUserName) {
		this.batchUploadUserName = batchUploadUserName;
	}

	public String getBatchUploadStatus() {
		return batchUploadStatus;
	}

	public void setBatchUploadStatus(String batchUploadStatus) {
		this.batchUploadStatus = batchUploadStatus;
	}
	
	

	public String getEtlProcessed() {
		return etlProcessed;
	}

	public void setEtlProcessed(String etlProcessed) {
		this.etlProcessed = etlProcessed;
	}

	public String getEtlActionStatus() {
		return etlActionStatus;
	}

	public void setEtlActionStatus(String etlActionStatus) {
		this.etlActionStatus = etlActionStatus;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getBatchUploadCrDate() {
		return batchUploadCrDate;
	}

	public void setBatchUploadCrDate(Date batchUploadCrDate) {
		this.batchUploadCrDate = batchUploadCrDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public boolean isActiveIndicator() {
		return activeIndicator;
	}

	public void setActiveIndicator(boolean activeIndicator) {
		this.activeIndicator = activeIndicator;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

   
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BatchHistoryDetail [id=").append(id).append("]").append("[batchUploadMonth=").append(batchUploadMonth).append("]")
        .append("[createdDate=").append(createdDate).append("]").append("[updatedDate=").append(updatedDate).append("]")
        .append("[activeIndicator=").append(activeIndicator).append("]").append("[createdBy=").append(createdBy).append("]")      
        .append("[batchUploadUserName=").append(batchUploadUserName).append("]")
        .append("[appId=").append(appId).append("]")
        .append("[etlProcessed=").append(etlProcessed).append("]")
        .append("[etlActionStatus=").append(etlActionStatus).append("]")
        .append("[batchUploadStatus=").append(batchUploadStatus).append("]").append("[batchUploadCrDate=").append(batchUploadCrDate).append("]")
          .append("[batchFileDetailList=").append(batchFileDetailList).append("]");
        return builder.toString();
    }

}