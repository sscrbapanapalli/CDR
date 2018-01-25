/* @author Basker Ammu, Ramesh Kumar B
 * Url Routing configuration
 * $stateProvider,$urlRouterProvider
 */

'use strict';
angular.module("cdrApp", [ "ui.router",'appConfigApp' ,'commonServiceApp','angularUtils.directives.dirPagination']);

'use strict';
angular.module('cdrApp').config(
		[ '$stateProvider', '$urlRouterProvider',
				function($stateProvider, $urlRouterProvider) {
				
					$urlRouterProvider.otherwise("/login");
					$stateProvider.state("login", {
						url : '/login',
						templateUrl : 'view/login.html',
						controller : "loginController",
						controllerAs : "loginController",
						data:{requireLogin:true}

					}).state("upload", {
						url : '/upload',
						templateUrl : 'view/upload.html',
						controller : "uploadController",
						controllerAs : "uploadController",
						
						data:{requireLogin:true}

					}).state("home", {
						url : '/home',
						templateUrl : 'view/home.html',
						controller : "homeController",
						controllerAs : "homeController",
						data:{requireLogin:true}

					}).state("app", {
						url : '/app?',
						templateUrl : 'view/home.html',
						controller : "homeController",
						controllerAs : "homeController",
						
					    params: { 
					    	appId: null,   // can initialise to default value
					      
					    },
						data:{requireLogin:true}

					}).state("userSettings", {
						url : '/userSettings',
						templateUrl : 'view/userSettings.html',
						controller : "settingsController",
						controllerAs : "settingsController",
						data:{requireLogin:true}

					}).state("applicationSettings",{
						url : '/applicationSettings',
						templateUrl :'view/applicationSettings.html',
						controller :"settingsController",
						controllerAs : "settingsController",
						data:{requireLogin:true}
					});

				} ]);


