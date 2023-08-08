// Đảm bảo rằng bạn đã bao gồm thư viện AngularJS trong dự án của mình
// Ví dụ: <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.2/angular.min.js"></script>

var app = angular.module('index-app', []);

app.controller('index-controller', function($scope, $http) {
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
        onClick: function () {}
    }).showToast();
};
  $scope.cart.loadCartItems();
});
















