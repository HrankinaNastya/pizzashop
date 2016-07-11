/**
 * creation date 03.07.2016
 *
 * @author A.Hrankina
 */
(function () {
    //TODO: loader
    var app = angular.module("pizzas", ["ngSanitize", "ui.bootstrap", "ui.grid", "ui.grid.selection", "ui.select", "ui.grid.autoResize"]);

    app.controller("PizzasCtrl", function ($scope, $http, $modal, $filter) {
        var store = this;

        store.isRowSelected = false;
        store.maxSize = 5;
        store.totalPages = 1;
        store.size = 22;
        store.totalElements = 0;
        store.currentPage = 1;
        store.sort = null;
        store.isLast = false;
        store.elementsOnPage = 0;

        $scope.items = [];

        $scope.sortChanged = function (grid, sortColumns) {
            if (sortColumns.length > 0) {
                store.sort = {name: sortColumns[0].name, dir: sortColumns[0].sort.direction}
            } else {
                store.sort = null;
            }
            store.currentPage = 1;
            store.refresh();
        };

        $scope.gridOptions = {
            data: "items",
            enableRowSelection: true,
            enableRowHeaderSelection: false,
            multiSelect: false,
            columnDefs: [
                {field: "id", displayName: 'ИД', visible: false, maxWidth: 140},
                {field: "name", displayName: "Название", maxWidth: 300},
                {
                    field: "preview",
                    displayName: "Картинка",
                    enableSorting: false,
                    cellTemplate: '<div style="text-align: center"><img ng-src="{{row.entity.preview}}" width = 200 height = 200 alt="Description"/></div>',
                    maxWidth: 200
                },
                {field: "price", displayName: "Стоимость, грн", maxWidth: 50},
                {field: "description", displayName: "Описание"}
            ],
            modifierKeysToMultiSelect: false,
            useExternalSorting: true,
            enableGridMenu: true
        };

        $scope.gridOptions.onRegisterApi = function (gridApi) {
            gridApi.core.on.sortChanged($scope, $scope.sortChanged);

            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                store.isRowSelected = row.isSelected;
            });
            store.selection = gridApi.selection;
        };

        store.filters = {};
        store.aFilters = {};
        store.clearFilters = function () {
            store.filters = {};
            store.aFilters = {};
            store.currentPage = 1; //TODO: refresh pages only then filters have been setted before
            store.refresh();
        };

        store.doFilters = function () {
            store.currentPage = 1; //TODO: refresh pages only then filters have been setted before
            store.refresh();
        };

        store.loadList = function (page, limit) {
            store.isRowSelected = false;

            var sort = store.sort ? "&order=" + store.sort.name + "&dir=" + store.sort.dir : "";
            var params = "?page=" + (page - 1) + "&limit=" + limit + sort;
            $http.get("/api/pizzas" + params)
                .success(function (data) {
                    store.totalElements = data.totalElements;
                    store.currentPage = data.number + 1;
                    store.totalPages = data.totalPages;
                    store.size = data.size;

                    store.isLast = data.last;
                    store.elementsOnPage = data.numberOfElements;

                    $scope.items = data.content;
                })
                .error(function () {
                    //TODO: show error...
                    console.log("error");
                });
        };

        store.setPage = function (pageNo) {
            store.currentPage = pageNo;
        };
        store.pageChanged = function () {
            store.loadList(store.currentPage, store.size);
        };
        store.refresh = function () {
            store.loadList(store.currentPage, store.size);
        };

        store.refresh();

        store.reload = function () {
            store.refresh();
        };

        //edit form
        store.showCreate = function () {
            store.open();
        };

        store.showEdit = function () {
            store.open(store.selection.getSelectedRows()[0].id);
        };

        store.open = function (recordId) {
            $modal.open({
                animation: true,
                templateUrl: 'js/pizzas/pizzas-edit.html',
                controller: 'PizzasEditFormCtrl',
                size: "lg",
                resolve: {
                    recordId: function () {
                        return recordId;
                    },
                    sender: function () {
                        return store;
                    }
                }
            });
        };

        store.showDelete = function() {
            $modal.open({
                animation: true,
                templateUrl: 'js/common/delete-dialog.html',
                controller: 'PizzasDeleteConfirmCtrl',
                resolve: {
                    recordId: function () {
                        return store.selection.getSelectedRows()[0].id;
                    },
                    sender: function () {
                        return store;
                    }
                }
            });
        }
    });

    app.directive("pizzasList", function () {
        return {
            restrict: "E",
            templateUrl: "js/pizzas/pizzas-list.html"
        }
    });

    app.controller('PizzasDeleteConfirmCtrl', function ($http, $scope, $modalInstance, $timeout, recordId, sender) {
        var store = this;

        store.isLast = false;
        store.elementsOnPage = 0;

        $scope.loading = false;
        $scope.ok = function () {
            $scope.loading = true;
            $scope.alerts = [];
            $http.delete("/api/pizzas/" + recordId)
                .success(function () {
                    $modalInstance.close();

                    if (store.isLast && store.elementsOnPage === 1 && store.currentPage != 1) {
                        store.currentPage--;
                    }

                    sender.refresh();
                })
                .error(function (data) {
                    $scope.loading = false;
                    $scope.alerts.push({msg: "Ошибка удаления: " + data.msg});
                });
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.alerts = [];
        store.closeAlert = function (index) {
            $scope.alerts.splice(index, 1);
        };
    });

    app.controller('PizzasEditFormCtrl', function ($http, $scope, $modalInstance, $timeout, $filter, recordId, sender) {
        var store = this;

        $scope.formRecord = {};

        $scope.recordId = recordId;
        $scope.loading = false;
        store.loadRecord = function () {
            $scope.loading = true;
            $http.get("/api/pizzas/" + recordId)
                .success(function (data) {
                    $scope.formRecord = data;
                    $scope.loading = false;
                })
                .error(function () {
                    //TODO: error message... maybe global
                    $scope.loading = false;
                });
        };

        store.findInDictById = function (id, dictData) {
            for (var i = 0; i < dictData.length; i++) {
                if (dictData[i].id == id) {
                    return dictData[i];
                }
            }
            return null;
        };

        $scope.ok = function () {
            $scope.alerts = [];
            $scope.loading = true;

            $http.post("/api/pizzas", $scope.formRecord)
                .success(function () {
                    $modalInstance.close();
                    sender.refresh();
                })
                .error(function (data) {
                    $scope.loading = false;
                    store.addAlert("Ошибка сохранения: " + data.msg);
                });
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.alerts = [];
        store.addAlert = function (aMsg, aType) {
            var alert = aType ? {type: aType, msg: aMsg} : {msg: aMsg};
            $scope.alerts.push(alert);
        };

        $scope.closeAlert = function (index) {
            $scope.alerts.splice(index, 1);
        };

        if (recordId) {
            store.loadRecord();
        }

    });

})()
