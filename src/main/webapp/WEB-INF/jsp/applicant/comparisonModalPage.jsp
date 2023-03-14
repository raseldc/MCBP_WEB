<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<div class="modal fade" id="comparisonModal" tabindex="-1" role="dialog" aria-labelledby="comparisonModalLabel" aria-hidden="true" style="opacity: 1; padding: 50px 0; height: 90%">
    <div class="vertical-alignment-helper">
        <div class="modal-dialog vertical-align-center" style="width: 70%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span>

                    </button>
                    <h3 class="modal-title" id="comparisonModalLabel">
                        <spring:message code='label.viewComparisonPageHeader'/>
                        ${headerTitle}
                    </h3>

                </div>
                <div class="modal-body" style="height: auto">
                    ...
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default bg-blue" data-dismiss="modal"><spring:message code="close"/></button>
                </div>
            </div>
        </div>
    </div>
</div>
