<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<t:templateAdmin>
    <jsp:attribute name="title">
		<spring:message code="nav.admin.twitter" />
    </jsp:attribute>

	<jsp:attribute name="headline">
		<spring:message code="nav.admin.twitter" />
	</jsp:attribute>
	
	<jsp:attribute name="breadcrumb">
		<li class="active"><spring:message code="nav.admin.twitter" /></li>
	</jsp:attribute>

  <jsp:body>

    <div class="col-sm-6">
      <t:panelWithHeading>
        <jsp:attribute name="heading">
            Fetch tweets
        </jsp:attribute>

        <jsp:body>
          <t:plainForm action="">
            <div class="form-group">
              <label for="panelHashtags" class="col-sm-3 control-label">Query:* </label>
              <div class="col-sm-9">
                <input type="text" class="form-control" id="panelHashtags" name="hashtags" required="required">
                <span class="help-block">See query specification at <a href="https://dev.twitter.com/rest/public/search">https://dev.twitter.com/rest/public/search</a></span>
              </div>
            </div>
            <div class="form-group">
              <label for="panelTwitterSinceId" class="col-sm-3 control-label">Since id:* </label>
              <div class="col-sm-9">
                <input type="number" class="form-control" id="panelTwitterSinceId" name="sinceId" placeholder="Tweets with status ids greater than the given id">
              </div>
            </div>
            <div class="form-group">
              <label for="panelTwitterMaxId" class="col-sm-3 control-label">Max id:* </label>
              <div class="col-sm-9">
                <input type="number" class="form-control" id="panelTwitterMaxId" name="maxId" placeholder="Tweets with status ids less than the given id">
              </div>
            </div>
            <div class="form-group">
              <label for="panelTwitterPages" class="col-sm-3 control-label">Pages:* </label>
              <div class="col-sm-9">
                <input type="number" class="form-control" id="panelTwitterPages" name="pages" placeholder="Number of pages">
              </div>
            </div>
            <div class="form-group">
              <label for="panelTwitterCount" class="col-sm-3 control-label">Max count:* </label>
              <div class="col-sm-9">
                <input type="number" class="form-control" id="panelTwitterCount" name="count" placeholder="Number of tweets per page">
              </div>
            </div>
            <div class="form-group">
              <div class="col-sm-offset-3 col-sm-9">
                <button type="submit" class="btn btn-default">Fetch tweets</button>
              </div>
            </div>
          </t:plainForm>
        </jsp:body>
      </t:panelWithHeading>
    </div>


  </jsp:body>
</t:templateAdmin>