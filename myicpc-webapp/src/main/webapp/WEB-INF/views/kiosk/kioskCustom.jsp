<%@ include file="/WEB-INF/views/includes/taglibs.jsp" %>

<t:templateKiosk>
    <jsp:attribute name="head">
        <style type="text/css">
            html, body {
                height: 100%;
                margin: 0;
            }
            iframe {
                display: block;
                background: white;
                border: none;
                width: 100%;
                height: 100%;
            }
        </style>
    </jsp:attribute>
    <jsp:attribute name="javascript">
        <script type="application/javascript">
            updateKioskPage = function(data) {
                location.reload();
            };

            $(function() {
                startSubscribe('${r.contextPath}', '${contest.code}', 'kiosk', updateKioskPage, null);
            });

        </script>

    </jsp:attribute>
    <jsp:body>
        <div style="padding-top: 100px; height: 100%">
            <iframe src="<spring:url value="${contestURL}/kiosk/custom/content" />" frameborder="0">
            </iframe>
        </div>
    </jsp:body>
</t:templateKiosk>