angular
		.module('cdrApp')
		.controller(
				'homeController',
				[
						'$scope',
						'$state',
						'$rootScope',
						'$window',
						'$q',
						'$http',
						'appConstants','userService','globalServices','AuthenticationService','$stateParams','anchorSmoothScroll','$location','$filter',
						function($scope, $state, $rootScope, $window, $q,
								$http,appConstants,userService,globalServices,AuthenticationService,$stateParams,anchorSmoothScroll,$location,$filter) {
							$scope.homepageContent = "landing dashboard page";
							//$scope.batchFiles={fileName:'', filePath:''};
							$scope.batchFileslist=[];
							$scope.checkStatus=false;
							$scope.welcomeMsg=false;
							$scope.userId=0;
							$scope.currentPage = 1;
							$scope.pageSize = 6;
							$scope.currentPagetest = 1;
							$scope.pageSizetest = 6;
							$scope.showChild=false;		
							$scope.currentUploadDetailsResult={};							
							$scope.selectedId = undefined;
							$scope.selectedIdChild = undefined;
							$scope.resetParent=function(){
							$scope.currentPage = 1;
							$scope.pageSize = 6;
									
									  $scope.search=""
										  if ( $window.sessionStorage.getItem('appId')  != undefined
													|| $window.sessionStorage.getItem('appId')  != null) {						
								            	
										  $scope.batchHistoryDetails( $window.sessionStorage.getItem('appId'));
										  }
								}
								$scope.resetChild=function(){
									  $scope.searchBatch=""
										  $scope.currentPagetest = 1;
									  $scope.pageSizetest = 6;
										  
								}
								
								$scope.ClassOdd = function(id)
								{
									if(id === $scope.selectedId)
										return "selected";
									else 
										return "odd";
								};
								
								$scope.ClassEven = function(id)
								{
									if(id === $scope.selectedId)
										return "selected";
									else 
										return "even";
								
								};
								
								$scope.ClassOddChild = function(id)
								{
									if(id === $scope.selectedIdChild)
										return "selected";
									else 
										return "odd";
								};
								
								$scope.ClassEvenChild = function(id)
								{
									if(id === $scope.selectedIdChild)
										return "selected";
									else 
										return "even";
								
								};
								
								
								
							//$rootscope.selectedAppId=0;
							$scope.inituser = function() {
								var data = globalServices.isUserTokenAvailable();
								if (data == null || data == undefined) {
									$rootScope.isProfilePage = false;
									$state.go("login");
								} else {
									$rootScope.currentUser = userService.getCurrentUser();
									if ($rootScope.currentUser != undefined
											|| $rootScope.currentUser != null) {
										$rootScope.isProfilePage = true;
									} else {
										$rootScope.isProfilePage = false;
										$state.go("login");
									}

								}
							}
							$scope.logout = function () {		
								   AuthenticationService.ClearCredentials();  
								   $rootScope.isProfilePage=false;
								   
							   }
							
							$scope.batchFiles=function(row,id){
								 $scope.selectedIdChild = undefined;
								$scope.selectedId = row;
								$scope.checkStatus=true;
								$scope.inituser();
								
							
								//alert(s);
							  	   $scope.batchFileslist=[];								
								  for(var i=0; i<$scope.batchDetailsResult.length; i++) {									
								   if(id==$scope.batchDetailsResult[i].id){
								   $scope.batchFileslist=$scope.batchDetailsResult[i].batchFileDetailList;		
								  						   
								      // call $anchorScroll()
								      anchorSmoothScroll.scrollTo('bottom');
								   }
								  }
								/*$scope.batchFiles.fileName=$scope.batchDetailsResult[s].batchFileDetail.batchFileName;
								$scope.batchFiles.filePath=$scope.batchDetailsResult[s].batchFileDetail.batchFileTrgtPath;
								console.log('got selected batch details', $scope.batchFiles)
								console.log($scope.batchFiles.fileName)
								console.log($scope.batchFiles.filePath)*/
							}

					
							// To get application list
							
							$scope.init = function() {
								$scope.inituser();
								if ($rootScope.currentUser!=undefined )
									if($rootScope.currentUser.userId != undefined
										||$rootScope.currentUser.userId != null) {
								$scope.userId=$rootScope.currentUser.userId;
								}
								
								$scope.user={};
								$scope.welcomeMsg=false;
								//$rootScope.selectedAppId='';
								console.log($scope.userId)
								var data = new FormData();
								/*var url = "/api/applications/"

								$http.get(url).then(function(response) {
									$scope.applications = response.data;
									
								}, function(response) {

									$scope.applications = response.data;
									
									
								});*/
								
								var url =  appConstants.serverUrl+"/login/getUserAuthDetails/" + $window.sessionStorage.getItem('userToken');
								
								$http.get(url).then(function(response) {
									
												$scope.applications=response.data.applications;	
												$scope.roles=response.data.roles;	
											console.log(' userDetails' , response)
											console.log(' applications' , $scope.applications)
											console.log(' roles' , $scope.roles)
											
										});
								
								 var appId = $stateParams.appId
									if(appId!=undefined )
									$scope.batchHistoryDetails(appId);
								
							};
							
							$scope.selectRecordRow=function(row) {
								$scope.selectedIdChild = row;
							}
						
							$scope.batchHistoryDetails = function(appId) {
								  $scope.selectedId = undefined;
								  $scope.selectedIdChild = undefined;
								  $scope.checkStatus=false;
								$scope.batchDetailsResult=[];
								$scope.batchFileslist=[];
								$scope.inituser();
								$scope.welcomeMsg=true;
								$scope.appId = appId;							
								if ($scope.appId  != undefined
										||$scope.appId  != null) {									
									$window.sessionStorage.setItem('appId',appId); 
					            	console.log('user selected app id in session' , $window.sessionStorage.getItem('appId'))
					            	   if($window.sessionStorage.getItem('appId')!=undefined){
			                                var url1=appConstants.serverUrl+"/api/currentUploadDetails/"+ $window.sessionStorage.getItem('appId');
											var url= appConstants.serverUrl+"/api/batchHistoryDetails/" + $window.sessionStorage.getItem('appId');
											
											
											console.log(url)
											var data = new FormData();
											
											$http.get(url1).then(function(response) {
												$scope.currentUploadDetailsResult = response.data;

												console.log('currentUploadDetailsResult', $scope.currentUploadDetailsResult);
												
											}, function(response) {
												$rootScope.buttonClicked = response.data;
												$rootScope.showModal = !$rootScope.showModal;
												$rootScope.contentColor = "#dd4b39";
												
												
											});

											$http.get(url).then(function(response) {
												$scope.batchDetailsResult = response.data;
												 angular.forEach($scope.batchDetailsResult, function(batchDetailsResult){
													   batchDetailsResult.formattedDate = $filter('date')(batchDetailsResult['batchUploadCrDate'],'yyyy-MM-dd HH:mm:ss');
												      }) ;
												      
												       $scope.batchDetailsResult =  $scope.batchDetailsResult;
												console.log('full batch his details', $scope.batchDetailsResult);
												
											}, function(response) {
												   $rootScope.buttonClicked = response.data;
													$rootScope.showModal = !$rootScope.showModal;
													  $rootScope.contentColor = "#dd4b39";
												
												
											});
											}
					
							};
							}
							$scope.hideChildTable=function(){
								 $scope.checkStatus=false;
							}
							
							$scope.sort = function(keyname){
								$scope.sortParent = keyname;   //set the sortKey to the param passed
								$scope.reverse = !$scope.reverse; //if true make it false and vice versa
								
								
							}
							
							$scope.sortBatch = function(keyname){
								$scope.sortChild = keyname;   //set the sortKey to the param passed
								$scope.reverseBatch = !$scope.reverseBatch; //if true make it false and vice versa
								
								
							}
						
							
							$scope.doUpload = function() {
								$rootScope.isProfilePage = true;
								
								$state.go("upload");
								$scope.checkStatus=false;
								
							}

							   $scope.showDialog = function(flag) {
							        jQuery("#confirmation-dialog .modal").modal(flag ? 'show' : 'hide');
							      };
									$scope.doReverse = function(id,batchId){
										console.log('in  do reverese')
									
										 $scope.confirmationDialogConfig = {
										      title: "REVERSE UPLOAD",
										      message: "Are you sure you want to Reverse?",
										      buttons: [{
										        label: "Reverse",
										        action: "Reverse"
										      }],
										      id:id,
										      batchId:batchId
										    };
										    $scope.showDialog(true);			    
										
									
									}
									$scope.confirmReverse =function(id){
										   console.log($window.sessionStorage.getItem('appId'))
										     console.log( $rootScope.currentUser.userName)
										       console.log(id)
										if($rootScope.currentUser!=undefined){
											
											var config = {
													transformRequest : angular.identity,
													transformResponse : angular.identity,
													headers : {
														'Content-Type' : undefined
													}
												}
										var url=appConstants.serverUrl+"/api/reverseUpload/";
										var data = new FormData();
										data.append("applicationId" ,$window.sessionStorage.getItem('appId'));
										data.append("userName", $rootScope.currentUser.userName);
										data.append("selectedBatchUniqueId", id);
										console.log(data)
							
										$http.post(url,data,config).then(
												function(response){
													
													  $('body').removeClass().removeAttr('style');$('.modal-backdrop').remove(); // added by me
													      $rootScope.buttonClicked = response.data;
														  $rootScope.showModal = !$rootScope.showModal;
														  $rootScope.contentColor = "#78b266";
														  $state.go("app", {appId:$window.sessionStorage.getItem('appId')}, {reload: true}); 
														
												},function(response){
													
													  $('body').removeClass().removeAttr('style');$('.modal-backdrop').remove(); // added by me
													      $rootScope.buttonClicked = response.data;
														  $rootScope.showModal = !$rootScope.showModal;
														  $rootScope.contentColor = "#dd4b39";
												});							
										}
										
									}
			
						} ]);



