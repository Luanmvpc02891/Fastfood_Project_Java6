// Đảm bảo rằng bạn đã bao gồm thư viện AngularJS trong dự án của mình
// Ví dụ: <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.2/angular.min.js"></script>

var app = angular.module('index-app', []);

app.controller('index-controller', function ($scope, $http , $window) {
    $scope.currentPage = 0;
    $scope.totalPages = 0;
    $scope.items = [];
    $scope.cart = {
        items: [],
        itemCount: 0,

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
        $scope.alertInfo = function (message) {
            Toastify({
                text: message,
                duration: 1000,
                newWindow: true,
                gravity: "top",
                position: "right",
                stopOnFocus: true,
                style: {
                    background: "#rgb(255, 165, 0)",
                    color: "white",
                },
                onClick: function () { }
            }).showToast();
        };
        $scope.item = {};
        $scope.relatedProducts = [];

        $scope.searchItems = function () {
            $http.get('/api/items/search?keyword=' + $scope.searchKeyword)
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

        $http.get('/api/items')
            .then(function (response) {
                $scope.items = response.data.content;
            })
            .catch(function (error) {
                console.error('Error fetching items:', error);
            });
            
            $scope.loadCategories = function () {
                $http.get('/api/categories')
                    .then(function (response) {
                        $scope.categories = response.data;
                    })
                    .catch(function (error) {
                        console.error('Error fetching categories:', error);
                    });
            };
            $scope.loadItemsByCategory = function (categoryId) {
                $http.get('/api/items/by-category?categoryId=' + categoryId)
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
            $scope.loadItemDetails = function (itemId) {
                $http.get('/api/shop/' + itemId)
                    .then(function (response) {
                        $scope.item = response.data.item;
                        $scope.relatedProducts = response.data.relatedProducts;
                        $scope.goToSinglePage(itemId); // Chuyển hướng sau khi tải xong dữ liệu
                    })
                    .catch(function (error) {
                        console.error('Error fetching item details:', error);
                    });
            };
            
            $scope.goToSinglePage = function (itemId) {
                $window.location.href = '/single-product?itemId=' + itemId;
            };



         $scope.loadCategories(); // Gọi hàm để tải danh sách loại hàng khi trang được 
         //$scope.loadItemDetails();
         $scope.cart.loadCartItems();
});
















