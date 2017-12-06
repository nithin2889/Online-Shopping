$(function() {
	// solving Active Menu problem
	// The "menu" variable is set in the JavaScript window object. This provides the title
	switch (menu) {
	case 'About':
		$('#about').addClass('active');
		break;
	case 'Contact':
		$('#contact').addClass('active');
		break;
	case 'View Products':
		$('#listProducts').addClass('active');
		break;
	case 'Manage Products':
		$('#manageProducts').addClass('active');
		break;
	case 'User Cart':
		$('#userCart').addClass('active');
		break;
	default:
		if (menu == "Home")
			break;
		$('#listProducts').addClass('active');
		$('#a_' + menu).addClass('active');
		break;
	}
	
	//-------------------------------------------------------------------------------------------
	// To tackle the CSRF token 403 forbidden error while activating and deactivating the product
	//-------------------------------------------------------------------------------------------
	var token = $('meta[name="_csrf"]').attr('content');
	var header = $('meta[name="_csrf_header"]').attr('content');
	
	if(token.length > 0 && header.length > 0) {
		// set the token for AJAX request
		$(document).ajaxSend(function(e, xhr, options) {
			xhr.setRequestHeader(header, token);
		});
	}
	//---------------------------

	//---------------------------
	// Code for jQuery data table
	//---------------------------
	var $table = $('#productListTable');

	// Execute the below code only where this table is present
	if ($table.length) {

		// Code for jQuery DataTable
		var jsonUrl = '';
		if (window.categoryId == '') {
			jsonUrl = window.contextRoot + '/json/data/all/products';
		} else {
			jsonUrl = window.contextRoot + '/json/data/category/'
					+ window.categoryId + '/products';
		}

		$table.DataTable({
			lengthMenu : [
					[ 3, 5, 10, -1 ],
					[ '3 Records', '5 Records', '10 Records',
							'All Records' ] ],
			pageLength : 5,
			ajax : {
				url: jsonUrl,
				dataSrc: ''
			},
			columns : [ {
				data : 'code',
				mRender : function(data, type, row) {
					return '<img src="'+window.contextRoot+'/resources/images/'+data+'.jpg" class="dataTableimg" />';
				}
			}, {
				data : 'name'
			}, {
				data : 'brand'
			}, {
				data : 'unitPrice',
				mRender : function(data, type, row) {
					return '&#8377;' + data;
				}
			}, {
				data : 'quantity',
				mRender : function(data, type, row) {
					if(data < 1) {
						return '<span style="color:red">Out of stock!</span>';
					} 
					return data;
				}
			}, {
				data : 'id',
				bSortable : false,
				mRender : function(data, type, row) {
					var str = '';
					str += '<a href="'+window.contextRoot+'/show/'+data+'/product" class="btn btn-primary"><span class="glyphicon glyphicon-eye-open"></span></a> &#160;';
					
					if(userRole == 'ADMIN') {
						str += '<a href="'+window.contextRoot+'/manage/'+data+'/product" class="btn btn-warning"><span class="glyphicon glyphicon-pencil"></span></a>';
					} else {
						if(row.quantity < 1) {
							str += '<a href="javascript:void(0)" class="btn btn-success disabled"><span class="glyphicon glyphicon-shopping-cart"></span></a>';
						} else {
							str += '<a href="'+window.contextRoot+'/cart/add/'+data+'/product" class="btn btn-success"><span class="glyphicon glyphicon-shopping-cart"></span></a>';
						}
					}
					return str;
				}
			}]
		});
	}

	// dismissing the alert after 3 seconds
	var $alert = $('.alert');
	
	if($alert.length) {
		setTimeout(function(){
			$alert.fadeOut('slow');
		}, 3000);
	}
	
	// -------------------------------------
	
	$('.switch input[type="checkbox"]').on('change', function() {
		// checkbox reference
		var checkbox = $(this);
		// checking whether the checkbox is checked or not
		var checked = checkbox.prop('checked');
		// getting the checkbox value
		var value = checkbox.prop('value');
		// the message to be displayed
		var dialogMsg = (checked) ? 'Do you want to activate the product?' : 'Do you want to deactivate the product?';
		
		// bootbox confirm method calling
		bootbox.confirm({
			size: 'medium',
			title: 'Product Activation & Deactivation',
			message: dialogMsg,
			// callback method to check whether the user clicked OK or Cancel
			callback: function(confirmed) {
				if(confirmed) {
					// User clicked OK so unchecking the toggle button
					console.log(value);
					bootbox.alert({
						size: 'medium',
						title: 'Information',
						message: 'You are making changes on product ' + value
					});
				} else {
					// User clicked Cancel so checking the toggle button back
					checkbox.prop('checked', !checked);
				}
			}
		});
	});
	
	//---------------------
	// Data Table for Admin
	//---------------------
	var  $adminProductsTable = $('#adminProductsTable');

	// Execute the below code only where this table is present
	if ($adminProductsTable.length) {
	    //console.log('Inside the table!');
	    var jsonUrl = window.contextRoot + '/json/data/admin/all/products';
	    $adminProductsTable.DataTable({

	        lengthMenu: [[10, 30, 50, -1], ['10 Records', '30 Records', '50 Records', 'ALL']],
	        pageLength: 30,
	        ajax: {
	            url: jsonUrl,
	            dataSrc: ''
	        },
	        columns: [
	            {
	                data: 'id'
	            },
	            {
	                data: 'code',
	                bSortable: false,
	                mRender: function (data, type, row) {
	                    return '<img src="' + window.contextRoot + '/resources/images/' + data + '.jpg" class="adminDataTableImg"/>';
	                }
	            },
	            {
	                data: 'name'
	            },
	            {
	                data: 'brand'
	            },
	            {
	                data: 'quantity',
	                mRender: function (data, type, row) {
	                    if (data < 1) {
	                        return '<span style="color:red">Out of Stock!</span>';
	                    }
	                    return data;
	                }
	            },
	            {
	                data: 'unitPrice',
	                mRender: function (data, type, row) {
	                    return '&#8377; ' + data
	                }
	            },
	            {
	                data: 'active',
	                bSortable: false,
	                mRender: function (data, type, row) {
	                    var str = '';
	                    str += '<label class="switch">';
	                    if (data) {
	                        str += '<input type="checkbox" checked="checked" value="' + row.id + '" />';
	                    }
	                    else {
	                        str += '<input type="checkbox" value="' + row.id + '" />';
	                    }
	                    str += '<div class="slider"></div></label>';
	                    return str;
	                }
	            },
	            {
	                data: 'id',
	                bSortable: false,
	                mRender: function (data, type, row) {
	                    var str = '';
	                    str += '<a href="'+window.contextRoot+'/manage/' + data + '/product" class="btn btn-warning">';
	                    str += '<span class="glyphicon glyphicon-pencil"></span></a>';
	                    return str;
	                }
	            }
	        ],
	        initComplete: function () {
	            var api = this.api();
	            api.$('.switch input[type="checkbox"]').on('change', function () {
	                var checkbox = $(this);
	                var checked = checkbox.prop('checked');
	                var dMsg = (checked) ? 'Do you want to activate the product?' :
	                    'Do you want to deactivate the product?';
	                var value = checkbox.prop('value');
	                bootbox.confirm({
	                    size: 'medium',
	                    title: 'Product Activation & Deactivation',
	                    message: dMsg,
	                    callback: function (confirmed) {
	                        if (confirmed) {
	                        	console.log(value);
	                        	var activationUrl = window.contextRoot + '/manage/product/' + value + '/activation';
	                        	console.log(activationUrl);
	                        	$.post(activationUrl, function(data) {
	                        		bootbox.alert({
		                                size: 'medium',
		                                title: 'Information',
		                                message: data
		                            });
	                        	});
	                        }
	                        else {
	                            checkbox.prop('checked', !checked);
	                        }
	                    }
	                });
	            });
	        }
	    });
	}
	
	//---------------------
	// Validation code for category
	var $categoryForm = $('#categoryForm');
	if($categoryForm.length) {
		$categoryForm.validate({
			rules : {
				name : {
					required : true,
					minlength : 2
				},
				description : {
					required : true
				}
			},
			messages : {
				name : {
					required : "Please add the category name!",
					minlength : "The category name should not be less than 2 characters"
				},
				description : {
					required : "Please add a description for this category!"
				}
			},
			errorElement : 'em',
			errorPlacement : function(error, element) {
				// Add the class of help-block
				error.addClass('help-block');
				// Dynamically adds the error element after the input element
				error.insertAfter(element);
			}
		});
	}
	//----------------------
	
	
	
	//---------------------
	// Validation for login page
	var $loginForm = $('#loginForm');
	if($loginForm.length) {
		$loginForm.validate({
			rules : {
				username : {
					required : true,
					email : true
				},
				password : {
					required : true
				}
			},
			messages : {
				username : {
					required : "Please enter the username!",
					email : "Please enter valid email address"
				},
				password : {
					required : "Please enter the password!"
				}
			},
			errorElement : 'em',
			errorPlacement : function(error, element) {
				// Add the class of help-block
				error.addClass('help-block');
				// Dynamically adds the error element after the input element
				error.insertAfter(element);
			}
		});
	}
	//----------------------
	// Handling refresh cart button click event
	
	$('button[name="refreshCart"]').click(function() {
		// fetch the cart line id
		var cartLineId = $(this).attr('value');
		var countElement = $('#count_' + cartLineId);
		
		var originalCount = countElement.attr('value');
		var currentCount = countElement.val();
		
		// work only when the count has changed
		if(currenCount != originalCount) {
			console.log("Current Count" + currentCount);
			console.log("Original Count" + originalCount);
			
			if(currentCount < 1 || currentCount > 3) {
				// Reverting to the original count
				// User has given value below 1 or above 3
				countElement.val(originalCount);
				bootbox.alert({
					size: 'medium',
					title: 'error',
					message: 'Product count should be minimum 1 or maximum 3'
				});
			} else {
				// Correct product count specified, hence redirecting/forwarding the url to the controller
				var updateUrl = window.contextRoot + '/cart/' + cartLineId + '/update?count' + currentCount;
				// forwarding here
				window.location.href = updateUrl;
			}
		}
	});
	
	//----------------------
});