angular.module('cdrApp').service('anchorSmoothScroll', function(){
    
    this.scrollTo = function(eID) {

        // This scrolling function 
        // is from http://www.itnewb.com/tutorial/Creating-the-Smooth-Scroll-Effect-with-JavaScript
        
        var startY = currentYPosition();
        var stopY = elmYPosition(eID);
        var distance = stopY > startY ? stopY - startY : startY - stopY;
        if (distance < 100) {
            scrollTo(0, stopY); return;
        }
        var speed = Math.round(distance / 100);
        if (speed >= 20) speed = 20;
        var step = Math.round(distance / 25);
        var leapY = stopY > startY ? startY + step : startY - step;
        var timer = 0;
        if (stopY > startY) {
            for ( var i=startY; i<stopY; i+=step ) {
                setTimeout("window.scrollTo(0, "+leapY+")", timer * speed);
                leapY += step; if (leapY > stopY) leapY = stopY; timer++;
            } return;
        }
        for ( var i=startY; i>stopY; i-=step ) {
            setTimeout("window.scrollTo(0, "+leapY+")", timer * speed);
            leapY -= step; if (leapY < stopY) leapY = stopY; timer++;
        }
        
        function currentYPosition() {
            // Firefox, Chrome, Opera, Safari
            if (self.pageYOffset) return self.pageYOffset;
            // Internet Explorer 6 - standards mode
            if (document.documentElement && document.documentElement.scrollTop)
                return document.documentElement.scrollTop;
            // Internet Explorer 6, 7 and 8
            if (document.body.scrollTop) return document.body.scrollTop;
            return 0;
        }
        
        function elmYPosition(eID) {
            var elm = document.getElementById(eID);
            var y = elm.offsetTop;
            var node = elm;
            while (node.offsetParent && node.offsetParent != document.body) {
                node = node.offsetParent;
                y += node.offsetTop;
            } return y;
        }

    };
    
});
angular.module('cdrApp')
.factory('AuthenticationService',
	    [ '$http', '$state',"$window",'$rootScope','appConstants','globalServices',
	    function ($http, $state,$window,$rootScope,appConstants,globalServices) {
	    	
	        var service = {};	     
	        service.Login = function (userName, password, callback) {     
	           $http.post(appConstants.serverUrl+"/login/loginUser", {userName:userName,password:encryptString (password)},
						{
					headers : {
						'Accept' : 'application/json',
						'Content-Type' : 'application/json'
					}})
	                .success(function (response) {  
	                
							if(response.message!='failure' && response.data.authStatus){
								service.SetCredentials(response);		                	       
							} 
							 callback(response);	
						
	                   
	                });		

	        };
	 
	        service.SetCredentials = function (response) {        	
	                 $window.sessionStorage.setItem('userToken',response.data.userToken); 
	                 $http.defaults.headers.common['userToken'] =response.data.userToken;	           
	        };
	 
	        service.ClearCredentials = function () {
	        	  
	        	$rootScope.username="";
	        	if(globalServices.isUserTokenAvailable()!=null&&globalServices.isUserTokenAvailable()!=undefined){	
	        		var logoutUrl = appConstants.serverUrl+"/login/logOut/"
					+ globalServices.isUserTokenAvailable()
			$http(
					{
						method : "POST",
						url : logoutUrl,
						headers : {
							"userToken" : globalServices.isUserTokenAvailable()
						}
					})	        	
	             .success(function (response) {                	
							if(response){
								if($rootScope.currentUser.userName!=undefined && $rootScope.currentUser.userName!=null)
									$rootScope.username=$rootScope.currentUser.userName								
									 $('body').removeClass().removeAttr('style');$('.modal-backdrop').remove(); // added by me
								    $rootScope.isProfilePage=false;
								    $rootScope.currentUser={};	
								    $rootScope.username="";
								 $window.sessionStorage.removeItem('userToken');
								 $window.sessionStorage.removeItem('currentUser');		
								 $window.sessionStorage.removeItem('appId');
								
								  $http.defaults.headers.common['userToken']==null;
								  $rootScope.buttonClicked = "Logout Successfully";
									$rootScope.showModal = !$rootScope.showModal;
									  $rootScope.contentColor = "#78b266";
				 
								  $rootScope.isProfilePage=false;
								  $state.go("login");
																
							}else{
								 $('body').removeClass().removeAttr('style');$('.modal-backdrop').remove(); // added by me
								  $rootScope.buttonClicked = "LogOut Error";
									$rootScope.showModal = !$rootScope.showModal;
									  $rootScope.contentColor = "#dd4b39";
							}
								
						
	             	       	
	                
	             });
	        	}
	           
	        };       
	      
	 
	        return service;
	    }]);



