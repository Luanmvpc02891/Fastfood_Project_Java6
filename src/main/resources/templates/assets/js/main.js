var app = angular.module('myApp', []);
		app.controller('myCtrl', function($scope) {
		  $scope.message = 'Hello, AngularJS!';
          alert("Đây là thông báo đơn giản!");
		});