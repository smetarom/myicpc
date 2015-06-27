<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<div id="timeline-poll-carousel" class="carousel slide timeline-carousel" data-ride="carousel" data-interval="15000" data-keyboard="false">
    <script id="poll-option-template" type="text/x-jquery-tmpl">
        <select>
            {{#each comments}}
              <option id="{{id}}" label="{{name}}">{{name}}</option>
            {{/each}}
        </select>
    </script>
    <script type="application/javascript">
        var pollOptionTemplate = compileHandlebarsTemplate("poll-option-template")
    </script>
    <div class="carousel-inner" role="listbox">
        <c:forEach var="notification" items="${openPolls}" varStatus="status">
            <div class="item ${status.index == 0 ? 'active' : ''}">
                <table class="width100">
                    <tr>
                        <td>
                            <a class="btn btn-link" href="#timeline-quest-carousel" role="button" data-slide="prev">
                                <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                                <span class="sr-only">Previous</span>
                            </a>
                        </td>
                        <td>
                            <h4>${notification.title}</h4>
                        </td>
                        <td class="text-right">
                            <a class="btn btn-link" href="#timeline-quest-carousel" role="button" data-slide="next">
                                <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                                <span class="sr-only">Next</span>
                            </a>
                        </td>
                    </tr>
                </table>
                <div id="poll-option-wrapper" class="content">

                </div>
                <div class="text-center">
                    <t:button context="primary"><spring:message code="quest.challenge.participate" /></t:button>
                </div>

                <script type="javascript">
                    
                </script>
            </div>
        </c:forEach>
    </div>
</div>