// Đảm bảo rằng bạn đã bao gồm thư viện AngularJS trong dự án của mình
// Ví dụ: <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.2/angular.min.js"></script>

var app = angular.module('index-app', []);

app.controller('index-controller', function ($scope, $http, $window) {
    $scope.currentPage = 0;
    $scope.totalPages = 0;
    $scope.items = [];
    $scope.priceRanges = [
        { min: 0, max: 50000, label: "Dưới 50.000 VNĐ" },
        { min: 50000, max: 100000, label: "50.000-100.000 VNĐ" },
        { min: 100000, max: 200000, label: "100.000-200.000 VNĐ" },
        // Thêm các mốc giá khác tùy ý
    ];
    $scope.selectedPriceRange = $scope.priceRanges[0];
    $scope.filterItemsByPrice = function () {
        var minPrice = $scope.selectedPriceRange.min;
        var maxPrice = $scope.selectedPriceRange.max;

        $http.get('/api/items/filterByPrice', { params: { minPrice: minPrice, maxPrice: maxPrice } })
            .then(function (response) {
                
                $scope.items = response.data;
                if ($scope.items.length === 0) {
                    $scope.alertInfo("Không tìm thấy sản phẩm!!!");
                    return;
                }
                $scope.alertSuccess("Đã lọc sản phẩm theo giá!");
            })
            .catch(function (error) {
                console.error('Error while filtering items:', error);
                $scope.alertInfo("Có lỗi xảy ra khi lọc sản phẩm theo giá!");
            });
    };
    // Tự động gọi hàm lọc sản phẩm khi mốc giá thay đổi
    $scope.$watch('selectedPriceRange', function (newVal, oldVal) {
        if (newVal !== oldVal) {
            $scope.filterItemsByPrice();
        }
    });
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
    $scope.relatedProducts = [];
    $scope.item = {};

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
            $scope.items = response.data;
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
        $http.get('/api/ss/' + itemId)
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
        window.location.href = `/single-product/${itemId}`; // Sử dụng chuỗi template (ES6)
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

    $scope.loadItemDetails();
    $scope.loadCategories(); // Gọi hàm để tải danh sách loại hàng khi trang được 
    $scope.cart.loadCartItems();
   
});
