angular.module('cdrApp').controller(
		'settingsController',
		[
				'$scope',
				'$state',
				'$rootScope',
				'$window',
				'$q',
				'$http',
				'appConstants','userService','globalServices','AuthenticationService',
				function($scope, $state, $rootScope, $window, $q, $http,
						appConstants,userService,globalServices,AuthenticationService) {
					$scope.homepageContent = "settings dashboard page";
					$scope.applicationName="";
					$scope.targetPath="";
					$scope.archivePath="";
					$scope.selectedFileType = "";
					$scope.folderMapping = [];
					$scope.fileTypeList = [{id: 1, type: '.xls'}, {id: 2, type: '.xlsx'},{id: 3, type: '.pdf'}];
					
					
					
					$scope.inituser = function() {
						var data = globalServices.isUserTokenAvailable();
						if (data == null || data == undefined) {
							$rootScope.isProfilePage = false;
							$state.go("login");
						} else {
							$rootScope.currentUser = userService.getCurrentUser();
							if ($rootScope.currentUser != undefined
									|| $rootScope.currentUser != null) {
								$rootScope.isProfilePage = true;
							} else {
								$rootScope.isProfilePage = false;
								$state.go("login");
							}

						}
					}
					
					$scope.init=function(){
                        var url =  appConstants.serverUrl+"/login/getUserAuthDetails/"+$window.sessionStorage.getItem('userToken');
                        var masterAppUrl =  appConstants.serverUrl+"/admin/getAllApplications/";	
                        var masterRoleUrl =  appConstants.serverUrl+"/admin/getAllRoles/";
                        
                        $http.get(url).then(function(response) {
								
											$scope.applications=response.data.applications;	
											$scope.roles=response.data.roles;	
										console.log(' settings userDetails' , response)
									});
                        
                        $http.get(masterAppUrl).then(function(response) {
                        	console.log(' allApplications ' , response)
                        	$scope.allApplications=response.data;	
                        	
                        	
                        });
                        $http.get(masterRoleUrl).then(function(response) {
                        	console.log(' allRoles ' , response)
                        	$scope.allRoles=response.data;	
                        	
                        	
                        });
							$scope.inituser();
						}
					$scope.userConfig=function(){
						var userName=$scope.userName;
						var roleId=$scope.userRole;
						var selectedAppId=$scope.selectedAppId;
						var propsLength=0;
						$scope.productSelection=[];
						$scope.folderMapping = [];
						
						$scope.folderMappingObj = {};
						 /*$rootScope.currentUser=userService.getCurrentUser();*/
						var createdBy=$rootScope.currentUser.userName;
						console.log(userName , roleId , selectedAppId , createdBy)
						var appCongigUrl=appConstants.serverUrl+"/admin/setUserConfiguration/";
						var data=new FormData();
						data.append("userName", userName);
						data.append("roleId", roleId);
						data.append("selectedAppId", selectedAppId);
						data.append("createdBy", createdBy);
						console.log($scope.userRole)
						
						var config = {
								transformRequest : angular.identity,
								transformResponse : angular.identity,
								headers : {
									'Content-Type' : undefined
								}
							}
						if(userName==null || userName==undefined || userName==""){
							$rootScope.buttonClicked = "Please provide User Id detail";
							$rootScope.showModal = !$rootScope.showModal;
							  $rootScope.contentColor = "#dd4b39";
							
						}else if(roleId==null || roleId==undefined || roleId==""){
							$rootScope.buttonClicked = "Please select User roles";
							$rootScope.showModal = !$rootScope.showModal;
							  $rootScope.contentColor = "#dd4b39";
							
						}else if(selectedAppId==null || selectedAppId==undefined || selectedAppId==""){
							$rootScope.buttonClicked = "Please select Application";
							$rootScope.showModal = !$rootScope.showModal;
							  $rootScope.contentColor = "#dd4b39";
							
						}else{
						$http.post(appCongigUrl,data,config).then(
								function(response){
									  $rootScope.buttonClicked = response.data;
										$rootScope.showModal = !$rootScope.showModal;
										  $rootScope.contentColor = "#78b266";
										  
									
								},function(response){
									
									 $rootScope.buttonClicked = response.data;
										$rootScope.showModal = !$rootScope.showModal;
										  $rootScope.contentColor = "#dd4b39";
								});
						}
					}
					

			            $scope.Add = function () {
			            	
			                //Add the new item to the Array.
			            	if($scope.folderName==null || $scope.folderName==undefined ||$scope.folderName=="" 
			            	   ||$scope.fileName==null || $scope.fileName==undefined || $scope.fileName==""){
			            		$rootScope.buttonClicked = "Please provide Folder Mapping Details";
								$rootScope.showModal = !$rootScope.showModal;
								  $rootScope.contentColor = "#dd4b39";
			            	}else{
			                var folderMappingObj = {};
			                
			                folderMappingObj.folderName = $scope.folderName;
			                folderMappingObj.fileName = $scope.fileName;
			                $scope.folderMapping.push(folderMappingObj);
			            	}
			                //Clear the TextBoxes.
			                $scope.folderName = "";
			                $scope.fileName = "";
			            	
			            };

			            $scope.Remove = function (index) {
			                //Find the record using Index from Array.
			                var name = $scope.folderMapping[index].folderName;
			                if ($window.confirm("Do you want to delete folder: " + name)) {
			                    //Remove the item from Array using Index.
			                    $scope.folderMapping.splice(index, 1);
			                }
			            }
			            
			            $scope.appConfig=function(){
			            	
			            	 var folderMappingObj = {};
				                folderMappingObj.folderName = $scope.folderName;
				                folderMappingObj.fileName = $scope.fileName;
				                $scope.folderMapping.push(folderMappingObj);
				                
				             var url =  appConstants.serverUrl+"/admin/setApplicationConfig/";
				             
				             var config = {
										transformRequest : angular.identity,
										transformResponse : angular.identity,
										headers : {
											'Content-Type' : undefined
										}
									}
				             
				             var dataObj = {
				            		 applicationName : $scope.applicationName,
				            		 targetPath : $scope.targetPath,
				            		 archivePath : $scope.archivePath,
				            		 selectedFileType :$scope.selectedFileType,
				            		 folderMapping :$scope.folderMapping,
				            		 userName :$rootScope.currentUser.userName
				     		};
				             
				             if($scope.applicationName==null || $scope.applicationName==undefined || $scope.applicationName==""){
				            	 $rootScope.buttonClicked = "Please provide Application Name";
									$rootScope.showModal = !$rootScope.showModal;
									  $rootScope.contentColor = "#dd4b39";
				             }else if($scope.targetPath==null || $scope.targetPath==undefined || $scope.targetPath==""){
				            	 $rootScope.buttonClicked = "Please provide Target Path details";
									$rootScope.showModal = !$rootScope.showModal;
									  $rootScope.contentColor = "#dd4b39";				            	 
				             }else if($scope.archivePath==null || $scope.archivePath==undefined || $scope.archivePath==""){
				            	 $rootScope.buttonClicked = "Please provide Archive Path details";
									$rootScope.showModal = !$rootScope.showModal;
									  $rootScope.contentColor = "#dd4b39";				            	 
				             }else if($scope.selectedFileType==null || $scope.selectedFileType==undefined || $scope.selectedFileType==""){
				            	 $rootScope.buttonClicked = "Please select file type";
									$rootScope.showModal = !$rootScope.showModal;
									  $rootScope.contentColor = "#dd4b39";				            	 
				             }else if($scope.folderName==null || $scope.folderName==undefined ||$scope.folderName=="" 
				            	   ||$scope.fileName==null || $scope.fileName==undefined || $scope.fileName==""){
				            		$rootScope.buttonClicked = "Please provide Folder Mapping Details";
									$rootScope.showModal = !$rootScope.showModal;
									  $rootScope.contentColor = "#dd4b39";
				            	}
				             else{
				             $http.post(url,dataObj,
										{
											headers : {
												'Accept' : 'application/json',
												'Content-Type' : 'application/json'
											}
										})
			                .success(function (response) {  
			                
									console.log(response)
									 $rootScope.buttonClicked = response;
									$rootScope.showModal = !$rootScope.showModal;
									  $rootScope.contentColor = "#78b266";
								
			                   
			                });
			              }
				            
				            	
				            	$scope.folderMapping=[];
				            	 $scope.folderName = "";
					             $scope.fileName = "";
			            	
			            }
				} ]);

