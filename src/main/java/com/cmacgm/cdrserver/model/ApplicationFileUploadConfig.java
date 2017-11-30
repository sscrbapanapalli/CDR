package com.cmacgm.cdrserver.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "application_file_upload_config")
public class ApplicationFileUploadConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @OneToOne(targetEntity = Application.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "app_id", foreignKey = @ForeignKey(name = "FK_VERIFY_APP"))
    private Application application;   
    @Column(name="seq_no",length = 100)
    private Long seqNo;
    @Column(name="folder_caption",length = 100)
    private String folderCaption;
    @Column(name="file_name_prefix",length = 100)
    private String fileNamePrefix;  
    @Column(name="file_trgt_path",length = 100)    
    private String fileTrgtPath;
    @Column(name="file_ack_path",length = 100)
    private String fileAckPath; 
    @Column(name="validation_type",length = 100)
    private String validationType;
    @Column(name="created_date")
    private Date createdDate;
    @Column(name="updated_date")
    private Date updatedDate;
    @Column(name="active_indicator")
    private boolean activeIndicator;
    @Column(name="created_by",length = 100)
    private String createdBy;
    @Column(name="updated_by",length = 100)
    private String updatedBy;       
  

  


    public Long getId() {
		return id;
	}





	public void setId(Long id) {
		this.id = id;
	}





	public Application getApplication() {
		return application;
	}





	public void setApplication(Application application) {
		this.application = application;
	}





	public Long getSeqNo() {
		return seqNo;
	}





	public void setSeqNo(Long seqNo) {
		this.seqNo = seqNo;
	}





	public String getFolderCaption() {
		return folderCaption;
	}





	public void setFolderCaption(String folderCaption) {
		this.folderCaption = folderCaption;
	}





	public String getFileNamePrefix() {
		return fileNamePrefix;
	}





	public void setFileNamePrefix(String fileNamePrefix) {
		this.fileNamePrefix = fileNamePrefix;
	}





	public String getFileTrgtPath() {
		return fileTrgtPath;
	}





	public void setFileTrgtPath(String fileTrgtPath) {
		this.fileTrgtPath = fileTrgtPath;
	}





	public String getFileAckPath() {
		return fileAckPath;
	}





	public void setFileAckPath(String fileAckPath) {
		this.fileAckPath = fileAckPath;
	}





	public String getValidationType() {
		return validationType;
	}





	public void setValidationType(String validationType) {
		this.validationType = validationType;
	}





	public Date getCreatedDate() {
		return createdDate;
	}





	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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
        builder.append("ApplicationFileUploadConfig [id=").append(id).append("]").append("[folderCaption=").append(folderCaption).append("]")
        .append("[createdDate=").append(createdDate).append("]").append("[updatedDate=").append(updatedDate).append("]")
        .append("[activeIndicator=").append(activeIndicator).append("]").append("[createdBy=").append(createdBy).append("]")
      
        .append("[folderCaption=").append(folderCaption).append("]").append("[seqNo=").append(seqNo).append("]")
        .append("[fileNamePrefix=").append(fileNamePrefix).append("]").append("[fileTrgtPath=").append(fileTrgtPath).append("]")
        .append("[fileAckPath=").append(fileAckPath).append("]") .append("[validationType=").append(validationType).append("]").append("[updatedBy=").append(updatedBy).append("]")
        .append("[application=").append(application).append("]");
        return builder.toString();
    }
}