$(function() {
	// solving Active Menu problem
	switch (menu) {
	case 'About Us':
		$('#about').addClass('active');
		break;
	case 'Contact Us':
		$('#contact').addClass('active');
		break;
	case 'View Products':
		$('#listProducts').addClass('active');
		break;
	default:
		if(menu == "Home") break;
		$('#listProducts').addClass('active');
		$('#a_'+menu).addClass('active');
		break;
	}
	
	// Code for jQuery DataTable
	// Create a dataset
	var products = [['1', 'ABC'],
		['2', 'DEF'],
		['3', 'GHI'],
		['4', 'JKL'],
		['5', 'MNO'],
		['6', 'PQR'],
		['7', 'STU'],
		['8', 'VWX']];
	
	var $table = $('#productListTable');
	
	// Execute the below code only where this table is present
	if($table.length) {
		$table.DataTable({
			lengthMenu : [[2,5,10,-1], ['2 Records','5 Records','10 Records','All Records']],
			pageLength : 5,
			data : products
		});
	}
	
});