angular
.module('cdrApp')
.controller(
		'loginController',
		[
				'$scope',
				'$state',
				'$rootScope',
				'$window',
				'$q',
				'$http'
				,'appConstants','AuthenticationService','userService',
				function($scope, $state, $rootScope, $window, $q,
						$http,appConstants,AuthenticationService,userService) {
							$rootScope.isProfilePage = false;
							 $scope.dataLoading = false;						
							$rootScope.currentUser = {
								roleType : '',
								email : '',
								userId : '',
								userName : '',
								userToken : ''
							};
							$scope.applications =[];
							/*$scope.roles =[];*/
							
							
							$scope.userLogin = function() {
								 $scope.dataLoading = true;
								var username = $scope.username;
								var password = $scope.password;

								if ((username != '' && username != null && username != undefined)
										&& (password != '' && password != null && password != undefined)) {
									 AuthenticationService.Login($scope.username, $scope.password, function(response) {
										    console.log(response.message)
										 if(response.message!='failure' && response.data.authStatus){	
											
							            	 $http.get(appConstants.serverUrl+'/login/getUserDetails/'+$window.sessionStorage.getItem('userToken'))
									         .success(function (response) {   
									        	 console.log('login response' , response)
									        
									        		var currentUser = {
															userId : response.data.userId,
															email : response.data.email,
															userName : response.data.userName,
															roleType : response.data.roleType,
															userToken : response.data.userToken
														};									        	
									        	 AuthenticationService.SetCredentials(response);
									        	 $window.sessionStorage.setItem('currentUser', JSON.stringify(currentUser));				        	
									        	 $rootScope.currentUser=userService.getCurrentUser();				        	
										            $rootScope.isProfilePage=true; 
										            $rootScope.buttonClicked ="welcome! "+$rootScope.currentUser.userName;
										            $rootScope.showModal = !$rootScope.showModal;
										            $rootScope.contentColor = "#78b266";
										           						           	
										            $state.go("home", {}, {reload: true}); 
										           
										        
									         });          	
							            	 
							             } else {  							            	
							            	
							                    $scope.dataLoading = false;
							                    $rootScope.buttonClicked ="Invalid Credentials";
								            	$rootScope.showModal = !$rootScope.showModal;
								            	  $rootScope.contentColor = "#dd4b39";
							            								
							             }
							         });
					
								}else{
							
									 $scope.dataLoading = false;
										
										$rootScope.buttonClicked ="Enter Credentials";
										$rootScope.showModal = !$rootScope.showModal;
										$rootScope.contentColor = "#dd4b39";
									
								}
								;

							};	
							$scope.init=function(){
	                          var url =  appConstants.serverUrl+"/login/getUserAuthDetails/"+$window.sessionStorage.getItem('userToken');
								
								$http.get(url).then(function(response) {
									
												$scope.applications=response.data.applications;	
												$scope.roles=response.data.roles;	
											console.log(' userDetails' , response)
											console.log(' applications' , $scope.applications)
											console.log(' roles' , $scope.roles)
											
										});
							}
						
						} ]);

