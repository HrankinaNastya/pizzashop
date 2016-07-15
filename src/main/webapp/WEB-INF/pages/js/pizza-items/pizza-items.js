/**
 * creation date 11.07.2016
 *
 * @author A.Hrankina
 */
(function () {
    //TODO: loader
    var app = angular.module("pizzaItems", ["ngSanitize", "ui.bootstrap", "ui.grid", "ui.grid.selection", "ui.select", "ui.grid.autoResize"]);

    app.controller("PizzaItemsCtrl", function ($scope, $http, $modal, $filter) {
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
                {field: "pizzaName", displayName: "Пицца", maxWidth: 140},
                {field: "sauceName", displayName: "Соус", maxWidth: 180},
                {field: "shortcakeName", displayName: "Корж", maxWidth: 100},
                {field: "sizeName", displayName: "Размер", maxWidth: 100},
                {field: "quantity", displayName: "Количество", maxWidth: 210},
                {field: "price", displayName: "Итог", maxWidth: 140}
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

        /* dictionaries */
        store.loadPizzas = function () {
            $http.get("/api/pizzas/list")
                .success(function (data) {
                    store.pizzas = data;
                    console.log(data);
                })
                .error(function () {
                    //TODO: show error...
                    console.log("error");
                });
        };
        store.loadPizzas();

        store.loadSauces = function () {
            $http.get("/api/sauces/list")
                .success(function (data) {
                    store.sauces = data;
                    console.log(data);
                })
                .error(function () {
                    //TODO: show error...
                    console.log("error");
                });
        };
        store.loadSauces();

        store.loadShortcakes = function () {
            $http.get("/api/shortcakes/list")
                .success(function (data) {
                    store.shortcakes = data;
                    console.log(data);
                })
                .error(function () {
                    //TODO: show error...
                    console.log("error");
                });
        };
        store.loadShortcakes();

        store.loadSizes = function () {
            $http.get("/api/sizes/list")
                .success(function (data) {
                    store.sizes = data;
                    console.log(data);
                })
                .error(function () {
                    //TODO: show error...
                    console.log("error");
                });
        };
        store.loadSizes();

        store.filters = {};
        store.aFilters = {};
        store.clearFilters = function () {
            store.filters = {};
            store.aFilters = {};
            store.currentPage = 1; //TODO: refresh pages only then filters have been setted before
            store.refresh();
        };

        store.doFilters = function () {
            store.aFilters.pizzaId = store.filters.pizza ? store.filters.pizza.id : undefined;
            store.aFilters.sauceId = store.filters.sauce ? store.filters.sauce.id : undefined;
            store.aFilters.shortcakeId = store.filters.shortcake ? store.filters.shortcake.id : undefined;
            store.aFilters.sizeId = store.filters.size ? store.filters.size.id : undefined;
            store.aFilters.docNo = store.filters.docNo ? store.filters.docNo : undefined;

            store.currentPage = 1; //TODO: refresh pages only then filters have been setted before
            store.refresh();
        };

        store.loadList = function (page, limit) {
            store.isRowSelected = false;

            var pizzaIdParam = store.aFilters.pizzaId ? "&pizza_id=" + store.aFilters.pizzaId : "";
            var sauceIdParam = store.aFilters.sauceId ? "&sauce_id=" + store.aFilters.sauceId : "";
            var shortcakeIdParam = store.aFilters.shortcakeId ? "&shortcake_id=" + store.aFilters.shortcakeId : "";
            var sizeIdParam = store.aFilters.sizeId ? "&size_id=" + store.aFilters.sizeId : "";

            var sort = store.sort ? "&order=" + store.sort.name + "&dir=" + store.sort.dir : "";
            var params = "?page=" + (page - 1) + "&limit=" + limit + pizzaIdParam + sauceIdParam + shortcakeIdParam + sizeIdParam + sort;
            $http.get("/api/pizza-items" + params)
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
            store.loadPizzas();
            store.loadSauces();
            store.loadShortcakes();
            store.loadSizes();
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
                templateUrl: 'js/pizza-items/pizza-items-edit.html',
                controller: 'PizzaItemsEditFormCtrl',
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
                controller: 'PizzaItemsDeleteConfirmCtrl',
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

    app.directive("pizzaItemsList", function () {
        return {
            restrict: "E",
            templateUrl: "js/pizza-items/pizza-items-list.html"
        }
    });

    app.controller('PizzaItemsDeleteConfirmCtrl', function ($http, $scope, $modalInstance, $timeout, recordId, sender, $filter) {
        var store = this;

        store.isLast = false;
        store.elementsOnPage = 0;

        $scope.loading = false;
        $scope.ok = function () {
            $scope.loading = true;
            $scope.alerts = [];
            $http.delete("/api/pizza-items/" + recordId)
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

    app.controller('PizzaItemsEditFormCtrl', function ($http, $scope, $modalInstance, $timeout, $filter, recordId, sender) {
        var store = this;

        $scope.pizza = {};
        $scope.pizzas = sender.pizzas;

        $scope.sauce = {};
        $scope.sauces = sender.sauces;

        $scope.shortcake = {};
        $scope.shortcakes = sender.shortcakes;

        $scope.size = {};
        $scope.sizes = sender.sizes;

        $scope.formRecord = {};

        $scope.recordId = recordId;
        $scope.loading = false;
        store.loadRecord = function () {
            $scope.loading = true;
            $http.get("/api/pizza-items/" + recordId)
                .success(function (data) {
                    $scope.formRecord = data;
                    $scope.formRecord.pizza = store.findInDictById($scope.formRecord.pizzaId, $scope.pizzas);
                    $scope.formRecord.sauce = store.findInDictById($scope.formRecord.sauceId, $scope.sauces);
                    $scope.formRecord.shortcake = store.findInDictById($scope.formRecord.shortcakeId, $scope.shortcakes);
                    $scope.formRecord.size = store.findInDictById($scope.formRecord.sizeId, $scope.sizes);
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

            if ($scope.formRecord.pizza) {
                $scope.formRecord.pizzaId = $scope.formRecord.pizza.id;
            } else {
                store.addAlert("Пицца обязательна для заполнения");
                $scope.loading = false;
                return;
            }

            if ($scope.formRecord.sauce) {
                $scope.formRecord.sauceId = $scope.formRecord.sauce.id;
            } else {
                store.addAlert("Соус обязателен для заполнения");
                $scope.loading = false;
                return;
            }

            if ($scope.formRecord.shortcake) {
                $scope.formRecord.shortcakeId = $scope.formRecord.shortcake.id;
            } else {
                store.addAlert("Корж обязателен для заполнения");
                $scope.loading = false;
                return;
            }

            if ($scope.formRecord.size) {
                $scope.formRecord.sizeId = $scope.formRecord.size.id;
            } else {
                store.addAlert("Размер обязателен для заполнения");
                $scope.loading = false;
                return;
            }

            $scope.formRecord.price = $scope.formRecord.pizzaPrice * $scope.formRecord.quantity;

            $http.post("/api/pizza-items", $scope.formRecord)
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
