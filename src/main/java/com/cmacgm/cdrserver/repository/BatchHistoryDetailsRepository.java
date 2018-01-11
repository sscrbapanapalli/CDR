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
	
	@Query(nativeQuery = true,value="SELECT TOP 1 * FROM [batch_history_detail] t WHERE t.[batch_upload_month]=:batchUploadMonth and t.[app_id]=:appId  order by [updated_date] desc")
	public  BatchHistoryDetail findByTop (@Param("batchUploadMonth") String batchUploadMonth,@Param("appId") Long appId);
	
	
	/*
	 * author:Ramesh Kumar B
	 * To get last upload record based on application for reverse operation
	 */
	/*@Query(nativeQuery=true,value="SELECT TOP 1 * FROM [batch_history_detail] t where t.[app_id]=:appId and t.[batch_upload_status]=:batchUploadStatus and etl_processed=:etlProcessed order by [batch_upload_cr_date] desc")
	public BatchHistoryDetail findByEtlProcessed (@Param("batchUploadStatus") String batchUploadStatus,@Param("appId") Long appId,@Param("etlProcessed") String etlProcessed);*/
	
	@Query(nativeQuery=true,value="SELECT TOP 1 * FROM [batch_history_detail] t where t.[app_id]=:appId and t.[batch_upload_status]=:batchUploadStatus and etl_processed IN(:etlProcessedNew,:etlProcessedCompleted) order by [batch_upload_cr_date] desc")
	public BatchHistoryDetail findByEtlProcessed (@Param("batchUploadStatus") String batchUploadStatus,@Param("appId") Long appId,@Param("etlProcessedNew") String etlProcessedNew,@Param("etlProcessedCompleted") String etlProcessedCompleted);

	public BatchHistoryDetail findById(@Param("selectedBatchUniqueId") Long selectedBatchUniqueId);
	
	@Query(nativeQuery=true,value="select * from [batch_history_detail] t where t.[app_id]=:appId and t.[batch_upload_status]=:batchReverseStatus and MONTH(created_date)=MONTH(GetDate()) and YEAR(created_date)=YEAR(GetDate())")
	public List<BatchHistoryDetail> getReverseBatchDetails(@Param("appId") Long appId,@Param("batchReverseStatus") String batchReverseStatus);
	
}
