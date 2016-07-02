/**
 * creation date 24.06.2016
 *
 * @author A.Hrankina
 */
(function () {
    var app = angular.module("utils", ["ui.bootstrap"]);

    app.directive('modal', function () {
        return {
            template: '<div class="modal fade">' +
            '<div class="modal-dialog">' +
            '<div class="modal-content">' +
            '<div class="modal-header">' +
            '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>' +
            '<h4 class="modal-title">{{ title }}</h4>' +
            '</div>' +
            '<div class="modal-body" ng-transclude></div>' +
            '</div>' +
            '</div>' +
            '</div>',
            restrict: 'E',
            transclude: true,
            replace: true,
            scope: true,
            link: function postLink(scope, element, attrs) {
                scope.title = attrs.title;

                scope.$watch(attrs.visible, function (value) {
                    if (value == true)
                        $(element).modal('show');
                    else
                        $(element).modal('hide');
                });

                $(element).on('shown.bs.modal', function () {
                    scope.$apply(function () {
                        scope.$parent[attrs.visible] = true;
                    });
                });

                $(element).on('hidden.bs.modal', function () {
                    scope.$apply(function () {
                        scope.$parent[attrs.visible] = false;
                    });
                });
            }
        }
    });

    app.directive('notification', function($timeout){
        return {
            restrict: 'E',
            replace: true,
            scope: {
                ngModel: '='
            },
            template: '<div class="alert fade" bs-alert="ngModel"></div>',
            link: function(scope, element, attrs){
                $timeout(function(){
                    element.remove();
                }, 5000);
            }
        }
    });

    app.controller("utilsCtrl", ["$scope", "$timeout", function (scope, timeout) {
        scope.alerts = [];

        scope.addSelfCloseAlert = function (aMsg, aType) {
            var alert = aType ? {type: aType, msg: aMsg} : {msg: aMsg};
            scope.alerts.push(alert);
            timeout(function() {
                scope.alerts.splice(scope.alerts.indexOf(alert), 1);
            }, 3000);
        };

        scope.addAlert = function (aMsg, aType) {
            var alert = aType ? {type: aType, msg: aMsg} : {msg: aMsg};
            scope.alerts.push(alert);
        };

        scope.closeAlert = function (index) {
            scope.alerts.splice(index, 1);
        };
    }]);

    app.directive('stringToNumber', function() {
        return {
            require: 'ngModel',
            link: function(scope, element, attrs, ngModel) {
                ngModel.$parsers.push(function(value) {
                    return '' + value;
                });
                ngModel.$formatters.push(function(value) {
                    return parseFloat(value, 10);
                });
            }
        };
    });
})();