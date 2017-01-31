<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<%@ include file="./common/header.jsp" %>

</head>

<body>
<div id="wrapper">

   <%@ include file="./common/navbar.jsp" %>
            
   <!-- Sidebar -->
   <div id="sideMenu" class="col-sm-2">
	  <div class="sidebar-nav"><a href="#" data-toggle="collapse" data-target="#viewMenu">Views <i class="glyphicon glyphicon-chevron-down"></i></a>
	    <ul class="nav nav-stacked collapse in" id="viewMenu">
	        <li class="active"><a href="./admin.jsp"><i class="material-icons">developer_board</i> Database Manager</a></li>
	        <li><a href="./admin.jsp"><i class="material-icons">group</i> Staff List</a></li>
	    </ul>
	  </div>
	  <div class="sidebar-nav"><a href="#" data-toggle="collapse" data-target="#reports"> Reports <i class="glyphicon glyphicon-chevron-right"></i></a>
	    <ul class="nav nav-stacked collapse" id="reports">
	        <li><a href="#">Create</a></li>
	        <li><a href="#">Views</a></li>
	    </ul>
	  </div>    
   </div>
   <!-- /#sidebar-wrapper -->

<div id="mainContent" class="col-sm-10">
	<ul class="nav navbar-nav">
       <li class="dropdown">
         <a href="bootstrap-elements.html" data-target="#" class="dropdown-toggle" data-toggle="dropdown" id="dropdownLabel"><h3>EMPLOYEES Tables
           <b class="caret"></b></h3></a>
         <ul class="dropdown-menu">
           <li><a href="#" onclick="load_data('employees')">Employees</a></li>
           <li><a href="#" onclick="load_data('breaks')">Breaks</a></li>
           <li><a href="#" onclick="load_data('companies')">Companies</a></li>
           <li><a href="#" onclick="load_data('location')">Location</a></li>
           <li><a href="#" onclick="load_data('positions')">Positions</a></li>
           <li><a href="#" onclick="load_data('scheduled_shifts')">Scheduled Shifts</a></li>
           <li><a href="#" onclick="load_data('system')">System</a></li>
           <li><a href="#" onclick="load_data('employee_locations')">Employee Locations</a></li>
           <li><a href="#" onclick="load_data('employee_positions')">Employee Positions</a></li>
         </ul>
       </li>
    </ul>
    <div class="add-button">
    	<a id="addButton" href="javascript:void(0)" class="btn btn-primary btn-fab"><i class="material-icons">add</i></a>
    </div>
    <!-- Table Skeleton - Dynamically loaded with Ajax -->
    <table id="adminTable" class="table table-striped table-hover ">
      <thead></thead>
  	  <tbody></tbody>
    </table>
</div>
<!-- /.mainContent -->

</div>
<!-- /#wrapper -->

<!-- Edit Modal -->
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
        <button id="saveChangesButton" type="button" class="btn btn-primary">Save changes</button>
      </div>
    </div>
  </div>
</div> 
<!-- END Edit Modal -->
<!-- Success Modal -->
<div class="modal" id="successAlert">
  <div style="width: 250px; text-align: center"class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h2><span class="label label-primary">Success</span></h2>
      </div>
      <div class="modal-body">
      	<b>Database updated!</b>
      </div>
    </div>
  </div>
</div>
<!-- END Success Modal -->
<!-- Delete Modal -->
<div class="modal" id="deleteModal">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h2><span class="label label-danger">Delete</span></h2>
      </div>
      <div class="modal-body">
      	Warning - This action <b>permanently</b> deletes the selected record.
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button id="deleteButton" type="button" class="btn btn-danger">Delete</button>
      </div>
    </div>
  </div>
</div>
<!-- END Delete Modal -->
<!-- Add Modal -->
<div class="modal" id="addModal">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
        <h2 class="modal-title">Add</h2>
      </div>
      <div class="modal-body">
		  <fieldset id="add-modal-body">
		    <!-- Dynamically adding label and input field(s) -->
		  </fieldset>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button id="addChangesButton" type="button" class="btn btn-primary">Save changes</button>
      </div>
    </div>
  </div>
</div> 
<!-- END Add Modal -->
    
<%@ include file="./common/footer.jsp" %>
<script src="./js/menu.js"></script>
<script src="./js/admin.js"></script>

</body>
</html>