var commonServices = angular.module("commonServiceApp", ['appConfigApp']);

angular.module('commonServiceApp') 
.config(function ( $httpProvider) {        
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
});
commonServices.service("globalServices", ['$q', '$http', '$window', function ($q, $http, $window) {
	return {		
		isUserTokenAvailable : function () {
				return $window.sessionStorage["userToken"];
		}
	}


}]);

