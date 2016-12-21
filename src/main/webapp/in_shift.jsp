<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@include file="./common/header.jsp" %>
	
</head>
<body>

<!-- Page Container -->
<div class="page-container">

  <!-- The Grid -->
  <div class="middle-column">
    <!-- Three rows -->
    <div class="one-quarter-height">
      <div class="center">
        <img class="format-image" src="images/mario_face.png" alt="Employee Profile">
        <div class="format-profile-name">
          IN SHIFT PAGE HERE
        </div>
      </div>
    </div>
    <div class="one-half-height">
      <div class="display-row">
        <div class="one-half-width">
          <div class="center">
            <div class="previous-shift">
              <div class="shift-row">
                <div class="shift-date">
                  Friday Oct. 28th
                </div>
                <div class="shift-hours">
                  10:00 pm - 10:00 pm
                </div>
              </div>
            </div>           
          </div>
        </div>
        <div class="one-half-width">
          <div class="center">
            <div class="position-button">
              <button type="button" id="button_breakin">Break in.</br> <%= new java.util.Date().toString() %></button>
              <button type="button" id="button_clockout">Clock OUT</br> <%= new java.util.Date().toString() %></button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="one-quarter-height">
      <div class="center">
  		<div class="position-button">
          <button type="button" id="button_addshiftnotes">Save note changes</button>
          <textarea id="shift-notes"><i>Loading shift notes...</i></textarea>
        </div>
      </div>
    </div>

    
          
  <!-- End Grid -->
  </div>
  
  <!-- End Page Container -->
</div>

<%@ include file="./common/footer.jsp" %>
<script src="./js/add_shift_note.js"></script>
<script src="./js/in_shift.js"></script>

</body>
</html>