<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="true" %>
<label for="${name}" class="col-sm-3 control-label">${label}: </label>

<div class="col-sm-9">
    <input type="file" class="form-control" name="${name}" id="${name}" accept=".csv">
</div>