angular.module('cdrApp').directive('modal', ['$timeout', function ($timeout) {
    return {
      template: '<div class="modal fade">' + 
          '<div class="modal-dialog" style="width: 750px; margin: auto;">' + 
            '<div class="modal-content" style="background-color:{{contentColor}}">' + 
              '<div class="modal-header">' + 
                '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>' + 
                '<h4 class="modal-title" style="color: #FFFFFF;">{{ buttonClicked }}!!</h4>' + 
              '</div>' + 
              '</div>' + 
          '</div>' + 
        '</div>',
      restrict: 'E',
      transclude: true,
      replace:true,
      scope:true,
      link: function postLink(scope, element, attrs) {
    	  $timeout(function() { // Timeout 
    		  $(element).modal('hide');
    		  }, 7000);
          scope.$watch(attrs.visible, function(value){
          if(value == true)
            $(element).modal('show');
          else       	 
        		  $(element).modal('hide');
          
         
        });

       
    		  
        $(element).on('shown.bs.modal', function(){
          scope.$apply(function(){
            scope.$parent[attrs.visible] = true;
          });
        });

        $(element).on('hidden.bs.modal', function(){
          scope.$apply(function(){
            scope.$parent[attrs.visible] = false;
          });
        });
      }
    };
}]);

angular.module('cdrApp').controller(
		'uploadController',
		[
				'$scope',
				'$rootScope',
				'$http',
				'$window',
				'$state',
				'appConstants','userService','globalServices','AuthenticationService',
				function($scope, $rootScope, $http, $window, $state,
						appConstants,userService,globalServices,AuthenticationService) {

					$rootScope.isProfilePage = false;
					$rootScope.currentUser = {
						roleType : '',
						email : '',
						userId : '',
						userName : '',
						userToken : ''
					};

					$scope.appId = 0;
					$scope.fileList = [];
					$scope.filePath = [];
					$scope.serverFolders = [];
					$scope.serverFoldersResult = [];
					$scope.sessionAppId=$window.sessionStorage.getItem('appId');
					$scope.dataLoading = false;
					$scope.duplicateUpload=false;
					$scope.inituser = function() {
						var data = globalServices.isUserTokenAvailable();
						if (data == null || data == undefined) {
							$rootScope.isProfilePage = false;
							$state.go("login");
						} else {
							$rootScope.currentUser = userService.getCurrentUser();
							if ($rootScope.currentUser != undefined
									|| $rootScope.currentUser != null) {
								$rootScope.isProfilePage = true;
							} else {
								$rootScope.isProfilePage = false;
								$state.go("login");
							}

						}
					}
					$scope.logout = function () {		
						   AuthenticationService.ClearCredentials();  
						   $rootScope.isProfilePage=false;
						 
					   }

					$scope.setFile = function(file, index) {
						$scope.duplicateUpload=false;
						if ($scope.fileList!=undefined && $scope.fileList.length>0) {	
							
						 for (var i = 1; i < $scope.fileList.length; i++) {
							if ($scope.fileList[i].name == file[0].name) {	  
															
								 $scope.duplicateUpload=true;
								 $rootScope.$apply(function() {	
								 $rootScope.buttonClicked ="Remove the duplicate file already selected with the name: "+$scope.fileList[i].name +  "";
								 $rootScope.showModal = !$rootScope.showModal;
						         $rootScope.contentColor = "#dd4b39";									
								});								   
								}
						 }
						}
						$scope.fileList[index] = file[0];
						

					}

					$scope.doUploadFile = function() {
						$scope.dataLoading = true;
						
						var selectedMonth = document.getElementById("startDate").value;
						
						
						var data = new FormData();

						for (var i = 0; i < $scope.fileList.length; i++) {

							data.append("uploadFile[" + i + "]",
									$scope.fileList[i]);

						}
						if( $window.sessionStorage.getItem('appId')!=undefined)
						data.append("applicationId", $window.sessionStorage.getItem('appId'));
						data.append("userName", $rootScope.currentUser.userName);
						data.append("selectedMonth",selectedMonth)
						console.log('selectedMonth' , selectedMonth)
					console.log('data for upload' , data)
						if (data != undefined && !$scope.duplicateUpload)
							$scope.UploadFileIndividual(data);
						else if($scope.duplicateUpload){
							 $scope.dataLoading = false;
							 $rootScope.buttonClicked ="Remove the duplicate files already selected";
					         $rootScope.showModal = !$rootScope.showModal;
					         $rootScope.contentColor = "#dd4b39";	
							
						}
					};
					$scope.UploadFileIndividual = function(data){


						var config = {
							transformRequest : angular.identity,
							transformResponse : angular.identity,
							headers : {
								'Content-Type' : undefined
							}
						}

						$http.post( appConstants.serverUrl+'/api/uploadfile', data, config).then(
								function(response) {
									$scope.dataLoading = false;
									if(response.data!="Files Uploaded Successfully"){
										  $rootScope.contentColor = "#dd4b39";
										  $rootScope.buttonClicked =response.data;
								          $rootScope.showModal = !$rootScope.showModal;	
									}
									else{							
										  $state.go("app", {appId:$window.sessionStorage.getItem('appId')}, {reload: true}); 
										  $rootScope.contentColor = "#78b266";
										  $rootScope.buttonClicked =response.data;
								          $rootScope.showModal = !$rootScope.showModal;	
									}
									
									
	  						           	
							           
								}, function(response) {
									 $rootScope.buttonClicked =response.data;
							         $rootScope.showModal = !$rootScope.showModal;
							         $rootScope.contentColor = "#dd4b39";
								});

					}

					$scope.appServerFolder = function() {
						
						$scope.sessionApp=$window.sessionStorage.getItem('appId');
						//$scope.appId = $rootScope.selectedAppId;
						console.log('upload screen sessionApp app id' , $window.sessionStorage.getItem('appId'))
						console.log('upload screen sessionApp' , $scope.sessionApp)
						
if ($scope.sessionApp != undefined
									|| $scope.sessionApp != null) {
						var url =  appConstants.serverUrl+"/api/serverfolders/" + $scope.sessionApp;
						console.log('app server folder url:' , url)
						var data = new FormData();

						$http.get(url).then(function(response) {
							$scope.serverFoldersResult = response.data;
							console.log($scope.serverFoldersResult)
						}, function(response) {

							$scope.serverFoldersResult = response.data;
							 $("#upload").show();
						});
}
					};
					$scope.init = function() {
						$scope.inituser();

						var url =  appConstants.serverUrl+"/api/applications/"

						var data = new FormData();

						$http.get(url).then(function(response) {
							$scope.applications = response.data;
							
						}, function(response) {

							$scope.applications = response.data;
							
						});
					};

					$scope.showAlert = function(event) {
						alert(event.target.id);
					}
					$scope.init();

				} ]);


