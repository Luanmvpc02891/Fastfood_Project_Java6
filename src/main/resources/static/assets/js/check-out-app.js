const app = angular.module("check-out-app", []);

app.controller("check-out-ctrl", function ($scope, $http, $window) {
    $scope.userInfo = $window.sessionStorage.getItem("userInfo");
    $scope.accountId = 1;

    $scope.citys = [];
    $scope.districts = [];
    $scope.user = [];

    $scope.selectedCity = {};
    $scope.selectedDistrict = {};

    $scope.showSelects = false;

    $scope.load_citys = function () {
        var url = `/client/checkout/city`;
        $http.get(url).then(resp => {
            $scope.citys = resp.data;
            $scope.selectedCity = $scope.citys[0];
            console.log("Success city", resp);
        }).catch(error => {
            console.log("Error", error);
        });
    }
    $scope.load_user = function (accountId) {
        $http.get('/client/checkout/findUser/' + 1)
            .then(function (response) {
                $scope.user = response.data;
                console.log($scope.user);
            })
            .catch(function (error) {
                console.error('User is not found:', error);
            });
    }
    // Update address
    $scope.updateCityAndDistrict = function (accountId) {
        var updateUser = {
            addressCity: $scope.selectedCity,
            addressDistrict: $scope.selectedDistrict
        };

        // Gọi API hoặc phương thức để cập nhật thông tin người dùng
        $http.put('/client/checkout/updateUser/' + 1, updateUser)
            .then(function (response) {
                // Cập nhật thông tin người dùng trong scope
                $scope.user.addressCity = $scope.selectedCity;
                $scope.user.addressDistrict = $scope.selectedDistrict;

                // Ẩn form chỉnh sửa
                $scope.showSelects = false;
            })
            .catch(function (error) {
                console.error('Error updating user:', error);
            });
    };


    $scope.load_districts = function () {
        var url = `/client/checkout/district`;
        $http.get(url).then(resp => {
            $scope.districts = resp.data;
            $scope.selectedDistrict = $scope.districts[0];
            console.log("Success district", resp);
        }).catch(error => {
            console.log("Error", error);
        });
    }

    $scope.toggleSelects = function () {
        $scope.showSelects = !$scope.showSelects;
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


    $scope.cart = {
        items: [],
        itemCount: 0,
        totalAmount: 0, // Thêm biến để lưu tổng giá

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
        calculateTotalAmount() {
            $scope.cart.totalAmount = 0;
            for (var i = 0; i < this.items.length; i++) {
                var item = this.items[i];
                $scope.cart.totalAmount += item.item.price * item.quantity;
            }
        },

    }
    $scope.ship = [];
	$scope.shipfee = function() {
		
    	var header = {
        	"token": "d6f64767-329b-11ee-af43-6ead57e9219a",
        	"shop_id": "4421897"
    	};
    	var body = {
        	"service_id": 53320,
        	"insurance_value": 70000,
        	"coupon": null,
        	"from_district_id": 1572,
        	"to_district_id": 1572,
        	"to_ward_code": null,
        	"height": 15,
        	"length": 15,
        	"weight": 1000,
        	"width": 15
    	};
		
    	$http.post('https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee', body, { headers: header })
        	.then(response => {
            	$scope.ship = response.data;
            	console.log($scope.user.addressDistrict.addressDistrictId);
        	})
        	.catch(error => {
            	console.error(error);
        	});
		};

        // Tính hóa đơn
    $scope.dataCheckout = {};
    $scope.invoices = function(){
        var checkoutData = {
            // shipfee2 : ($scope.ship.data.total),
            totalAmount : parseFloat ($scope.cart.totalAmount)
        };
        var selectedMethod = document.querySelector('input[name="paymentMethod"]:checked').value;
            if (selectedMethod === "method1") {
                $http.post('/client/checkout/add-invoice/methodCOD', checkoutData)
                .then(function (response) {
                    $scope.alertSuccess("Đã đặt hàng!!!")
                    console.log($scope.ship.data.total);
                }).catch(function (error) {
                        console.error('Error updating user:', error);
                    });
        } else if (selectedMethod === "method2") {
            $http.post('/client/checkout/add-invoice/methodBanking', checkoutData)
		.then(function (response) {
			console.log('Oke');
		}).catch(function (error) {
                console.error('Error updating user:', error);
            });
        }
		
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
    $scope.cart.loadCartItems();
    $scope.loadCategories();
    $scope.load_user();
    $scope.load_citys();
    $scope.load_districts();
    $scope.shipfee();
});
