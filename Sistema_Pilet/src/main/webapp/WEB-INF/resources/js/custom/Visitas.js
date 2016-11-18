$(document).ready(function() {
    $.fn.initBootTableVisi = function() {
        $(this).bootstrapTable('destroy');
        $(this).bootstrapTable().
        unbind('check.bs.table').on('check.bs.table', function (e, row) {
            consObjeVisi([{name : 'codiObjeVisi', value : row.id.trim()}]);
        });
        return false;
    };
    INIT_OBJE_CITA();
});
function INIT_OBJE_CITA(){
    $("#FormRegi\\:btonAgreVisi").confirmation({container: '#FormRegi'});
    
 //$("#TablAlum").initBootTableAlum();
}
function INIT_OBJE_TABL(){
    $("#TablVisi").initBootTableVisi();
    //$("#TablVisiCita").bootstrapTable();
     //$("#TablInvo").bootstrapTable();
    //setMessage('MESS_INFO', 'Atención', 'Inicializando...');
}