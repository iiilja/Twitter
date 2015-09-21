var services = angular.module('tweetApp.services', ['ngResource']);

services.factory('Twitter', ['$resource',
    function ($resource) {
        return $resource('',{},{
                get_tweets: {method: 'GET', url: apiEndpoint + 'lastTweets/'},
                get_settings: {method: 'GET', url: apiEndpoint + 'getSettings/'},
                save_settings: {method: 'POST', url: apiEndpoint + 'setSettings/', params: { data : '@data'}}
            });}]);

services.factory("sysMessage", ['toaster','$filter', function (toaster, $filter) {
    return {
        ok: function (message) {
            toaster.pop('success', 'Success', message);
        },
        error: function (message) {
            toaster.pop('error', 'Error', message);
        }
    }
}]);

services.factory("sysLocation", ['$location', function ($location) {
    return {
        goHome: function () {
            $location.path('/');
        },
        goList: function () {
            $location.path('/list/');
        },
        goLink: function (link) {
            $location.path(link);
        }
    }
}]);

services.factory('browser', ['$window', function($window) {

     return {

        detectBrowser: function() {

            var userAgent = $window.navigator.userAgent;

            var browsers = {
                chrome: /chrome/i, 
                safari: /safari/i, 
                firefox: /firefox/i, 
                ie: /internet explorer/i, 
                opera: /opera/i
            };

            for(var key in browsers) {
                if (browsers[key].test(userAgent)) {
                    return key;
                }
            }

           return 'unknown';
       }
    }
}]);

services.factory('facade', function( sysMessage, Twitter, $location, $filter, $modal) {

     return {


        getSysMessage: function() {
           return sysMessage;
        },
        getWeather: function(){
            return Twitter;
        },cation: function() {
            return $location;
        },
        getFilter: function() {
            return $filter;
        },
        getModal: function() {
            return $modal;
        }

    }
});