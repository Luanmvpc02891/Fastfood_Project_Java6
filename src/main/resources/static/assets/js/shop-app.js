var app = angular.module('shop-app', []);

app.controller('shop-controller', function ($scope, $http , $window) {
    $scope.cart = {
        items: [],

        loadCartItems() {
            var accountId = 1; // Thay thế bằng cách lấy accountId từ người dùng sau khi đăng nhập
            var url = `/rest/products/cart-items/${accountId}`;

            $http.get(url)
                .then(response => {
                    $scope.cart.itemCount = response.data.length; // Cập nhật số lượng sản phẩm
                    this.items = response.data;
                    this.calculateTotalAmount(); // Tính tổng giá sau khi cập nhật danh sách sản phẩm
                })
                .catch(error => {
                    console.error(error);
                });
        },

        add(itemId) {
            
            var item = this.items.find(item => item.itemId == itemId);
            $http.get(`/rest/products/${itemId}`).then(resp => {
                resp.data.quantity = 1;
                this.items.push(resp.data);
                this.saveToDatabase(itemId, 1);
                $scope.alertSuccess("Sản phẩm đã được thêm vào giỏ hàng !!!");
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
        calculateTotalAmount() {
            $scope.cart.totalAmount = 0;
            for (var i = 0; i < this.items.length; i++) {
                var item = this.items[i];
                $scope.cart.totalAmount += item.item.price * item.quantity;
            }
        },
    }
    $scope.page = 0;
    $scope.currentPage = 0;
    $scope.totalPages = 0;
    $scope.items = [];
    $scope.item = {};
    $scope.relatedProducts = [];
//load dữ liệu
    // $http.get('/api/shop/items')
    // .then(function (response) {
    //     $scope.items = response.data.content;
    // })
    // .catch(function (error) {
    //     console.error('Error fetching items:', error);
    // });
    $scope.loadItem = function () {
        $http.get('/api/shop/items')
            .then(function (response) {
                $scope.items = response.data;
            });
    };

    $scope.loadCategories = function () {
        $http.get('/api/shop/categories')
            .then(function (response) {
                $scope.categories = response.data;
            })
            .catch(function (error) {
                console.error('Error fetching categories:', error);
            });
    };
    
    $scope.loadItemsByCategory = function (categoryId) {
        $http.get('/api/shop/items/by-category?categoryId=' + categoryId)
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

    $scope.searchItems = function () {
        $http.get('/api/shop/items/search?keyword=' + $scope.searchKeyword)
            .then(function (response) {
                $scope.items = response.data.content;
                if ($scope.items.length === 0) {
                    $scope.alertInfo("Không tìm thấy sản phẩm!!!");
                }
            })
            .catch(function (error) {
                console.error('Error fetching items:', error);
            });
    };
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
            onClick: function () { }
        }).showToast();
    };
    $scope.loadItemDetails = function (itemId) {
        $http.get(`/api/shop/item/${itemId}`)
            .then(function (response) {
                $scope.item = response.data.item;
                $scope.relatedProducts = response.data.relatedProducts;
                // Không cần chuyển hướng tại đây nữa
            })
            .catch(function (error) {
                console.error('Error fetching item details:', error);
            });
    };
    function filterItemsByStatus(isActive) {
        return $scope.items.filter(function (item) {
            return item.active === isActive;
        });
    }

    $scope.pager = {
        page: 0,
        size: 6,
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
    $scope.loadCategories($scope.page);
    $scope.cart.loadCartItems();
    $scope.loadItem($scope.page);
   
});
