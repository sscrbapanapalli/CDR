 
 var config=angular.module("appConfigApp", [])
.constant("appConstants",{
	//serverUrl : "http://10.13.44.80:8080/cdr",
	serverUrl : "http://localhost:8080",

}).run(function($http, $window) {	
	$http.defaults.headers.common['userToken'] =$window.sessionStorage["userToken"]?$window.sessionStorage["userToken"]: null;
});
 
 angular.module('appConfigApp') 
 .config(function ( $httpProvider) {        

     delete $httpProvider.defaults.headers.common['X-Requested-With'];
 });