app = angular.module("admin-app",["ngRoute"]);

app.config(function($routeProvider){
    $routeProvider
    .when("/item",{
        templateUrl: "/admin/item.html",
        controller: "/js/item-app"
    })
}
)