angular.module('cdrApp').directive('multiselectDropdown', [function() {
    return function(scope, element, attributes) {
            
        // Below setup the dropdown:
        
        element.multiselect({
            buttonClass : 'btn btn-small',
            buttonWidth : '200px',
            buttonContainer : '<div class="btn-group" />',
            maxHeight : 200,
            enableFiltering : true,
            enableCaseInsensitiveFiltering: true,
            buttonText : function(options) {
                if (options.length == 0) {
                    return element.data()['placeholder'] + ' <b class="caret"></b>';
                } else if (options.length > 1) {
                    return _.first(options).text 
                    + ' + ' + (options.length - 1)
                    + ' more selected <b class="caret"></b>';
                } else {
                    return _.first(options).text
                    + ' <b class="caret"></b>';
                }
            },
            // Replicate the native functionality on the elements so
            // that angular can handle the changes for us.
            onChange: function (optionElement, checked) {
                optionElement.removeAttr('selected');
                if (checked) {
                    optionElement.prop('selected', 'selected');
                }
                element.change();
            }
            
        });
        // Watch for any changes to the length of our select element
        scope.$watch(function () {
            return element[0].length;
        }, function () {
            element.multiselect('rebuild');
        });
        
        // Watch for any changes from outside the directive and refresh
        scope.$watch(attributes.ngModel, function () {
            element.multiselect('refresh');
        });
        
        // Below maybe some additional setup
    }
}]);

