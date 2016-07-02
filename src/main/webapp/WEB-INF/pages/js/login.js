/**
 * creation date 24.06.2016
 *
 * @author A.Hrankina
 */
(function () {
    var app = angular.module("login", []);

    app.controller("LoginCtrl", function($location) {
        var store = this;
        store.msg = "";
        store.showMsg = false;
        var reason = location.search.substring(1);

        if (reason !== "") {
            store.showMsg = true;
            switch (reason) {
                case "logout": {store.msg = "Вы вышли из системы"; break;}
                case "notfound": {store.msg = "Пользователь не найден"; break;}
                case "disabled": {store.msg = "Пользователь заблокирован"; break;}
                case "other": {store.msg = "Неопределённая ошибка. Обратитесь к администратору"; break;}
            }
        }

        console.log($location);
    });
})();