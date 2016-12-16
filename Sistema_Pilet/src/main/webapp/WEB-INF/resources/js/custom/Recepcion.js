$(document).ready(function() {
    $.fn.initBootTableVisi = function() {
        $(this).bootstrapTable('destroy');
        $(this).bootstrapTable().
        unbind('check.bs.table').on('check.bs.table', function (e, row) {
            consObjeVisi([{name : 'codiObjeVisi', value : row.id.trim()}]);
        });
        return false;
    };
    $.fn.initBootTableEmpl = function() {
        $(this).bootstrapTable('destroy');
        $(this).bootstrapTable().
        unbind('check.bs.table').on('check.bs.table', function (e, row) {
            consObjeEmpl([{name : 'codiObjeEmpl', value : row.id.trim()}]);
        });
        return false;
    };
    $.fn.initBootTableVisiCita = function() {
        $(this).bootstrapTable('destroy');
        $(this).bootstrapTable().
        unbind('check.bs.table').on('check.bs.table', function (e, row) {
            consObjeVisiCita([{name : 'codiObjeVisiCita', value : row.id.trim()}]);
        });
        return false;
    };
});
function INIT_OBJE_TABL_VISI(){
    $("#TablVisi").initBootTableVisi();
}
function INIT_OBJE_TABL_EMPL(){
    $("#TablEmpl").initBootTableEmpl();
}
function INIT_OBJE_REGI_VISI(){
    $("#TablVisiCita").initBootTableVisiCita();
}
