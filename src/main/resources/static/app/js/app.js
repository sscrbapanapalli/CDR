/* @author Basker Ammu, Ramesh Kumar B
 * Url Routing configuration
 * $stateProvider,$urlRouterProvider
 */

'use strict';
angular.module("cdrApp", [ "ui.router", 'toastr','appConfigApp' ,'commonServiceApp','angularUtils.directives.dirPagination']);

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
						'appConstants','userService','globalServices','AuthenticationService',
						function($scope, $state, $rootScope, $window, $q,
								$http,appConstants,userService,globalServices,AuthenticationService) {
							$scope.homepageContent = "landing dashboard page";
							//$scope.batchFiles={fileName:'', filePath:''};
							$scope.batchFileslist=[];
							$scope.checkStatus=false;
							$scope.welcomeMsg=false;
							$scope.userId=0;
							 $scope.currentPage = 1;
							  $scope.pageSize = 5;
							  $scope.currentPagetest = 1;
							  $scope.pageSizetest = 5;
							
							  
							  $scope.selectedId = undefined;
							  $scope.selectedIdChild = undefined;
							$scope.resetParent=function(){
								  $scope.search=""
									  if ( $window.sessionStorage.getItem('appId')  != undefined
												|| $window.sessionStorage.getItem('appId')  != null) {						
							            	
									  $scope.batchHistoryDetails( $window.sessionStorage.getItem('appId'));
									  }
							}
							$scope.resetChild=function(){
								  $scope.searchBatch=""
									  
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
							
							$scope.batchFiles=function(row){
								 $scope.selectedIdChild = undefined;
								$scope.selectedId = row;
								$scope.checkStatus=true;
								$scope.inituser();
								
								console.log('in select app.js')
								//alert(s);
								$scope.batchFileslist=$scope.batchDetailsResult[row].batchFileDetailList;
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
							
							$scope.selectRecordRow=function(row) {
								$scope.selectedIdChild = row;
							}
						
							$scope.batchHistoryDetails = function(appId) {
								  $scope.selectedId = undefined;
								  $scope.selectedIdChild = undefined;
								
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
								var url= appConstants.serverUrl+"/api/batchHistoryDetails/" + $window.sessionStorage.getItem('appId');
								
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
								}
							};
							
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
							$scope.doReverse = function(){
								console.log('in  do reverese')
								
								var config = {
										transformRequest : angular.identity,
										transformResponse : angular.identity,
										headers : {
											'Content-Type' : undefined
										}
									}
								if($rootScope.currentUser!=undefined){
								var url=appConstants.serverUrl+"/api/reverseUpload/";
								var data = new FormData();
								data.append("applicationId" , $scope.appId);
								data.append("userName", $rootScope.currentUser.userName);
								console.log(data)
								$http.post(url,data,config).then(
										function(response){
											
											$scope.reverseResult=response.data;
										},function(response){
											
											$scope.reverseResult=response.data;
										});
								console.log('reverse response :', $scope.reverseResult)
								}
							
							}

						} ]);

angular.module('cdrApp')
.factory('AuthenticationService',
	    [ '$http', '$state',"$window",'$rootScope','appConstants','globalServices','FlashService',
	    function ($http, $state,$window,$rootScope,appConstants,globalServices,FlashService) {
	    	
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
							
								    $rootScope.isProfilePage=false;
								    $rootScope.currentUser={};	
								    $rootScope.username="";
								 $window.sessionStorage.removeItem('userToken');
								 $window.sessionStorage.removeItem('currentUser');		
								 $window.sessionStorage.removeItem('appId');
								
								  $http.defaults.headers.common['userToken']==null;
								  $rootScope.buttonClicked = "LogOut Successfully";
									$rootScope.showModal = !$rootScope.showModal;
				 
								  $rootScope.isProfilePage=false;
								  $state.go("login");
																
							}else{
								  $rootScope.buttonClicked = "LogOut Error";
									$rootScope.showModal = !$rootScope.showModal;
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
				'$http'
				,'appConstants','AuthenticationService','userService','FlashService',
				function($scope, $state, $rootScope, $window, $q,
						$http,appConstants,AuthenticationService,userService,FlashService) {
							$rootScope.isProfilePage = false;
							 $scope.dataLoading = false;						
							$rootScope.currentUser = {
								roleType : '',
								email : '',
								userId : '',
								userName : '',
								userToken : ''
							};
							
							
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
										           						           	
										            $state.go("home", {}, {reload: true}); 
										           
										        
									         });          	
							            	 
							             } else {  							            	
							            	
							                    $scope.dataLoading = false;
							                    $rootScope.buttonClicked ="Invalid Credentials";
								            	$rootScope.showModal = !$rootScope.showModal;
							            								
							             }
							         });
					
								}else{
							
									 $scope.dataLoading = false;
										
										$rootScope.buttonClicked ="Enter Credentials";
										$rootScope.showModal = !$rootScope.showModal;
									
								}
								;

							};						

						} ]);

angular.module('cdrApp').directive('modal', ['$timeout', function ($timeout) {
    return {
      template: '<div class="modal fade">' + 
          '<div class="modal-dialog">' + 
            '<div class="modal-content">' + 
              '<div class="modal-header">' + 
                '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>' + 
                '<h4 class="modal-title">{{ buttonClicked }}!!</h4>' + 
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
				'appConstants','userService','globalServices','AuthenticationService','FlashService',
				function($scope, $rootScope, $http, $window, $state,
						appConstants,userService,globalServices,AuthenticationService,FlashService) {

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
									$scope.dataLoading = false;
									  $rootScope.buttonClicked =response.data;
							          $rootScope.showModal = !$rootScope.showModal;								
								}, function(response) {
									 $rootScope.buttonClicked =response.data;
							         $rootScope.showModal = !$rootScope.showModal;
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



'use strict';
angular
.module('cdrApp')
.factory('FlashService', FlashService);

FlashService.$inject = ['$rootScope','$timeout'];
function FlashService($rootScope,$timeout) {
	  var service = {};

      service.Success = Success;
      service.Error = Error;

      initService();

      return service;

      function initService() {
          $rootScope.$on('$locationChangeStart', function () {
              clearFlashMessage();
          });

          function clearFlashMessage() {
              var flash = $rootScope.flash;
              if (flash) {
                  if (!flash.keepAfterLocationChange) {
                      delete $rootScope.flash;
                  } else {
                      // only keep for a single location change
                      flash.keepAfterLocationChange = false;
                  }
              }
          }
      }

      function Success(message, keepAfterLocationChange) {
          $rootScope.flash = {
              message: message,
              type: 'success', 
              keepAfterLocationChange: keepAfterLocationChange
          };
          $timeout(function(){
        	  $rootScope.flash={};
          }, 3000);
      }

      function Error(message, keepAfterLocationChange) {
          $rootScope.flash = {
              message: message,
              type: 'error',
              keepAfterLocationChange: keepAfterLocationChange
          };
          $timeout(function(){
        	  $rootScope.flash={};
          }, 3000);
      }
}

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
