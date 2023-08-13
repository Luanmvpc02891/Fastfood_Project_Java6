const app = angular.module("item-app", []);
app.controller('item-controller', function ($scope, $http) {
    $scope.items = [];
    $scope.newItem = {};
    $scope.form = {};
    $scope.page = 0;
    $scope.totalPages = 0;
    $scope.categories = [];
    $scope.discounts = []; 

    $scope.loadItems = function () {
        $http.get('/product/item1')
            .then(function (response) {
                $scope.items = response.data;
            });
    };

    $scope.loadCategories = function () {
        $http.get('/api/categories')
            .then(function (response) {
                $scope.categories = response.data;
            })
            .catch(function (error) {
                console.error('Error fetching categories:', error);
            });
    };

    $scope.loadDiscount = function () {
        $http.get('/Discount/load')
            .then(function (response) {
                $scope.discounts = response.data;
            });
    };

    $scope.imageChanged = function (files) {
        var data = new FormData();
        data.append('file', files[0]);
        $http.post('/rest/uploads/images', data, {
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined }
        }).then(resp => {
            $scope.newItem.image = resp.data.name;
        }).catch(error => {
            alert("Loi roi");
            console.log("Error:" + error);
        })
    }

    $scope.addItem = function () {
        $http.post('/product/item1', $scope.newItem)
            .then(function (response) {
                $scope.items.push(response.data);;
                $scope.newItem = {};
                $scope.alertSuccess("Thêm sản phẩm thành công!!!");
                $scope.loadItems();
            });
    };

    $scope.updateItem = function () {
        // Thực hiện kiểm tra form có hợp lệ hay không
        if ($scope.form.$invalid) {
            // Hiển thị thông báo lỗi nếu form không hợp lệ
            $scope.alertWaring("Vui lòng điền đầy đủ thông tin hợp lệ.");
            return;
        }

        // Gọi API để cập nhật thông tin item
        $http.put(`/product/item1/${$scope.newItem.itemId}`, $scope.newItem)
            .then(function (response) {
                // Tìm và cập nhật thông tin người dùng trong mảng $scope.users
                var index = $scope.items.findIndex(item => item.itemId === $scope.newItem.itemId);
                if (index !== -1) {
                    $scope.items[index] = response.data;
                }
                // Xóa thông tin người dùng trong biểu mẫu sau khi cập nhật thành công
                $scope.newItem = {};
                $scope.alertSuccess("Cập nhật item thành công!!!");
            })
            .catch(function (error) {
                // Xử lý lỗi nếu có
                $scope.alertWaring("Có lỗi xảy ra khi cập nhật thông tin item!!!." + error.statusText);
                console.error(error);
            });
    };
    $scope.deactivateItem = function (item) {
        // Xác nhận item dùng muốn cập nhật trạng thái active về false
        if (confirm("Bạn có chắc chắn muốn xóa item này?")) {
            item.active = false;
            $scope.alertSuccess("Xóa item thành công!!!");
            // Gọi API để cập nhật trạng thái active của người dùng
            $http.put(`/product/deactivate/${item.itemId}`, item)
                .then(function (response) {
                    // Cập nhật thông tin người dùng trong mảng $scope.users
                    var index = $scope.items.findIndex(u => u.itemId === item.itemId);
                    if (index !== -1) {
                        $scope.items[index] = response.data;
                    }
                })
                .catch(function (error) {
                    // Xử lý lỗi nếu có
                    $scope.alertWaring("Có lỗi xảy ra khi xóa item này.");
                    console.error(error);
                    // Phục hồi trạng thái active về true nếu xảy ra lỗi
                    item.active = true;
                });
        }
    };
    // Hàm lọc danh sách người dùng dựa trên trạng thái isActive
    function filterItemsByStatus(isActive) {
        return $scope.items.filter(function (item) {
            return item.active === isActive;
        });
    }

    $scope.pager = {
        page: 0,
        size: 3,
        isActive: true,

        get items() {
            var filteredItems = filterItemsByStatus(this.isActive);
            var start = this.page * this.size;
            return filteredItems.slice(start, start + this.size);
        },

        get count() {
            var filteredItems = filterItemsByStatus(this.isActive);
            return Math.ceil(1.0 * filteredItems.length / this.size);
        },

        first() {
            this.page = 0;
        },
        prev() {
            this.page--;
            if (this.page < 0) {
                this.last();
            }
        },
        next() {
            this.page++;
            if (this.page >= this.count) {
                this.first();
            }
        },
        last() {
            this.page = this.count - 1;
        }
    };
    $scope.edit = function (items) {
        $scope.newItem = angular.copy(items);
    }
    $scope.alertSuccess = function (message) {
        Toastify({
            text: message,
            duration: 1000,
            newWindow: true,
            gravity: "top",
            position: "right",
            stopOnFocus: true,
            style: {
                background: "#34c240",
                color: "white",
            },
            onClick: function () { }
        }).showToast();
    };

    $scope.alertWaring = function (message) {
        Toastify({
            text: message,
            duration: 1000,
            newWindow: true,
            gravity: "top",
            position: "right",
            stopOnFocus: true,
            style: {
                background: "#FF0000",
                color: "white",
            },
            onClick: function () { }
        }).showToast();
    };
    
    $scope.loadDiscount();
    $scope.loadCategories();
    $scope.loadItems($scope.page);
});