/* @author Basker Ammu, Ramesh Kumar B
 * Url Routing configuration
 * $stateProvider,$urlRouterProvider
 */
'use strict';
angular.module("cdrApp", [ "ui.router", 'toastr','appConfigApp' ]);

'use strict';
angular.module('cdrApp').config(
		[ '$stateProvider', '$urlRouterProvider',
				function($stateProvider, $urlRouterProvider) {
					var roles = {
						ROLE_ADMIN : 0,
						ROLE_USER : 1
					};
					$urlRouterProvider.otherwise("/login");
					$stateProvider.state("login", {
						url : '/login',
						templateUrl : 'view/login.html',
						controller : "loginController",
						controllerAs : "loginController"

					}).state("upload", {
						url : '/upload',
						templateUrl : 'view/upload.html',
						controller : "uploadController",
						controllerAs : "uploadController",

					}).state("home", {
						url : '/home',
						templateUrl : 'view/home.html',
						controller : "homeController",
						controllerAs : "homeController",

					}).state("settings", {
						url : '/settings',
						templateUrl : 'view/settings.html',
						controller : "settingsController",
						controllerAs : "settingsController",

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
						'NotificationFactory','appConstants',
						function($scope, $state, $rootScope, $window, $q,
								$http, NotificationFactory,appConstants) {
							$scope.homepageContent = "landing dashboard page";
							//$scope.batchFiles={fileName:'', filePath:''};
							$scope.batchFileslist=[];
							$scope.checkStatus=false;
							//$rootscope.selectedAppId=0;
							$scope.getCurrentUser = function() {

								if ($window.sessionStorage.getItem('currentUser')) {
									return JSON.parse($window.sessionStorage
											.getItem('currentUser'));
								}
							};
							$scope.isUserTokenAvailable = function() {
								return $window.sessionStorage.getItem('userToken');
							}
							$scope.inituser = function() {
								var data = $scope.isUserTokenAvailable();
								if (data == null || data == undefined) {
									$rootScope.isProfilePage = false;
									$state.go("login");
								} else {
									$rootScope.currentUser = $scope.getCurrentUser();
									if ($rootScope.currentUser != undefined
											|| $rootScope.currentUser != null) {
										$rootScope.isProfilePage = true;
									} else {
										$rootScope.isProfilePage = false;
										$state.go("login");
									}

								}
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

							$scope.logout = function() {
								var deferred = $q.defer();
								var logoutUrl = appConstants.serverUrl+"/login/logOut/"
										+ $window.sessionStorage["userToken"];
								$http(
										{
											method : "POST",
											url : logoutUrl,
											headers : {
												"userToken" : $window.sessionStorage["userToken"]
											}
										})
										.then(
												function(result) {
													$window.sessionStorage["currentUser"] = null;
													$http.defaults.headers.common['userToken'] = null;
													$rootScope.currentUser = {
														roleType : '',
														email : '',
														userId : '',
														userName : '',
														userToken : ''
													};
													$state.go("login");
													deferred.resolve(result);
												}, function(error) {
													deferred.reject(error);
												});

								return deferred.promise;
							}
							// To get application list
							
							$scope.init = function() {
								$scope.inituser();
								$scope.userId=$rootScope.currentUser.userId;
								$scope.user={};
								//$rootScope.selectedAppId='';
								console.log($scope.userId)
								var data = new FormData();
								/*var url = "/api/applications/"

								$http.get(url).then(function(response) {
									$scope.applications = response.data;
									
								}, function(response) {

									$scope.applications = response.data;
									
									
								});*/
								
								var url =  appConstants.serverUrl+"/login/getUserDetails1/" + $window.sessionStorage["userToken"];
								
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
								$scope.appId = appId;
								$rootScope.selectedAppId=appId;
								console.log('user selected app id' , $rootScope.selectedAppId)

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
							};
							
							$scope.doUpload = function() {
								$rootScope.isProfilePage = true;
								
								$state.go("upload");
								$scope.checkStatus=false;
								
							}

						} ]);

angular.module('cdrApp').controller(
		'settingsController',
		[
				'$scope',
				'$state',
				'$rootScope',
				'$window',
				'$q',
				'$http',
				'NotificationFactory','appConstants',
				function($scope, $state, $rootScope, $window, $q, $http,
						NotificationFactory,appConstants) {
					$scope.homepageContent = "settings dashboard page";
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
						'NotificationFactory','appConstants',
						function($scope, $state, $rootScope, $window, $q,
								$http, NotificationFactory,appConstants) {
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

									$http
											.post(
													 appConstants.serverUrl+"/login/loginUser",
													{
														userName : username,
														password : password
													},
													{
														headers : {
															'Accept' : 'application/json',
															'Content-Type' : 'application/json'
														}
													})
											.then(
													function(response) {
														//console.log(response)
														if (!response.data) {
															NotificationFactory.error("Invalid Credentials");
															$state.go("login");
															 // NotificationFactory.clear();
											            	  NotificationFactory.error("Invalid Credentials");
											            	  NotificationFactory.success("Invalid Credentials");
														} else {

															/*if (response.data.data.userToken!=null && response.data.data.userToken!=undefined) {*/
															if (response.data.message!="failure") {
																$http.defaults.headers.common['userToken'] = response.data.data.userToken ? response.data.data.userToken
																		: null;
																$window.sessionStorage["userToken"] = response.data.data.userToken;
																var data = $scope
																		.isUserTokenAvailable();
																if (data == null
																		|| data == undefined) {
																	$rootScope.isProfilePage = false;
																	$state
																			.go("login");
																	 NotificationFactory.error("Invalid Credentials");
																} else {
																	$scope
																			.setCurrentUser();
																	$rootScope.currentUser = $scope
																			.getCurrentUser();
															if ($rootScope.currentUser != undefined
																			|| $rootScope.currentUser != null) {
																		$rootScope.isProfilePage = true;
																		
																		$state
																				.go("home");
																		 NotificationFactory.clear();
														            	  NotificationFactory.success("welcome! "+$rootScope.currentUser.userName);
																	}
																}

															} else {
																$state.go("login");
																 NotificationFactory.clear();
																 //NotificationFactory.error("Invalid Credentials");
																 $scope.uploadResult='Invalid Credentials';
																 console.log( $scope.uploadResult)
															}

														}
													},
													function(errResponse) {
														return 'Error while get All';
													})
								}
								;

							};

							$scope.setCurrentUser = function() {
								$http
										.get(
												 appConstants.serverUrl+'/login/getUserDetails/'
														+ $window.sessionStorage["userToken"])
										.success(
												function(response) {
													if (response.data) {
														var currentUser = {
															userId : response.data.userId,
															email : response.data.email,
															userName : response.data.userName,
															roleType : response.data.roleType,
															userToken : response.data.userToken
														};

														$rootScope.currentUser = {
															roleType : currentUser.roleType,
															email : currentUser.email,
															userId : currentUser.userId,
															userName : currentUser.userName,
															userToken : currentUser.userToken
														};
														$window.sessionStorage
																.setItem(
																		'currentUser',
																		JSON
																				.stringify(currentUser));
														$rootScope.currentUser = currentUser;
														if (!$rootScope.$$phase)
															$rootScope.$apply();
													}

												});

							}
							$scope.getCurrentUser = function() {

								if ($window.sessionStorage
										.getItem('currentUser')) {
									return JSON.parse($window.sessionStorage
											.getItem('currentUser'));
								}
							};

							$scope.isUserTokenAvailable = function() {
								return $window.sessionStorage
										.getItem('userToken');
							}

						} ]);

angular.module('cdrApp').controller(
		'uploadController',
		[
				'$scope',
				'$rootScope',
				'$http',
				'$window',
				'$state',
				'NotificationFactory','appConstants',
				function($scope, $rootScope, $http, $window, $state,
						NotificationFactory,appConstants) {

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

					$scope.getCurrentUser = function() {

						if ($window.sessionStorage.getItem('currentUser')) {
							return JSON.parse($window.sessionStorage
									.getItem('currentUser'));
						}
					};
					$scope.isUserTokenAvailable = function() {
						return $window.sessionStorage.getItem('userToken');
					}
					$scope.inituser = function() {
						var data = $scope.isUserTokenAvailable();
						if (data == null || data == undefined) {
							$rootScope.isProfilePage = false;
							$state.go("login");
						} else {
							$rootScope.currentUser = $scope.getCurrentUser();
							if ($rootScope.currentUser != undefined
									|| $rootScope.currentUser != null) {
								$rootScope.isProfilePage = true;
							} else {
								$rootScope.isProfilePage = false;
								$state.go("login");
							}

						}
					}

					$scope.setFile = function(file, index) {
						

						$scope.fileList[index] = file[0];

					}

					$scope.doUploadFile = function() {
						var data = new FormData();

						for (var i = 0; i < $scope.fileList.length; i++) {

							data.append("uploadFile[" + i + "]",
									$scope.fileList[i]);

						}
						data.append("applicationId",$rootScope.selectedAppId);
						data.append("userName", $rootScope.currentUser.userName);
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

						var url =  appConstants.serverUrl+"/api/serverfolders/" + $rootScope.selectedAppId;

						var data = new FormData();

						$http.get(url).then(function(response) {
							$scope.serverFoldersResult = response.data;
							console.log($scope.serverFoldersResult)
						}, function(response) {

							$scope.serverFoldersResult = response.data;
							 $("#upload").show();
						});
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
