<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>
<table id="acceptedSubmissions" class="table table-hover table-striped">
    <tbody>
    <c:forEach var="submission" items="${submissions}" varStatus="status">
        <tr class="${status.index gt 4 ? 'additionalRow' : ''}" style="${status.index gt 4 ? 'display: none' : ''}">
            <td><img src="${submission.participant.contestParticipant.profilePictureUrl}" alt="${submission.participant.contestParticipant.fullname}" width="50" height="50"></td>
            <td>
                <strong>${submission.participant.contestParticipant.fullname}</strong>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<c:if test="${submissions.size() gt 5}">
    <t:button onclick="toggleSubmissionList('acceptedSubmissions');" styleClass="btn-sm">Load more</t:button>
</c:if>
