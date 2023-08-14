const app = angular.module("account-role-app", []);
app.controller('account-role-controller', function ($scope, $http) {

    $scope.roles = [];
    $scope.newRole = {};
    $scope.form = {};
    $scope.page = 0;
    $scope.totalPages = 0;
    $scope.users = [];
    $scope.roleall = [];
    $scope.loadAccRole = function () {
        $http.get('/account/role')
            .then(function (response) {
                $scope.roles = response.data;
            });
    };
    $scope.loadRole = function () {
        $http.get('/api/roleall')
            .then(function (response) {
                $scope.roleall = response.data;
            });
    };
    $scope.loadUsers = function () {
        $http.get('/api/accounts')
            .then(function (response) {
                $scope.users = response.data;
            });
    };
    $scope.addRoleAcc = function() {
        var existingItem = checkIfRoleExists($scope.newRole);
    
        if (existingItem) {
            $scope.alertWaring("Quyền đã tồn tại cho account!");
            return;
        }
    
        $http.post('/account/role', $scope.newRole)
            .then(function(response) {
                $scope.roles.push(response.data);
                $scope.newRole = {};
                $scope.alertSuccess("Phân quyền thành công!!!");
                $scope.loadAccRole();
            })
            .catch(function(error) {
                // Xử lý lỗi nếu cần thiết
                console.error('Error while adding role:', error);
                $scope.alertWaring("Có lỗi xảy ra khi thêm phân quyền!");
            });
    };
    
    // Kiểm tra xem cặp (accountId, roleId) đã tồn tại trong danh sách roles hay không
    function checkIfRoleExists(newRole) {
        for (var i = 0; i < $scope.roles.length; i++) {
            var role = $scope.roles[i];
            if (role.account.accountId === newRole.account.accountId && role.role.roleId === newRole.role.roleId) {
                return true;
            }
        }
        return false;
    }
    

    $scope.updateRoleAcc = function () {
        // Thực hiện kiểm tra form có hợp lệ hay không
        if ($scope.form.$invalid) {
            // Hiển thị thông báo lỗi nếu form không hợp lệ
            $scope.alertWaring("Vui lòng điền đầy đủ thông tin hợp lệ.");
            return;
        }

        // Gọi API để cập nhật thông tin item
        $http.put(`/account/role/update/${$scope.newRole.accountRolesId}`, $scope.newRole)
            .then(function (response) {
                // Tìm và cập nhật thông tin người dùng trong mảng $scope.users
                var index = $scope.roles.findIndex(cate => cate.roleId === $scope.newRole.accountRolesId);
                if (index !== -1) {
                    $scope.roles[index] = response.data;
                }
                // Xóa thông tin người dùng trong biểu mẫu sau khi cập nhật thành công
                $scope.newRole = {};
                $scope.alertSuccess("Cập nhật quyền thành công!!!");
                $scope.loadAccRole();
            })
            .catch(function (error) {
                // Xử lý lỗi nếu có
                $scope.alertWaring("Có lỗi xảy ra khi cập nhật thông tin quyền!!!." + error.statusText);
                console.error(error);
            });
    };
    $scope.deleteAccountRoles = function(accountRolesId) {
        var confirmed = window.confirm('Bạn có chắc chắn muốn xóa?');
        if (!confirmed) {
            return; // Không xóa nếu người dùng không xác nhận
        }

        $http.delete('/account/role/delete/' + accountRolesId)
        
            .then(function(response) {
                $scope.alertSuccess("Xóa thành công!!!");
                $scope.loadAccRole();
                // Xóa thành công, bạn có thể thực hiện các hành động khác tại đây
                console.log('Deleted successfully:', response.data.message);
            })
            .catch(function(error) {
                // Xử lý lỗi nếu cần thiết
                console.error('Error while deleting:', error);
            });
    };
   
    $scope.pager = {
        page: 0,
        size: 3,

        get roles() {
            var start = this.page * this.size;
            return $scope.roles.slice(start, start + this.size);
        },

        get count() {
            return Math.ceil(1.0 * $scope.roles.length / this.size);
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
    $scope.edit = function (roles) {
        $scope.newRole = angular.copy(roles);
    }
    $scope.loadRole();
    $scope.loadUsers();
    $scope.loadAccRole($scope.page);
});