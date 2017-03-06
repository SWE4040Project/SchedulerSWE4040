<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@include file="./common/header.jsp" %>
	<link rel="stylesheet" type="text/css" href="css/main.css">
</head>
<body>
<div class="container">
  <div class="row vertical-offset-100">
	<div class="col-md-4 col-md-offset-4">
		<div class="panel panel-default">
			<div class="panel panel-info">
				  <div class="panel-heading">
				    <h3 class="panel-title">Clock In</h3>
				  </div>
				  	<div id="errorMessage">
				  		<div class="panel panel-danger notifications">
						  	<div class="panel-heading centered">
							  	<div>
				   	 				<i class="material-icons notifications">error</i> The information entered is not correct. Please try again.
				   	 			</div>
						   	</div>
						</div>
					</div>
					<div id="successMessage">
						<div class="panel panel-success notifications">
							<div class="panel-heading centered">
								<div>
									<i class="material-icons notifications">check</i> New User successfully created.
								</div>
							</div>
						</div>
					</div>
				<div id="duplicateUserMessage">
					<div class="panel panel-danger notifications">
						<div class="panel-heading centered">
							<div>
								<i class="material-icons notifications">error</i> User name already exists. Please try a new one.
							</div>
						</div>
					</div>
				</div>
	              <div class="panel-body">
	                  <form class="form-horizontal" method='POST'>
						  <fieldset>
						    <div class="form-group label-floating">
						      <label for="inputUsername" class="col-md-2 control-label"> Username</label>
						      <div class="col-md-10">
						     	 <input type="text" class="form-control" id="inputUsername" name="username" required>
						      </div>
						    </div>
						    <div class="form-group label-floating">
						      <label for="inputPassword" class="col-md-2 control-label"> Password</label>
						      <div class="col-md-10">
						        <input type="password" class="form-control" id="inputPassword" name="password" required>
						      </div>
						    </div>
						    <div class="form-group">
						      <div class="row-view">
							    <input type="button" class="btn btn-secondary" onclick="createNewEmployee()" value="Create New User">
							    <button type="submit" class="btn btn-primary">Login</button>
						      </div>
						    </div>
						  </fieldset>
						</form>
	              </div>
              </div>
          </div>
      </div>
  </div>
</div>

<%@ include file="./common/footer.jsp" %>
<script src="./js/login.js"></script>

</body>
</html>
