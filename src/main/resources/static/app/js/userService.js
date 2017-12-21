angular.module('cdrApp').service('userService', ['$window', '$q','$http','appConstants', function ($window, $q,$http,appConstants) {
 
    this.getCurrentUser = function () {
    	
        if ($window.sessionStorage.getItem('currentUser')) {        	
            return JSON.parse($window.sessionStorage.getItem('currentUser'));
        }
    };
    this.clearCurrentUser = function () {
        $window.sessionStorage.removeItem('currentUser');
    };
}]);