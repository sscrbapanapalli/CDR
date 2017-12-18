package com.cmacgm.cdrserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cmacgm.cdrserver.model.BatchHistoryDetail;

@Repository
public interface BatchHistoryDetailsRepository extends JpaRepository<BatchHistoryDetail, Long>  {
	
	public List<BatchHistoryDetail> findByAppId(@Param("appId") Long id);
	
		
		public BatchHistoryDetail findByAppIdAndBatchUploadMonthAndBatchUploadStatus(@Param("appId") Long appId, @Param("batchUploadMonth") String batchUploadMonth,@Param("batchUploadStatus") String batchUploadStatus);
	
	

}
