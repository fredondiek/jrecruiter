<title>Job List</title>
<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp"%>

<s:url id="tableUrl" includeParams="none"/>
<s:url id="tableUrlAjax" action="show-jobs-ajax" includeParams="none"/>
<s:form name="jobsForm" action="show-jobs-ajax">
    <s:hidden name="backTo"/>
    <div id="jobs">
        <jsp:include page="jobsTable.jsp"></jsp:include>
    </div>

  <script type="text/javascript">

  function onInvokeAction(id) {
      setExportToLimit(id, '');

      var parameterString = createParameterStringForLimit(id);

      jQuery.get('<s:property value="%{#tableUrlAjax}"/>?ajax=true&' + parameterString, function(data) {
          jQuery("#jobs").html(data);
      });
  }
  </script>

  <table id="socialinks">
    <tr>
      <td>
        <a href='http://del.icio.us/post?url=http://www.ajug.org/jrecruiter/showJobs.html&title=AJUG+Job+Posting+Service' target='_blank'><img border='0' src='images/icons/delicious.png' alt='del.icio.us' title='del.icio.us' /></a>
      </td>
      <td>
        <a href='http://www.stumbleupon.com/submit?url=http://www.ajug.org/jrecruiter/showJobs.html&amp;title=AJUG+Job+Posting+Service' target='_blank'><img border='0' src='images/icons/stumbleupon.png' alt='stumbleupon' title='stumbleupon' /></a>
      </td>
      <td>
        <a href='http://www.google.com/bookmarks/mark?op=add&amp;hl=de&amp;bkmk=http://www.ajug.org/jrecruiter/showJobs.html&amp;annotation=&amp;labels=&amp;title=AJUG+Job+Posting+Service' target='_blank'><img border='0' src='images/icons/google.png' alt='google' title='google' /></a>
      </td>
      <td>
        <a href='http://myweb2.search.yahoo.com/myresults/bookmarklet?u=http://www.ajug.org/jrecruiter/showJobs.html&t=AJUG+Job+Posting+Service' target='_blank'><img border='0' src='images/icons/yahoomyweb.png' alt='YahooMyWeb' title='YahooMyWeb' /></a>
      </td>
    </tr>
  </table>

  <div class="submit">
      <s:submit value="Back" method="cancel"/>
  </div>
</s:form>