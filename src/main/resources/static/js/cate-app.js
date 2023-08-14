const app = angular.module("cate-app", []);
app.controller('cate-controller', function ($scope, $http) {

    $scope.cates = [];
    $scope.newCate = {};
    $scope.form = {};
    $scope.page = 0;
    $scope.totalPages = 0;

    $scope.loadCate = function () {
        $http.get('/api/categories')
            .then(function (response) {
                $scope.cates = response.data;
            });
    };

    $scope.addCate = function () {
        $http.post('/category/cate', $scope.newCate)
            .then(function (response) {
                $scope.cates.push(response.data);;
                $scope.newCate = {};
                $scope.alertSuccess("Thêm loại thành công!!!");
                $scope.loadCate();
            });
    };

    $scope.updateCate = function () {
        // Thực hiện kiểm tra form có hợp lệ hay không
        if ($scope.form.$invalid) {
            // Hiển thị thông báo lỗi nếu form không hợp lệ
            $scope.alertWaring("Vui lòng điền đầy đủ thông tin hợp lệ.");
            return;
        }

        // Gọi API để cập nhật thông tin item
        $http.put(`/category/cate/${$scope.newCate.categoryId}`, $scope.newCate)
            .then(function (response) {
                // Tìm và cập nhật thông tin người dùng trong mảng $scope.users
                var index = $scope.cates.findIndex(cate => cate.categoryId === $scope.newCate.categoryId);
                if (index !== -1) {
                    $scope.cates[index] = response.data;
                }
                // Xóa thông tin người dùng trong biểu mẫu sau khi cập nhật thành công
                $scope.newCate = {};
                $scope.alertSuccess("Cập nhật loại thành công!!!");
            })
            .catch(function (error) {
                // Xử lý lỗi nếu có
                $scope.alertWaring("Có lỗi xảy ra khi cập nhật thông tin loại!!!." + error.statusText);
                console.error(error);
            });
    };
    $scope.deactivateCate = function (item) {
        // Xác nhận item dùng muốn cập nhật trạng thái active về false
        if (confirm("Bạn có chắc chắn muốn xóa loại này?")) {
            item.active = false;
            $scope.alertSuccess("Xóa loại thành công!!!");
            // Gọi API để cập nhật trạng thái active của người dùng
            $http.put(`/category/deactivate/${cate.categoryId}`, cate)
                .then(function (response) {
                    // Cập nhật thông tin người dùng trong mảng $scope.users
                    var index = $scope.cates.findIndex(u => u.categoryId === cate.categoryId);
                    if (index !== -1) {
                        $scope.cates[index] = response.data;
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
function filterCatesByStatus(isActive) {
    return $scope.cates.filter(function (cate) {
        return cate.active === isActive;
    });
}

$scope.pager = {
    page: 0,
    size: 3,
    isActive: true,

    get cates() {
        var filteredItems = filterCatesByStatus(this.isActive);
        var start = this.page * this.size;
        return filteredItems.slice(start, start + this.size);
    },

    get count() {
        var filteredItems = filterCatesByStatus(this.isActive);
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
$scope.edit = function (cates) {
    $scope.newCate = angular.copy(cates);
}

    $scope.loadCate($scope.page);
});