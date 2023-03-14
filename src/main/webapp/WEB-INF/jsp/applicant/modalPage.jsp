<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="opacity: 1; padding: 50px 0; height: 90%">
    <div class="vertical-alignment-helper">
        <div class="modal-dialog vertical-align-center" style="width: 70%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span>

                    </button>
                    <h3 class="modal-title" id="myModalLabel">
                        <spring:message code='label.viewNewBeneficiaryPageHeader'/>
                        ${headerTitle}
                    </h3>

                </div>
                <div class="modal-body" style="height: 603px">
                    <img src="${pageContext.request.contextPath}/resources/img/loading.gif" class="image">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default bg-blue" data-dismiss="modal"><spring:message code="close"/></button>
                </div>
            </div>
        </div>
    </div>
</div>