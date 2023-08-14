const app = angular.module("user-app", []);
app.controller('user-controller', function ($scope, $http) {

    $scope.users = [];
    $scope.newUser = {};
    $scope.form = {};
    $scope.page = 0;
    $scope.totalPages = 0;
    $scope.addresscity = [];
    $scope.addressdis = [];

    $scope.loadAddressCity = function () {
        $http.get('/Addresscity/load')
            .then(function (response) {
                $scope.addresscity = response.data;
            });
    };

    $scope.loadAddressDist = function () {
        $http.get('/AddressDistrict/load')
            .then(function (response) {
                $scope.addressdis = response.data;
            });
    };

    $scope.loadUsers = function () {
        $http.get('/api/accounts')
            .then(function (response) {
                $scope.users = response.data;
            });
    };


    // ...

    $scope.pager = {
        page: 0,
        size: 5,

        get users() {
            var start = this.page * this.size;
            return $scope.users.slice(start, start + this.size);
        },

        get count() {
            return Math.ceil(1.0 * $scope.users.length / this.size);
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
    $scope.addUser = function () {
        $http.post('/api/accounts', $scope.newUser)
            .then(function (response) {
                $scope.users.push(response.data);;
                $scope.newUser = {};
                $scope.alertSuccess("Thêm account thành công!!!");
                $scope.loadUsers();
            });
    };


    $scope.imageChanged = function (files) {
        var data = new FormData();
        data.append('file', files[0]);
        $http.post('/rest/uploads/images', data, {
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined }
        }).then(resp => {
            $scope.newUser.image = resp.data.name;
        }).catch(error => {
            alert("Loi roi");
            console.log("Error:" + error);
        })
    }

    $scope.updateUser = function () {
        // Thực hiện kiểm tra form có hợp lệ hay không
        if ($scope.form.$invalid) {
            // Hiển thị thông báo lỗi nếu form không hợp lệ
            $scope.alertWaring("Vui lòng điền đầy đủ thông tin hợp lệ.");
            return;
        }

        // Gọi API để cập nhật thông tin người dùng
        $http.put(`/api/accounts/${$scope.newUser.accountId}`, $scope.newUser)
            .then(function (response) {
                // Tìm và cập nhật thông tin người dùng trong mảng $scope.users
                var index = $scope.users.findIndex(user => user.accountId === $scope.newUser.accountId);
                if (index !== -1) {
                    $scope.users[index] = response.data;
                }
                // Xóa thông tin người dùng trong biểu mẫu sau khi cập nhật thành công
                $scope.newUser = {};
                $scope.alertSuccess("Cập nhật account thành công!!!");
            })
            .catch(function (error) {
                // Xử lý lỗi nếu có
                $scope.alertWaring("Có lỗi xảy ra khi cập nhật thông tin người dùng.");
                console.error(error);
            });
    };

    $scope.deactivateUser = function (user) {
        // Xác nhận người dùng muốn cập nhật trạng thái active về false
        if (confirm("Bạn có chắc chắn muốn vô hiệu hóa người dùng này?")) {
            user.active = false;

            // Gọi API để cập nhật trạng thái active của người dùng
            $http.put(`/api/accounts/${user.accountId}`, user)
                .then(function (response) {
                    // Cập nhật thông tin người dùng trong mảng $scope.users
                    var index = $scope.users.findIndex(u => u.accountId === user.accountId);
                    if (index !== -1) {
                        $scope.users[index] = response.data;
                    }
                })
                .catch(function (error) {
                    // Xử lý lỗi nếu có
                    $scope.alertWaring("Có lỗi xảy ra khi cập nhật trạng thái người dùng.");
                    console.error(error);
                    // Phục hồi trạng thái active về true nếu xảy ra lỗi
                    user.active = true;
                });
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
    $scope.edit = function (users) {
        $scope.newUser = angular.copy(users);
    }
    // Khởi tạo


    $scope.loadUsers($scope.page);
    $scope.loadAddressCity();
    $scope.loadAddressDist();

});
function displayImages(event) {
    var input = event.target;
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            var imageContainer = document.getElementById('uploadedImages');
            imageContainer.src = e.target.result;
            imageContainer.style.display = "block";
        };

        reader.readAsDataURL(input.files[0]);
    }
}