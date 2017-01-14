<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@include file="header_includes.jsp" %>
	<script src="./js/clock_in.js"></script>
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
          Super Mario
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
            <div class="active-shift">
              <div class="shift-row">
                <div class="shift-date">
                  Saturday Oct. 29th
                </div>
                <div class="shift-hours">
                  3:00 pm - 10:00 pm
                </div>
              </div>
            </div>
            <div class="future-shift">
              <div class="shift-row">
                <div class="shift-date">
                  Sunday Oct. 30th
                </div>
                <div class="shift-hours">
                  3:00 pm - 3:00 pm
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="one-half-width">
          <div class="center">
            <div class="position-button">
              <button type="button" id="button_clockin">Clock in </br> <%= new java.util.Date().toString() %></button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="one-quarter-height">
      <div class="center">
        Other information
      </div>
    </div>

    
          
  <!-- End Grid -->
  </div>
  
  <!-- End Page Container -->
</div>

</body>
</html>