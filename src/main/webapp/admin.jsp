<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
   <!-- Material Design fonts -->
   <link rel="stylesheet" type="text/css" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700">
   <link rel="stylesheet" type="text/css" href="https://fonts.googleapis.com/icon?family=Material+Icons">

   <!-- Bootstrap -->
   <link rel="stylesheet" type="text/css" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <!-- Bootstrap Material Design -->
  <link rel="stylesheet" type="text/css" href="dist/css/bootstrap-material-design.min.css">
  <link rel="stylesheet" type="text/css" href="dist/css/ripples.min.css">

   <!-- Sidebar - Navigation -->
   <link rel="stylesheet" type="text/css" href="css/simple-sidebar.css">

   <!-- Dropdown.js -->
  <link href="https://cdn.rawgit.com/FezVrasta/dropdown.js/master/jquery.dropdown.css" rel="stylesheet">

  <!-- jQuery -->
  <script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
  
  <script src="./js/admin.js"></script>

</head>
<body>
<div id="wrapper">

        <!-- Sidebar -->
        <div id="sidebar-wrapper">
         <ul class="nav nav-pills nav-stacked" style="max-width: 300px;">
           <li><a href="javascript:void(0)">Home</a></li>
           <li class="active"><a href="javascript:void(0)">Admin Screen</a></li>
           <li class="disabled"><a href="javascript:void(0)">Disabled</a></li>
         </ul>
        </div>
        <!-- /#sidebar-wrapper -->

        <!-- Page Content -->
        <div id="page-content-wrapper">
            <div class="navbar navbar-inverse">
              <div class="container-fluid">
                <div class="navbar-header">
                  <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-inverse-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                  </button>
                  <a class="navbar-brand" href="javascript:void(0)">Brand</a>
                </div>
                <div class="navbar-collapse collapse navbar-inverse-collapse">
                  <ul class="nav navbar-nav">
                    <li id="menu-toggle" class="active"><a href="javascript:void(0)">Menu Toggle</a></li>
                    <li><a href="javascript:void(0)">Link</a></li>
                    <li class="dropdown">
                      <a href="bootstrap-elements.html" data-target="#" class="dropdown-toggle" data-toggle="dropdown" id="dropdownLabel">View Tables
                        <b class="caret"></b></a>
                      <ul class="dropdown-menu">
                        <li><a href="javascript:void(0)" onclick="load_data('employees')">Employees</a></li>
                        <li><a href="javascript:void(0)" onclick="load_data('worked_shifts')">Worked Shifts</a></li>
                        <li><a href="javascript:void(0)" onclick="load_data('breaks')">Breaks</a></li>
                        <li><a href="javascript:void(0)" onclick="load_data('companies')">Companies</a></li>
                        <li><a href="javascript:void(0)" onclick="load_data('location')">Location</a></li>
                        <li><a href="javascript:void(0)" onclick="load_data('positions')">Positions</a></li>
                        <li><a href="javascript:void(0)" onclick="load_data('scheduled_shifts')">Scheduled Shifts</a></li>
                        <li><a href="javascript:void(0)" onclick="load_data('system')">System</a></li>
                        <li><a href="javascript:void(0)" onclick="load_data('employee_locations')">Employee Locations</a></li>
                        <li><a href="javascript:void(0)" onclick="load_data('employee_positions')">Employee Positions</a></li>
                      </ul>
                    </li>
                  </ul>
                  <form class="navbar-form navbar-left">
                    <div class="form-group">
                      <input type="text" class="form-control col-md-8" placeholder="Search">
                    </div>
                  </form>
                  <ul class="nav navbar-nav navbar-right">
                    <li><a href="javascript:void(0)">Link</a></li>
                  </ul>
                </div>
              </div>
            </div>

            <!-- NAV BAR END -->
            <h2 id="tableTitle">Employees Table</h2>       
               <!-- Table Skeleton - Dynamically loaded with Ajax -->
               <table id="adminTable" class="table table-striped table-hover ">
                <thead></thead>
              	<tbody></tbody>
               </table>
        </div>
        <!-- /#page-content-wrapper -->

    </div>
    <!-- /#wrapper -->
    
    <div class="modal" id="editModal">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
	        <h2 class="modal-title">Edit</h2>
	      </div>
	      <div class="modal-body">
			  <fieldset id="modal-body-id">
			    <!-- Dynamically adding label and input field(s) -->
			  </fieldset>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-primary">Save changes</button>
	      </div>
	    </div>
	  </div>
	</div>
    
    
<!-- 

   SCRIPTS 

         -->

    <!-- Menu Toggle Script -->
    <script>
    $("#menu-toggle").click(function(e) {
        e.preventDefault();
        $("#wrapper").toggleClass("toggled");
    });
    </script>

<!-- Twitter Bootstrap -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.6/js/bootstrap.min.js"></script>

<!-- Material Design for Bootstrap -->
<script src="dist/js/material.min.js"></script>
<script src="dist/js/ripples.min.js"></script>
<script>
  $.material.init();
</script>


</body>
</html>