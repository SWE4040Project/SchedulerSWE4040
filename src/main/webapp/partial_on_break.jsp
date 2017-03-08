<div class="panel-heading">
   <h3 class="panel-title">On Break Status</h3>
</div>
<div class="panel-body">
   <div class="centered">
   	<legend>Clocked In.</legend>
  	</div>
	<div class="list-group">
		<div id="noShiftMessage">
			<div class="list-group-item">
				<div class="row-action-primary">
					<i id="cross-icon" class="btn-primary material-icons">error</i>
				</div>
				<div class="row-content">
					<div data-toggle="tooltip" data-placement="left" title="" data-original-title="Displays the current shift you are clocked in for." class="action-secondary"><i class="material-icons">info</i></div>
					<p>No current shifts.</p>
				</div>
			</div>
		</div>
		<div id="shift_id">
			<div class="list-group-item">
				<div class="row-action-primary">
					<i id="check-icon" class="btn-primary material-icons">check</i>
				</div>
				<div class="row-content">
					<div data-toggle="tooltip" data-placement="left" title="" data-original-title="Displays the current shift you are clocked in for." class="action-secondary"><i class="material-icons">info</i></div>
					<h4 class="list-group-item-heading">Shift</h4>
					<p id="start_time" class="list-group-item-text">Clocked in: 1:58 pm Monday, Jan 26th</p>
					<p id="end_time" class="list-group-item-text">Clocked out: -- </p>
				</div>
			</div>
			<div class="list-group-separator"></div>
			<br>
			<div class="list-group-item">
				<div class="col-md-9">
					<p id="scheduled_shift" class="list-group-item-text">Shift: 2 pm - 10 pm</p>
					<p id="scheduled_date" class="list-group-item-text"> Monday, Jan 26th</p>
				</div>
				<div class="col-md-3">
					<b id="current_time">4:24 pm</b>
				</div>
				<div class="row-action-primary">
					<br>
				</div>
				<div class="col-md-12">
					<div class="progress">
						<div class="progress-bar progress-bar-primary"></div>
					</div>
				</div>
			</div>
			<div class="list-group-item">
				<div class="row-action-primary">
					<i class="material-icons">note</i>
				</div>
				<div class="row-content">
					<textarea id="shift_notes" class="list-group-item-text">
					</textarea>
					<button id="button_addshiftnotes" class="btn btn-secondary">Update Notes</button>
				</div>
			</div>
		</div> <!-- shift_id end -->
	</div>
	<div id="errorMessage">
		<div class="panel panel-danger notifications">
			<div class="panel-heading centered">
				<div id="errorText">
					<i class="material-icons notifications">error</i> Clock-in did not succeed. Please logout and try again.
				</div>
			</div>
		</div>
	</div>

   <div class="form-group">
     <div class="centered col-md-12">
       <button id="button_breakout" class="btn btn-info">End Break</button>
     </div>
   </div>
</div>

<script src="./js/on_break.js"></script>
<script src="./js/add_shift_note.js"></script>