angular.module('cdrApp').service('userService', ['$window', '$q','$http','appConstants', function ($window, $q,$http,appConstants) {
    this.setCurrentUser = function (user) {
    	$http.get(appConstants.serverUrl+'/login/getUserDetails'+ $window.sessionStorage["userToken"])
         .success(function (response) {           	
        	
        	 var currentUser = {
						userId : response.data.userId,
						email : response.data.email,
						userName : response.data.userName,
						roleType : response.data.roleType,
						userToken : response.data.userToken
					};
        	
        	 $window.sessionStorage.setItem('currentUser', JSON.stringify(currentUser));        	
        	
         });          	
       
    },
    this.getCurrentUser = function () {
    	
        if ($window.sessionStorage.getItem('currentUser')) {        	
            return JSON.parse($window.sessionStorage.getItem('currentUser'));
        }
    };
    this.clearCurrentUser = function () {
        $window.sessionStorage.removeItem('currentUser');
    };
}]);