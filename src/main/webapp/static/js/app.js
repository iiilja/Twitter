var apiEndpoint = "http://localhost:8080/";

var app = angular.module('tweetApp', ['ngRoute', 'ngSanitize', 'ui.bootstrap', 'tweetApp.services', 'toaster', 'ui.router']);

app.config(['$routeProvider','$stateProvider','$urlRouterProvider', function ($routeProvider, $stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/');
    $stateProvider
        .state('list', {
            url: "/",
            views: {
                "topView": { controller: 'TopMenuController',templateUrl: './views/top_clean_menu.html' },
                "contentView": { controller: 'MainController',templateUrl: './views/list.html' },
                "leftMenuView": {controller: 'LeftMenuController', templateUrl: 'views/left_menu.html' }
            }
        })
        .state('settings', {
            url: "/",
            views: {
                "topView": { controller: 'TopMenuController',templateUrl: './views/top_clean_menu.html' },
                "contentView": { controller: 'SettingsController',templateUrl: './views/settings.html' },
                "leftMenuView": {controller: 'LeftMenuController', templateUrl: 'views/left_menu.html' }
            }
        });
}]);

app.factory('AuthInterceptor', ['$q' ,function ($q) {
    return {
        responseError: function (response) {
            if(response.status === 401){
                return $q.reject(response);
            } else {
                return $q.reject(response);
            }
        }
    };
}]);

app.config(function ($httpProvider) {
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
    $httpProvider.defaults.headers.put['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

    $httpProvider.interceptors.push([
        '$injector',
        function ($injector) {
            return $injector.get('AuthInterceptor');
        }
    ]);

});


app.controller('LeftMenuController', ['$scope', '$location', '$http', '$rootScope',
    function ($scope, $location, $http, $rootScope) {
    }]);


app.controller('TopMenuController', ['$scope', '$location', '$http', '$rootScope',
    function ($scope, $location, $http, $rootScope) {
        $rootScope.bodyClass = 'content_bg';
        $rootScope.left_menu_active = '';
        $scope.compName = "Small Ambitious company";
    }]);

app.controller('MainController', ['$scope', 'Twitter', 'sysMessage', '$rootScope',
    function ($scope, Twitter, sysMessage, $rootScope) {
        $rootScope.left_menu_active = 'twitter';
        $scope.currentCampaign = {};
        Twitter.get_tweets(function (response) {
            $scope.statuses = response.statuses;
            var parent = document.getElementById("statuses");
            for (i = 0; i < $scope.statuses.length; i++){
                var div = document.createElement("div");
                div.innerHTML = $scope.statuses[i].html;
                parent.appendChild(div);
            }
            $scope.welcomeMessage = response.welcomeMessage;
        });
        Twitter.get_settings(function (response) {
            $scope.settings = {};
            $scope.settings.welcomeTag = response.welcomeTag;
            $scope.settings.feedTag = response.feedTag;
        });
    }]);
app.controller('SettingsController', ['$scope', 'Twitter', 'sysMessage', '$rootScope',
    function ($scope, Twitter, sysMessage, $rootScope) {
        $rootScope.left_menu_active = 'settings';

        Twitter.get_settings(function (response) {
            $scope.settings = {};
            $scope.settings.welcomeTag = response.welcomeTag;
            $scope.settings.feedTag = response.feedTag;
        });

        $scope.save = function(){
            Twitter.save_settings({data:$scope.settings},function(response){
                if (response.result == "OK"){
                    sysMessage.ok("Saved");
                } else {
                    sysMessage.error(response.result);
                }
            });
        }
    }]);
