/**
 * creation date 24.06.2016
 *
 * @author A.Hrankina
 */
(function () {
    //TODO: loader
    var app = angular.module("users", ["ngSanitize", "ui.bootstrap", "ui.grid", "ui.grid.selection", "ui.select", "ui.grid.autoResize"]);

    app.controller("usersCtrl", function ($scope, $http, $modal) {
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
            modifierKeysToMultiSelect: false,
            columnDefs: [
                {field: "id", displayName: 'ИД', visible: false},
                {field: "username", displayName: "Логин"},
                {field: "lastName", displayName: "Фамилия"},
                {field: "firstName", displayName: "Имя"},
                {field: "secondName", displayName: "Отчество"},
                {field: "roleComments", displayName: "Роль", enableSorting: false},
                {
                    field: "enabled",
                    displayName: "Активний",
                    cellTemplate: '<div style="text-align: center"><input type="checkbox" ng-model="row.entity.enabled" ng-disabled="true" readonly></div>',
                    maxWidth: 140
                }
            ],
            useExternalSorting: true,
            enableGridMenu: true
        };
        $scope.gridOptions.onRegisterApi = function (gridApi) {
            gridApi.core.on.sortChanged($scope, $scope.sortChanged);
            //$scope.sortChanged($scope.gridApi.grid, [$scope.gridOptions.columnDefs[1]]);
            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                store.isRowSelected = row.isSelected;
            });
            store.selection = gridApi.selection;
        };

        /* dictionaries */
        store.roles = [];
        store.loadRoles = function () {
            $http.get("/api/roles")
                .success(function (data) {
                    store.roles = data.content;
                    console.log(data);
                })
                .error(function () {
                    //TODO: show error...
                    console.log("error");
                });
        };
        store.loadRoles();

        store.filters = {};
        store.aFilters = {};
        store.clearFilters = function () {
            store.filters = {};
            store.aFilters = {};
            store.currentPage = 1; //TODO: refresh pages only then filters have been setted before
            store.refresh();
        };

        store.doFilters = function () {
            if (store.filters.username || store.filters.username === "")
                store.aFilters.username = store.filters.username;
            if (store.filters.role)
                store.aFilters.roleId = store.filters.role.id;

            store.currentPage = 1; //TODO: refresh pages only then filters have been setted before
            store.refresh();
        };

        store.loadList = function (page, limit) {
            store.isRowSelected = false;
            var usernameParam = store.aFilters.username ? "&username=" + store.aFilters.username : "";
            var roleIdParam = store.aFilters.roleId ? "&role_id=" + store.aFilters.roleId : "";
            var sort = store.sort ? "&order=" + store.sort.name + "&dir=" + store.sort.dir : "";
            var params = "?page=" + (page - 1) + "&limit=" + limit + usernameParam + roleIdParam + sort;
            $http.get("/api/users" + params)
                .success(function (data) {
                    store.totalElements = data.totalElements;
                    store.currentPage = data.number + 1;
                    store.totalPages = data.totalPages;
                    store.size = data.size;

                    store.isLast = data.last;
                    store.elementsOnPage = data.numberOfElements;

                    $scope.items = data.content;
                    console.log(data);
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
            store.loadRoles();
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
                templateUrl: 'js/users/users-edit.html',
                controller: 'UsersEditFormCtrl',
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
                //size: "sm",
                templateUrl: 'js/common/delete-dialog.html',
                controller: 'UsersDeleteConfirmCtrl',
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

    app.directive("usersList", function () {
        return {
            restrict: "E",
            templateUrl: "js/users/users-list.html"
        }
    });

    app.controller('UsersDeleteConfirmCtrl', function ($http, $scope, $modalInstance, $timeout, recordId, sender) {
        var store = this;

        store.isLast = false;
        store.elementsOnPage = 0;

        $scope.loading = false;
        $scope.ok = function () {
            $scope.loading = true;
            $scope.alerts = [];
            $http.delete("/api/users/" + recordId)
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

    app.controller('UsersEditFormCtrl', function ($http, $scope, $modalInstance, $timeout, recordId, sender) {
        var store = this;

        $scope.role = {};
        $scope.roles = sender.roles;

        $scope.recordId = recordId;
        $scope.loading = false;
        $scope.formRecord = {};
        store.loadRecord = function () {
            $scope.loading = true;
            $http.get("/api/users/" + recordId)
                .success(function (data) {
                    $scope.formRecord = data;
                    $scope.formRecord.role = store.findInDictById($scope.formRecord.roleId, $scope.roles);
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
        };

        $scope.ok = function () {
            $scope.loading = true;
            $scope.alerts = [];
            if ($scope.formRecord.role) {
                $scope.formRecord.roleId = $scope.formRecord.role.id;
            } else {
                store.addAlert("Роль обязательна для заполнения");
                $scope.loading = false;
                return;
            }

            if ($scope.formRecord.changePwd && ($scope.formRecord.password == null || $scope.formRecord.password.trim() == "")) {
                store.addAlert("Пароль не может быть пустым");
                $scope.loading = false;
                return;
            }

            if ($scope.formRecord.id == null && !$scope.validation.username.success) {
                store.addAlert("Логин должен быть уникальным и содержать более 3 символов");
                $scope.loading = false;
                return;
            }

            $http.post("/api/users", $scope.formRecord)
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

        /* validation */
        $scope.validation = {};
        $scope.validation.username = {};
        $scope.onUsernameEnter = function () {
            $scope.validation.username.error = false;
            $scope.validation.username.success = false;
            if ($scope.formRecord.username.length > 3) { //TODO: order of AJAX requests
                $http.get("/api/users/check/" + $scope.formRecord.username)
                    .error(function (data, status) {
                        if (status == 404) {
                            $scope.validation.username.success = true;
                        } else {
                            $scope.validation.username.error = true;
                        }
                    });
            }
        };

        if (recordId) {
            store.loadRecord();
        }
    });
})();