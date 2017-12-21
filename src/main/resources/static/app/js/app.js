/* @author Basker Ammu, Ramesh Kumar B
 * Url Routing configuration
 * $stateProvider,$urlRouterProvider
 */
/* @author Basker Ammu, Ramesh Kumar B
 * Url Routing configuration
 * $stateProvider,$urlRouterProvider
 */
'use strict';
angular.module("cdrApp", [ "ui.router", 'toastr','appConfigApp' ,'commonServiceApp']);

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

					}).state("settings", {
						url : '/settings',
						templateUrl : 'view/settings.html',
						controller : "settingsController",
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
						'NotificationFactory','appConstants','userService','globalServices','AuthenticationService',
						function($scope, $state, $rootScope, $window, $q,
								$http, NotificationFactory,appConstants,userService,globalServices,AuthenticationService) {
							$scope.homepageContent = "landing dashboard page";
							//$scope.batchFiles={fileName:'', filePath:''};
							$scope.batchFileslist=[];
							$scope.checkStatus=false;
							$scope.welcomeMsg=false;
							$scope.userId=0;
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
							
							$scope.batchFiles=function(s){
								$scope.checkStatus=true;
								$scope.inituser();
								
								console.log('in select app.js')
								//alert(s);
								$scope.batchFileslist=$scope.batchDetailsResult[s].batchFileDetailList;
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
												$scope.roles=response.data.applications;	
											console.log(' userDetails' , response)
											console.log(' applications' , $scope.applications)
											console.log(' roles' , $scope.roles)
										});
							};
							
						
							$scope.batchHistoryDetails = function(appId) {
								$scope.inituser();
								$scope.welcomeMsg=true;
								$scope.appId = appId;
								$rootScope.selectedAppId=appId;
								if ($scope.appId  != undefined
										||$scope.appId  != null) {
								
								//console.log('user selected app id' , $rootScope.selectedAppId)

								var url= appConstants.serverUrl+"/api/batchHistoryDetails/" + $scope.appId;
								
								console.log(url)
								var data = new FormData();

								$http.get(url).then(function(response) {
									$scope.batchDetailsResult = response.data;
									console.log('full batch his details', $scope.batchDetailsResult);
									
								}, function(response) {

									$scope.batchDetailsResult = response.data;
									console.log($scope.batchDetailsResult)
									
								});
								}
							};
							
							$scope.doUpload = function() {
								$rootScope.isProfilePage = true;
								
								$state.go("upload");
								$scope.checkStatus=false;
								
							}

						} ]);

angular.module('cdrApp')
.factory('AuthenticationService',
	    [ '$http', '$state',"$window",'$rootScope','appConstants','globalServices','NotificationFactory',
	    function ($http, $state,$window,$rootScope,appConstants,globalServices,NotificationFactory) {
	    	
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
	                	        callback(response);	
							} 
						
	                   
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
								   NotificationFactory.success($rootScope.username+": Log out Successfully"); 	
								    $rootScope.isProfilePage=false;
								    $rootScope.currentUser={};	
								    $rootScope.username="";
								 $window.sessionStorage.removeItem('userToken');
								 $window.sessionStorage.removeItem('currentUser');								 
								  $http.defaults.headers.common['userToken']==null;
								 				 
								  $rootScope.isProfilePage=false;
								  $state.go("login");
								  		
							}else{
								  NotificationFactory.error($rootScope.username+": Log out Error"); 	
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
				'NotificationFactory','appConstants','userService','globalServices','AuthenticationService',
				function($scope, $state, $rootScope, $window, $q, $http,
						NotificationFactory,appConstants,userService,globalServices,AuthenticationService) {
					$scope.homepageContent = "settings dashboard page";
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
					$scope.inituser();
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
				'$http',
				'NotificationFactory','appConstants','AuthenticationService','userService',
				function($scope, $state, $rootScope, $window, $q,
						$http, NotificationFactory,appConstants,AuthenticationService,userService) {
							$rootScope.isProfilePage = false;
							$scope.uploadResult='';
							$rootScope.currentUser = {
								roleType : '',
								email : '',
								userId : '',
								userName : '',
								userToken : ''
							};
						
							$scope.userLogin = function() {

								var username = $scope.username;
								var password = $scope.password;

								if ((username != '' && username != null && username != undefined)
										&& (password != '' && password != null && password != undefined)) {
									 AuthenticationService.Login($scope.username, $scope.password, function(response) {
										    
										 if(response.message!='failure' && response.data.authStatus){	
							            	 $http.get(appConstants.serverUrl+'/login/getUserDetails/'+$window.sessionStorage.getItem('userToken'))
									         .success(function (response) {           	
									        
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
										            $state.go("home");
										            NotificationFactory.clear();
									            	NotificationFactory.success("welcome! "+$rootScope.currentUser.userName);
									         });          	
							            	 
							             } else {     
							            	 NotificationFactory.clear();
											 NotificationFactory.error("Invalid Credentials");
							             }
							         });
					
								}
								;

							};						

						} ]);

angular.module('cdrApp').controller(
		'uploadController',
		[
				'$scope',
				'$rootScope',
				'$http',
				'$window',
				'$state',
				'NotificationFactory','appConstants','userService','globalServices','AuthenticationService',
				function($scope, $rootScope, $http, $window, $state,
						NotificationFactory,appConstants,userService,globalServices,AuthenticationService) {

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
						

						$scope.fileList[index] = file[0];

					}

					$scope.doUploadFile = function() {
						
						var selectedMonth = document.getElementById("startDate").value;
						
						
						var data = new FormData();

						for (var i = 0; i < $scope.fileList.length; i++) {

							data.append("uploadFile[" + i + "]",
									$scope.fileList[i]);

						}
						data.append("applicationId",$rootScope.selectedAppId);
						data.append("userName", $rootScope.currentUser.userName);
						data.append("selectedMonth",selectedMonth)
					console.log('data for upload' , data)
						if (data != undefined)
							$scope.UploadFileIndividual(data);
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
								
									$scope.uploadResult = response.data;

								}, function(response) {
									
									$scope.uploadResult = response.data;
								});

					}

					$scope.appServerFolder = function() {
						
						
						//$scope.appId = $rootScope.selectedAppId;
						console.log("upload screen selected app id" , $scope.appId)
if ($rootScope.selectedAppId != undefined
									|| $rootScope.selectedAppId != null) {
						var url =  appConstants.serverUrl+"/api/serverfolders/" + $rootScope.selectedAppId;

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

'use strict';
angular.module('cdrApp').factory('NotificationFactory', function(toastr) {
	var logIt;

	toastr.options = {
		"closeButton" : true,
		"positionClass" : "toast-top-center",
		"timeOut" : "3000"
	};

	logIt = function(message, type) {
		return toastr[type](message);
	};

	return {
		success : function(message) {
			logIt(message, 'success');
		},
		error : function(message) {
			logIt(message, 'error');
		},
		clear : function() {

			toastr.clear();

		}
	};
});

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
