const app = angular.module("shopping-cart-app", []);

app.controller("shopping-cart-ctrl", function ($scope, $http) {
    $scope.cart = {
        items: [],
        itemCount: 0,
        selectAll: false,
        totalAmount: 0, // Thêm biến để lưu tổng giá

        add(itemId) {
            var item = this.items.find(item => item.itemId == itemId);   
                $http.get(`/rest/products/${itemId}`).then(resp => {
                    resp.data.quantity = 1;
                    this.items.push(resp.data);
                    this.saveToDatabase(itemId, 1);
                    $scope.alertSuccess("Sản phẩm đã được thêm vào giỏ hàng!!!"); // Sử dụng $scope để gọi hàm alertSuccess
                     // Cập nhật số lượng sản phẩm sau khi thêm thành công
                $scope.cart.itemCount += 1;
                })         
        },
        saveToDatabase(itemId, quantity) {
            var accountId = 1; // Thay thế bằng cách lấy accountId từ người dùng sau khi đăng nhập
            var url = `/rest/products/add-to-cart/${itemId}/${accountId}/${quantity}`;

            $http.post(url)
                .then(response => {
                    console.log(response.data);
                    
                    this.loadCartItems(); // Load lại danh sách sản phẩm trong giỏ hàng sau khi cập nhật   
                    $scope.cart.itemCount += 1;             
                })
                .catch(error => {
                    console.error(error);
                });
        },

       
        loadCartItems() {
            $http.get('/rest/products/cart-items')
                .then(response => {
                    $scope.cart.itemCount = response.data.length; // Cập nhật số lượng sản phẩm
                    this.items = response.data;
                    this.calculateTotalAmount(); // Tính tổng giá sau khi cập nhật danh sách sản phẩm
                })
                .catch(error => {
                    console.error(error);
                });
        },
        
        removeFromCart(itemId) {
            var accountId = 1; // Thay thế bằng cách lấy accountId từ người dùng sau khi đăng nhập
            var url = `/rest/products/remove-from-cart/${itemId}/${accountId}`;
           
            $http.delete(url)
            .then(response => {
                console.log(response.data.message); // Hiển thị thông báo từ phản hồi
                // Cập nhật số lượng sản phẩm sau khi xóa thành công
                $scope.cart.itemCount -= 1;
                // Cập nhật danh sách sản phẩm trong giỏ hàng
                this.loadCartItems();
                // Hiển thị thông báo thành công
                $scope.alertSuccess(response.data.message);
            })
            .catch(error => {
                console.error(error);
            });
        
        },
        calculateTotalAmount() {
            $scope.cart.totalAmount = 0;
            for (var i = 0; i < this.items.length; i++) {
                var item = this.items[i];
                $scope.cart.totalAmount += item.item.price * item.quantity;
            }
        },
        
    }
    $scope.alertInfo = function (message) {
        Toastify({
            text: message,
            duration: 1000,
            newWindow: true,
            gravity: "top",
            position: "center",
            stopOnFocus: true,
            style: {
                background: "#rgb(255, 165, 0)",
                color: "white",
            },
            onClick: function () { }
        }).showToast();
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
            onClick: function () {}
        }).showToast();
    };

    $scope.alertError = function (message) {
        Toastify({
            text: message,
            duration: 1000,
            newWindow: true,
            gravity: "top",
            position: "right",
            stopOnFocus: true,
            style: {
                background: "#dc3545",
                color: "white",
            },
            onClick: function () {}
        }).showToast();
    };
    $scope.removeSelectedItems = function() {
        var selectedItems = $scope.cart.items.filter(item => item.selected);

        if (selectedItems.length === 0) {
            $scope.alertError("Vui lòng chọn ít nhất một sản phẩm để xóa.");
            return;
        }
        selectedItems.forEach(item => {
            $scope.cart.removeFromCart(item.item.itemId);
        });
    };

    $scope.selectAllItems = function() {
        $scope.cart.items.forEach(item => {
            item.selected = $scope.cart.selectAll;
        });
    };

    $scope.updateCartItemQuantity = function(item) {
        var accountId = 1; // Thay thế bằng cách lấy accountId từ người dùng sau khi đăng nhập
        var url = `/rest/products/update-quantity/${item.item.itemId}/${item.quantity}/${accountId}`;
    
        $http.put(url)
            .then(response => {
                console.log(response.data);
                $scope.alertSuccess(response.data.message);
              $scope.cart.calculateTotalAmount();
            })
            .catch(error => {
                console.error(error);
            });
    };
    
    $scope.loadCategories = function () {
        $http.get('/rest/products/categories')
            .then(function (response) {
                $scope.categories = response.data;
            })
            .catch(function (error) {
                console.error('Error fetching categories:', error);
            });
    };
    $scope.loadItemsByCategory = function (categoryId) {
        $http.get('/rest/products/items/by-category?categoryId=' + categoryId)
            .then(function (response) {
                $scope.items = response.data.content;
                $scope.totalPages = response.data.totalPages;
                if ($scope.items.length === 0) {
                    $scope.alertInfo("Không có sản phẩm!!!");
                }
            })
            .catch(function (error) {
                console.error('Error fetching items by category:', error);
            });
    };

    $scope.loadCategories();
    $scope.cart.loadCartItems();

});

