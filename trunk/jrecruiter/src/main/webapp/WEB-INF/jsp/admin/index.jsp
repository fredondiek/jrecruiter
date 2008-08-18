<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp"%>
    <title>jRecruiter - Administrative Area</title>
    <s:text name="admin.main.welcome"/>
    <security:authentication property="principal.firstName"/>
     <security:authentication property="principal.lastName"/>!

<style>
    div fieldset { margin-right: 20em;}
</style>

    <div class="adminMain">

          <s:url action="edit-user"             id="editUserUrl"/>
          <s:url action="show-jobs"             id="editJobUrl"/>
          <s:url action="add-job"               id="addJobUrl"/>
          <s:url action="show-statistics"       id="showStatisticsUrl"/>
          <s:url action="show-users"            id="userListUrl"/>
          <s:url action="add-user"              id="addUserUrl"/>
          <s:url action="edit-settings"         id="editSettingsUrl"/>
          <s:url action="add-user"              id="showJobsUrl">
            <s:param name="show-jobs" value="true"/>
          </s:url>
          <s:url action="logout" id="logoutUrl"/>
          <s:url action="search-index" id="searchIndexUrl"/>
          <s:url action="setup-demo"            id="setupDemoUrl"/>
          <s:url action="logging" id="loggingUrl"/>
      <fieldset>
          <legend>Manage Jobs Postings</legend>
                <ul>
                    <li><a href="${addJobUrl}" ><s:text name="admin.main.label.add.job"/></a></li>
                    <li><a href="${editJobUrl}" ><s:text name="admin.main.label.edit.jobs"/></a></li>
                    <li><a href="${showStatisticsUrl}" ><s:text name="admin.main.label.show.statistics"/></a></li>
                    <li><a href="${showJobsUrl}" ><s:text name="admin.main.label.view.jobs"/></a></li>
                </ul>
      </fieldset>
      <fieldset>
                <legend>User Task</legend>
                <ul>
                    <li><a href="${editUserUrl}" ><s:text name="admin.main.label.edit.registration"/></a></li>
                    <li><a href="${logoutUrl}" ><s:text name="admin.main.label.logout"/></a></li>
                </ul>
            </fieldset>
            <security:authorize ifAllGranted="ADMIN">
              <fieldset>
                  <legend>jRecruiter Administration</legend>
                  <ul>
            <li><a href="${userListUrl}" ><s:text name="admin.main.label.edit.user"/></a></li>
            <li><a href="${addUserUrl}" ><s:text name="admin.main.label.add.user"/></a></li>
            <li><a href="${editSettingsUrl}" ><s:text name="admin.main.label.edit.settings"/></a></li>
            <li><a href="${searchIndexUrl}" >Re-Index Search Index</a></li>
            <li><a href="${setupDemoUrl}" >Setup Demo Jobs</a></li>
            <li><a href="${loggingUrl}" >Logging Settings</a></li>
                  </ul>
              </fieldset>
            </security:authorize>
    </div>

