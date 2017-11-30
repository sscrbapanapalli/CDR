/* @author Basker Ammu
 * Url Routing configuration
 * $stateProvider,$urlRouterProvider
 */
'use strict';
angular.module("cdrApp", [ "ui.router", 'toastr' ]);

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
						templateUrl : '/login',
						controller : "loginController",
						controllerAs : "loginController"

					}).state("upload", {
						url : '/upload',
						templateUrl : '/upload',
						controller : "uploadController",
						controllerAs : "uploadController",

					}).state("home", {
						url : '/home',
						templateUrl : '/home',
						controller : "homeController",
						controllerAs : "homeController",

					}).state("settings", {
						url : '/settings',
						templateUrl : '/settings',
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
						'NotificationFactory',
						function($scope, $state, $rootScope, $window, $q,
								$http, NotificationFactory) {
							$scope.homepageContent = "landing dashboard page";

							$scope.logout = function() {
								var deferred = $q.defer();
								var logoutUrl = "/login/logOut/"
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
				'NotificationFactory',
				function($scope, $state, $rootScope, $window, $q, $http,
						NotificationFactory) {
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
						'NotificationFactory',
						function($scope, $state, $rootScope, $window, $q,
								$http, NotificationFactory) {
							$rootScope.isProfilePage = false;
							$rootScope.currentUser = {
								roleType : '',
								email : '',
								userId : '',
								userName : '',
								userToken : ''
							};
							

							$scope.logout = function() {
								AuthenticationService.ClearCredentials();
								$rootScope.isProfilePage = false;
							}
							$scope.userLogin = function() {

								var username = $scope.username;
								var password = $scope.password;

								if ((username != '' && username != null && username != undefined)
										&& (password != '' && password != null && password != undefined)) {

									$http
											.post(
													"/login/loginUser",
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
														if (!response.data) {

															$state.go("login");
															  NotificationFactory.clear();
											            	  NotificationFactory.error("Invalid Credentials");
														} else {

															if (response.data.data.userToken!=null && response.data.data.userToken!=undefined) {
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
																				.go("upload");
																		 NotificationFactory.clear();
														            	  NotificationFactory.success("welcome! "+$rootScope.currentUser.userName);
																	}
																}

															} else {
																$state
																		.go("login");
																 NotificationFactory.clear();
																 NotificationFactory.error("Invalid Credentials");
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
												'/login/getUserDetails/'
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
				'NotificationFactory',
				function($scope, $rootScope, $http, $window, $state,
						NotificationFactory) {

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
						data.append("applicationId", $scope.appId);
					
						if (data != undefined)
							$scope.UploadFileIndividual(data);
					};
					$scope.UploadFileIndividual = function(data)

					{
						var targetRequestPath = "/api/uploadfile";

						var config = {
							transformRequest : angular.identity,
							transformResponse : angular.identity,
							headers : {
								'Content-Type' : undefined
							}
						}

						$http.post(targetRequestPath, data, config).then(
								function(response) {
								
									$scope.uploadResult = response.data;

								}, function(response) {
									
									$scope.uploadResult = response.data;
								});

					}

					$scope.appServerFolder = function(appId) {
						$scope.appId = appId;

						var url = "/api/serverfolders/" + $scope.appId;

						var data = new FormData();

						$http.get(url).then(function(response) {
							$scope.serverFoldersResult = response.data;
							
						}, function(response) {

							$scope.serverFoldersResult = response.data;
						});
					};
					$scope.init = function() {
						$scope.inituser();

						var url = "/api/applications/"

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