'use strict';
angular.module('cdrApp').run([
			'$state','$http','$rootScope','globalServices','userService',function ( $state,$http,$rootScope,globalServices,userService) {
				
	   $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams) {	
	    var requireLogin = toState.data.requireLogin;
	    $rootScope.currentUser=userService.getCurrentUser();	
			
	    if ((requireLogin && typeof $rootScope.currentUser === 'undefined')|| (typeof globalServices.isUserTokenAvailable()=== 'undefined'))
		 {	 
	    	$rootScope.isProfilePage=false;
	        return $state.go('login');	        
	    }else{	    	
	    	$rootScope.isProfilePage=true;
	    }	  
	    
	  });
	   

	}]);

angular.module('cdrApp').run([
                  			'$state','$http','$rootScope','$timeout','$document','AuthenticationService',function ( $state,$http,$rootScope,$timeout,$document,AuthenticationService) {
                				
    console.log('starting run');

    // Timeout timer value 8 minutes automatically logout
    var TimeOutTimerValue = 480000;

    // Start a timeout
    var TimeOut_Thread = $timeout(function(){ LogoutByTimer() } , TimeOutTimerValue);
    var bodyElement = angular.element($document);

    angular.forEach(['keydown', 'keyup', 'click', 'mousemove', 'DOMMouseScroll', 'mousewheel', 'mousedown', 'touchstart', 'touchmove', 'scroll', 'focus'], 
    function(EventName) {
         bodyElement.bind(EventName, function (e) { TimeOut_Resetter(e) });  
    });

    function LogoutByTimer(){
        console.log('Logout');
        ///////////////////////////////////////////////////
        /// redirect to another page(eg. Login.html) here
        ///////////////////////////////////////////////////
       // Timeout timer value 8 minutes automatically logout
        AuthenticationService.ClearCredentials();  
   
    }

    function TimeOut_Resetter(e){
    
        /// Stop the pending timeout
        $timeout.cancel(TimeOut_Thread);

        /// Reset the timeout
        TimeOut_Thread = $timeout(function(){ LogoutByTimer() } , TimeOutTimerValue);
    }

}]);

/*'use strict';
angular.module('cdrApp').run([
			'$state','$http','$rootScope','globalServices','userService',function ( $state,$http,$rootScope,globalServices,userService) {
				
	   $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams) {	
	    var requireLogin = toState.data.requireLogin;
	    $rootScope.currentUser=userService.getCurrentUser();	
			
	    if ((requireLogin && typeof $rootScope.currentUser === 'undefined')|| (typeof globalServices.isUserTokenAvailable()=== 'undefined'))
		 {	 
	    	$rootScope.isProfilePage=false;
	        return $state.go('login');	        
	    }else{	    	
	    	$rootScope.isProfilePage=true;
	    }	  
	    
	  });
	   

	}]);*/
