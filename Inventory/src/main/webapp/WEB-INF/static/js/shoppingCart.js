/**
 * 
 */

var shoppingCartApp = angular.module('shoppingCartApp', ['ngMaterial']);

shoppingCartApp.controller('shoppingCartController', function($scope, $mdDialog, $http) {
	$http({
        method : 'GET',
        url : 'http://localhost:8080/ArtanisInventory/products/all',
        headers : {
        	'Accept' : 'application/json'
        }
    }).then(function successCallback(response) {
        $scope.products = response.data.data;
        $scope.boughtItems = new Array($scope.products.length);
        $scope.values = new Array($scope.products.length);
        for (i = 0; i < $scope.products.length; i++) {
        	$scope.boughtItems[i] = {
    			itemCode: $scope.products[i].itemCode,
    			name: $scope.products[i].name,
    			quantity: 0
    			};
  	    }
        
    }, function errorCallback(response) {
        console.log(response.statusText);
    });
		
	$scope.doPurchase = function(event) {
		$scope.erroneousPurchases = [];
		for (i = 0; i < $scope.products.length; i++) {
			if ($scope.boughtItems[i].quantity < 0
					|| $scope.products[i].quantityOnHand < $scope.boughtItems[i].quantity) {
				$scope.erroneousPurchases.push({
					name : $scope.products[i].name,
					brand : $scope.products[i].brand,
					quantity : $scope.boughtItems[i].quantity,
					quantityAvailable : $scope.products[i].quantityOnHand
				});
			}
		}
		
		if ($scope.erroneousPurchases.length > 0) {
			//show Error Purchases
			$mdDialog.show ({
	        	controller: DialogController,
	        	scope: $scope,
	        	preserveScope: true,
	        	templateUrl: 'template_error.html',
	        	parent: angular.element(document.body),
	        	clickOutsideToClose: true,
	        	targetEvent: event,
	        });
		} else {
			//show Purchase Summary
			$mdDialog.show ({
	        	controller: DialogController,
	        	scope: $scope,
	        	preserveScope: true,
	        	templateUrl: 'template.html',
	        	parent: angular.element(document.body),
	        	clickOutsideToClose: true,
	        	targetEvent: event,
	        }).then(function(answer) {
	            //$scope.purchaseStatus = answer;
	        	$scope.buyItems();
	        }, function() {
	        	$scope.purchaseStatus = 'cancelled';
	        	console.log("Purchase is " + $scope.purchaseStatus + "!");
	        });
		}
     };
	
	$scope.buyItems = function() {
		$http({
	        method : 'POST',
	        url : 'http://localhost:8080/ArtanisInventory/products/buy',
	        headers : {
	        	'Accept' : 'application/json',
	        	'Content-Type' : 'application/json'
	        },
	        data : angular.toJson($scope.boughtItems)
	    }).then(function successCallback(response) {
	    	oldBoughtItems = $scope.boughtItems;
	    	
	        $scope.products = response.data.data;
	        $scope.boughtItems = new Array($scope.products.length);
	        $scope.values = new Array($scope.products.length);
	        for (i = 0; i < $scope.products.length; i++) {
	        	$scope.boughtItems[i] = {
	    			itemCode: $scope.products[i].itemCode,
	    			name: $scope.products[i].name,
	    			quantity: oldBoughtItems[i].quantity
	    			};
	  	    }
	        
	    }, function errorCallback(response) {
	        console.log(response.statusText);
	    });
	};
	
	function DialogController($scope, $mdDialog) {
	    $scope.hide = function() {
	      $mdDialog.hide();
	    };

	    $scope.cancel = function() {
	      $mdDialog.cancel();
	    };

	    $scope.answer = function(answer) {
	      $mdDialog.hide(answer);
	    };
	  }
});



