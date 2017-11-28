package com.cmacgm.cdrserver.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "batch_file_detail")
public class BatchFileDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;
    @Column(name="batch_id",length = 100)
    private String batchId;
    @Column(name="batch_file_name",length = 100)
    private String batchFileName;  
    @Column(name="batch_file_trgt_path",length = 100)
    private String batchFileTrgtPath;
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
  

    public BatchFileDetail() {
        super();
    }

    public BatchFileDetail(final String batchId) {
        super();
        this.batchId = batchId;
    }

    //

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }   
  
    public String getbatchId() {
		return batchId;
	}

	public void setbatchId(String batchId) {
		this.batchId = batchId;
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((batchId == null) ? 0 : batchId.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BatchFileDetail batchFileDetail = (BatchFileDetail) obj;
        if (!batchFileDetail.equals(batchFileDetail.batchId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BatchFileDetail [id=").append(id).append("]").append("[batchId=").append(batchId).append("]")
        .append("[createdDate=").append(createdDate).append("]").append("[updatedDate=").append(updatedDate).append("]")
        .append("[activeIndicator=").append(activeIndicator).append("]").append("[createdBy=").append(createdBy).append("]")
      
        .append("[batchId=").append(batchId).append("]")
        .append("[batchFileTrgtPath=").append(batchFileTrgtPath).append("]").append("[batchFileName=").append(batchFileName).append("]")
          .append("[updatedBy=").append(updatedBy).append("]");
        return builder.toString();
    }
}