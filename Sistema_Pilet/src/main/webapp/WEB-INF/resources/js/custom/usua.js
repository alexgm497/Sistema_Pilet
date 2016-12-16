$(document).ready(function() {
    $.fn.initBootTable = function() {
        $(this).bootstrapTable('destroy');
        $(this).bootstrapTable().
        unbind('check.bs.table').on('check.bs.table', function (e, row) {
            cons([{name : 'codiObjePara', value : row.id.trim()}]);
        });
        return false;
    };
    $.fn.initBootTableRole = function() {
        $(this).bootstrapTable('destroy');
        $(this).bootstrapTable().
        unbind('load-success.bs.table').on('load-success.bs.table', function (e, row) {
            $("[data-id='chck']").bootstrapToggle();
        }).
        unbind('page-change.bs.table').on('page-change.bs.table', function (e, row) {
            $("[data-id='chck']").bootstrapToggle();
        });
        return false;
    };
    $('#ModaFormRegi').on('show.bs.modal', function() {
        INIT_OBJE();
        $("[data-id='chck']").bootstrapToggle();
    });
    $('#ModaFormRegi').on('hide.bs.modal', function() {
        $("#TablRegi").bootstrapTable('uncheckAll');
        $("[data-id='chck']").bootstrapToggle();
    });
    INIT_OBJE();
    $("#TablRegi1").initBootTableRole();
    $("[data-id='chck']").bootstrapToggle();
});

function INIT_OBJE()
{
    $("[data-id='chck']").bootstrapToggle();
    $("#TablRegi").initBootTable();
    INIT_OBJE_MODA();
}

function RECA()
{
    $("[data-id='chck']").bootstrapToggle();
}

function INIT_OBJE_MODA()
{
    $("[data-id='chck']").bootstrapToggle();
    $("#FormRegi\\:role").selectpicker();
}
